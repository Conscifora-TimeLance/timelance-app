package com.nexora.timelance.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexora.timelance.data.service.impl.SkillServiceImpl
import com.nexora.timelance.ui.model.ProgressData
import com.nexora.timelance.ui.components.card.ProgressItem
import com.nexora.timelance.ui.components.stat.StatisticsGraph
import com.nexora.timelance.ui.theme.TimelanceTheme
import com.nexora.timelance.util.TimeUtil.Companion.secondsToTrackedTime
import java.time.LocalDate

@Composable
fun HomeScreen (
    skillService: SkillServiceImpl
) {

    val skillsAndHistory = skillService.getAllHistory()
    val allHistory = skillsAndHistory.flatMap { it.histories }
    val totalTimed = allHistory.sumOf { it.timeTrackedSeconds }
    var todaySkillTracked = skillsAndHistory
        .filter { group -> group.histories
            .all { it.date == LocalDate.now() }
        }
        .map { group -> ProgressData(
            group.skill.name,
            group.histories.sumOf { it.timeTrackedSeconds },
            100f)
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
    ) {

        Column {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total timed: ${secondsToTrackedTime(totalTimed)}"
            )
        }

        Row(
            modifier = Modifier
                .height(300.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatisticsGraph(allHistory)
        }

        Text(
            text = "Today's recent tracking",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height(300.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = true
        ) {
            items(todaySkillTracked) { item ->
                ProgressItem(
                    title = item.title,
                    time = item.time
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewScreen() {
    TimelanceTheme {
        HomeScreen(SkillServiceImpl())
    }
}