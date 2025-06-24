package com.nexora.timelance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexora.timelance.data.dto.SkillDto
import com.nexora.timelance.data.service.SkillService
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class GlobalSearchUiState(
    val isSearchActive: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<SkillDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@OptIn(FlowPreview::class)
class GlobalSearchViewModel(
    private val skillService: SkillService
) : ViewModel() {

    private val _uiState = MutableStateFlow(GlobalSearchUiState())
    val uiState: StateFlow<GlobalSearchUiState> = _uiState.asStateFlow()

    // Используем для debounce
    private val _searchQueryInternal = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _searchQueryInternal
                .debounce(300) // Задержка перед выполнением поиска
                .distinctUntilChanged() // Выполнять поиск только если запрос изменился
                .collectLatest { query -> // collectLatest отменит предыдущий поиск, если пришел новый запрос
                    if (query.isBlank()) {
                        _uiState.update { it.copy(searchResults = emptyList(), isLoading = false) }
                    } else {
                        performSearch(query)
                    }
                }
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Предполагаем, что skillService может искать по всем навыкам
                // Возможно, вам понадобится метод вроде skillService.searchSkillsByName(query)
                val allSkills = skillService.getAllSkills() // Временное решение, лучше иметь спец. метод поиска
                val results = allSkills.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.groupTags.any { tag -> tag.name.contains(query, ignoreCase = true) }
                }
                _uiState.update { it.copy(searchResults = results, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Search failed: ${e.message}") }
            }
        }
    }

    fun toggleSearchActive() {
        val newState = !_uiState.value.isSearchActive
        _uiState.update { it.copy(isSearchActive = newState) }
        if (!newState) {
            // Очищаем запрос и результаты при закрытии
            onSearchQueryChange("")
            _uiState.update { it.copy(searchResults = emptyList()) }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        _searchQueryInternal.value = query
    }

    fun onSearchSubmit(query: String) {
        // Может быть полезно для немедленного поиска без debounce
        if (query.isNotBlank()) {
            performSearch(query)
        }
        println("Global search submitted: $query")
    }

    fun clearSearchResultsAndQuery() {
        onSearchQueryChange("")
        _uiState.update { it.copy(searchResults = emptyList())}
    }
}