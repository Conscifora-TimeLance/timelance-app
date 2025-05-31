package com.nexora.timelance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.nexora.timelance.data.repository.list.HistorySkillRepositoryImpl
import com.nexora.timelance.data.repository.list.SkillRepositoryImpl
import com.nexora.timelance.data.repository.list.TagRepositoryImpl
import com.nexora.timelance.data.service.SkillServiceImpl
import com.nexora.timelance.ui.components.navigation.AppNavigationGraph
import com.nexora.timelance.ui.theme.TimelanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val tagRepository = TagRepositoryImpl()
        val historySkillRepository = HistorySkillRepositoryImpl()
        val skillRepository = SkillRepositoryImpl(tagRepository, historySkillRepository)

        setContent {
            TimelanceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigationGraph(
                        skillService = SkillServiceImpl()
                    )
                }
            }
        }
    }
}


