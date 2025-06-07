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
            skillService.createSkill(SkillDto("1", "Java", listOf(tagBackend), 1400000))
        val createSkill1 =
            skillService.createSkill(SkillDto("2", "Kotlin", listOf(tagAndroid), 140000))

        skillService.addTrackHistoryBySkillId(HistorySkill(skillId = createSkill.id, date = LocalDate.now(),
            timeTackedSeconds = createSkill.timeTotalSeconds))

        skillService.addTrackHistoryBySkillId(HistorySkill(skillId = createSkill1.id, date = LocalDate.now(),
            timeTackedSeconds = createSkill1.timeTotalSeconds))

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


