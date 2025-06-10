package com.nexora.timelance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.nexora.timelance.data.dto.SkillDto
import com.nexora.timelance.data.service.impl.SkillServiceImpl
import com.nexora.timelance.domain.model.entity.HistorySkill
import com.nexora.timelance.domain.model.entity.Tag
import com.nexora.timelance.ui.components.navigation.AppNavigationGraph
import com.nexora.timelance.ui.theme.TimelanceTheme
import java.time.LocalDate
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val skillService = SkillServiceImpl()
        val tagAndroid = skillService.createTag(Tag(UUID.randomUUID().toString(), "Android"))
        val tagBackend = skillService.createTag(Tag(UUID.randomUUID().toString(), "Backend"))

        val createSkill =
            skillService.createSkill(SkillDto("1", "Java", listOf(tagBackend), 140000))
        val createSkill1 =
            skillService.createSkill(SkillDto("2", "Kotlin", listOf(tagAndroid), 14000))

        skillService.addTrackHistoryBySkillId(HistorySkill(skillId = createSkill.id, date = LocalDate.now(),
            timeTrackedSeconds = createSkill.timeTotalSeconds))

        skillService.addTrackHistoryBySkillId(HistorySkill(skillId = createSkill1.id, date = LocalDate.now(),
            timeTrackedSeconds = createSkill1.timeTotalSeconds))

        skillService.addTrackHistoryBySkillId(HistorySkill(skillId = createSkill1.id, date = LocalDate.now().minusDays(8),
            timeTrackedSeconds = 16000L))

        skillService.addTrackHistoryBySkillId(HistorySkill(skillId = createSkill1.id, date = LocalDate.now().minusDays(7),
            timeTrackedSeconds = 10000L))

        skillService.addTrackHistoryBySkillId(HistorySkill(skillId = createSkill1.id, date = LocalDate.now().minusDays(6),
            timeTrackedSeconds = 10000L))

        skillService.addTrackHistoryBySkillId(HistorySkill(skillId = createSkill1.id, date = LocalDate.now().minusDays(5),
            timeTrackedSeconds = 10000L))

        skillService.addTrackHistoryBySkillId(HistorySkill(skillId = createSkill1.id, date = LocalDate.now().minusDays(4),
            timeTrackedSeconds = 10000L))

        skillService.addTrackHistoryBySkillId(HistorySkill(skillId = createSkill1.id, date = LocalDate.now().minusDays(3),
            timeTrackedSeconds = 10000L))

        skillService.addTrackHistoryBySkillId(HistorySkill(skillId = createSkill1.id, date = LocalDate.now().minusDays(2),
            timeTrackedSeconds = 1000L))

        skillService.addTrackHistoryBySkillId(HistorySkill(skillId = createSkill1.id, date = LocalDate.now().minusDays(1),
            timeTrackedSeconds = 1000L))

        setContent {
            TimelanceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigationGraph(
                        skillService = skillService
                    )
                }
            }
        }
    }
}


