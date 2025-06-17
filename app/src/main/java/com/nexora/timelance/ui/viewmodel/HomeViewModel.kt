package com.nexora.timelance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexora.timelance.data.service.SkillService
import com.nexora.timelance.domain.model.entity.HistorySkill
import com.nexora.timelance.ui.model.ProgressData
import com.nexora.timelance.util.TimeUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.time.Duration.Companion.seconds

data class HomeUiState(
    val totalTimeTracked: String = "",
    val dailyActivity: List<HistorySkill> = emptyList(),
    val todayProgressItems: List<ProgressData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(
    private val skillService: SkillService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeScreenData()
    }

    private fun loadHomeScreenData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val skillsAndHistory = skillService.getAllHistory()
                val allHistory = skillsAndHistory.flatMap { it.histories }
                val totalTimedSeconds = allHistory.sumOf { it.timeTrackedSeconds }

                val todaySkillTracked = skillsAndHistory
                    .filter { group ->
                        group.histories.any { it.date == LocalDate.now() }
                    }
                    .mapNotNull { group ->
                        val todayTime = group.histories
                            .filter { it.date == LocalDate.now() }
                            .sumOf { it.timeTrackedSeconds }

                        // delay(1.seconds)

                        if (todayTime > 0) {
                            ProgressData(
                                title = group.skill.name,
                                timeTrackedSeconds = todayTime,
                                progress = 1.0f
                            )
                        } else null
                    }


                _uiState.update {
                    it.copy(
                        totalTimeTracked = TimeUtil.secondsToTrackedTime(totalTimedSeconds),
                        dailyActivity = allHistory,
                        todayProgressItems = todaySkillTracked,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load data: ${e.message}") }
            }
        }
    }
}