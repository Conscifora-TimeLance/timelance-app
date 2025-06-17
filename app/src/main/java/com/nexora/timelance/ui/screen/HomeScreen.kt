package com.nexora.timelance.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexora.timelance.ui.model.ProgressData
import com.nexora.timelance.ui.components.card.ProgressItem
import com.nexora.timelance.ui.components.stat.StatisticsGraph
import com.nexora.timelance.ui.theme.TimelanceTheme
import com.nexora.timelance.ui.viewmodel.HomeUiState
import com.nexora.timelance.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreenDestination(
    viewModel: HomeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreenContent(uiState = uiState)
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState
) {
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (uiState.error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp) // Общий паддинг
    ) {
        Column {
            Text(
                text = "Total tracked: ${uiState.totalTimeTracked}",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.dailyActivity.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatisticsGraph(uiState.dailyActivity)
            }
        } else {
            Text("No activity data for graph.")
        }


        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Today's recent tracking",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.todayProgressItems.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                // userScrollEnabled = false,
            ) {
                items(uiState.todayProgressItems) { item ->
                    ProgressItem(
                        title = item.title,
                        time = item.timeTrackedSeconds
                        // progress = item.progress
                    )
                }
            }
        } else {
            Text("No tracking مسجد for today yet.")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeScreen() {
    TimelanceTheme {
        val previewUiState = HomeUiState(
            totalTimeTracked = "10h 30m",
            dailyActivity = emptyList(),
            todayProgressItems = listOf(
                ProgressData("Kotlin", 3600L, 1f),
                ProgressData("Java", 7200L, 1f)
            ),
            isLoading = false
        )
        HomeScreenContent(uiState = previewUiState)
    }
}

@Preview(showBackground = true, name = "HomeScreen Loading")
@Composable
private fun PreviewHomeScreenLoading() {
    TimelanceTheme {
        HomeScreenContent(uiState = HomeUiState(isLoading = true))
    }
}

//@Composable
//fun HomeScreen (
//    skillService: SkillServiceImpl
//) {
//
//    val skillsAndHistory = skillService.getAllHistory()
//    val allHistory = skillsAndHistory.flatMap { it.histories }
//    val totalTimed = allHistory.sumOf { it.timeTrackedSeconds }
//    var todaySkillTracked = skillsAndHistory
//        .filter { group -> group.histories
//            .all { it.date == LocalDate.now() }
//        }
//        .map { group -> ProgressData(
//            group.skill.name,
//            group.histories.sumOf { it.timeTrackedSeconds },
//            100f)
//        }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(
//                rememberScrollState()
//            )
//    ) {
//
//        Column {
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = "Total timed: ${secondsToTrackedTime(totalTimed)}"
//            )
//        }
//
//        Row(
//            modifier = Modifier
//                .height(300.dp),
//            horizontalArrangement = Arrangement.Start,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            StatisticsGraph(allHistory)
//        }
//
//        Text(
//            text = "Today's recent tracking",
//            style = MaterialTheme.typography.bodyLarge
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            modifier = Modifier.height(300.dp),
//            contentPadding = PaddingValues(8.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//            userScrollEnabled = true
//        ) {
//            items(todaySkillTracked) { item ->
//                ProgressItem(
//                    title = item.title,
//                    time = item.timeTrackedSeconds
//                )
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//private fun PreviewScreen() {
//    TimelanceTheme {
//        HomeScreen(SkillServiceImpl())
//    }
//}