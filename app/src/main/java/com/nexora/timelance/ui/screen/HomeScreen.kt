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
import com.nexora.timelance.ui.model.DailyStat
import com.nexora.timelance.ui.model.ProgressData
import com.nexora.timelance.ui.components.SkillGraph
import com.nexora.timelance.ui.components.ProgressItem
import com.nexora.timelance.ui.theme.TimelanceTheme

@Composable
fun HomeScreen () {

    // TODO отрефакторить в другое место
    val progressItems = listOf(
        ProgressData("Java Practice", "3H 17 min", 0.5f),
        ProgressData("Japanese Vocabulary", "1H 7 min", 0.5f),
        ProgressData("English Vocabulary", "47 min", 0.5f),
        ProgressData("English Reading", "37 min", 0.5f),
        ProgressData("English Reading", "37 min", 0.5f),
        ProgressData("English Reading", "37 min", 0.5f)
    )

    val statisticsSevenDaysData = listOf(
        DailyStat("3 Sep", 3, 0),
        DailyStat("4 Sep", 2, 23),
        DailyStat("5 Sep", 1, 15),
        DailyStat("6 Sep", 4, 5),
        DailyStat("7 Sep", 0, 45),
        DailyStat("8 Sep", 2, 10),
        DailyStat("9 Sep", 3, 30)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
    ) {

//            homeDrawer.DrawerContent(
//                drawerState = drawerState,
//                onMenuItemClick = { menuItem ->
//                    println("Выбранный элемент: $menuItem")
//                }
//            )

        // TODO HEADER MENU IS HERE

        Column {
            Text(
                text = "Time management"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "7 H 16 M"
            )
        }

        Row(
            modifier = Modifier
                .height(300.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkillGraph().StatisticsGraph(statisticsSevenDaysData)
        }

        Text(
            text = "Today's recent tracking",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height(200.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = true
        ) {
            items(progressItems) { item ->
                ProgressItem(
                    title = item.title,
                    time = item.time,
                    progress = item.progress
                )
            }
        }

        Column {
            Text(
                text = "Total Statistics",
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Time: 2000 H 56 M\nStreak days: 202 d"
            )
            Spacer(modifier = Modifier.height(16.dp))
//            ProgressItem(
//                title = "Nearest goal: Java",
//                time = "460 / 520 H",
//                progress = 0.91f
//            )
        }

        // TODO BOTTOM MENU IS HERE


    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewScreen() {
    TimelanceTheme {
        HomeScreen()
    }
}