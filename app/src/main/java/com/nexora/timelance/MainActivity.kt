package com.nexora.timelance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexora.timelance.data.model.DailyStat
import com.nexora.timelance.ui.components.HomeDrawer
import com.nexora.timelance.ui.components.CUIGraph
import com.nexora.timelance.ui.theme.ProgressBackgroundColorLight
import com.nexora.timelance.ui.theme.ProgressFillColorLight
import com.nexora.timelance.ui.theme.TimelanceTheme
import kotlin.math.roundToInt

import com.nexora.timelance.ui.theme.*;
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimelanceTheme {
                MainScreen()
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    TimelanceTheme {
        MainScreen()
    }
}

@Composable
fun MainScreen() {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val homeDrawer = HomeDrawer()
    val scope = rememberCoroutineScope()

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

    val items = listOf("Home", "Contact", "About")
    val selectedItem = remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            homeDrawer.DrawerContent(
                drawerState = drawerState,
                onMenuItemClick = { menuItem ->
                    println("Выбранный элемент: $menuItem")
                    scope.launch { drawerState.close() }
                }
            )
        }

    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
            ) {

//            homeDrawer.DrawerContent(
//                drawerState = drawerState,
//                onMenuItemClick = { menuItem ->
//                    println("Выбранный элемент: $menuItem")
//                }
//            )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {scope.launch {drawerState.open()}},
                        content = { Icon(Icons.Filled.Menu, "Меню") }
                    )

                    IconButton(onClick = { /* Handle search action */ }) {
                        Icon(
                            imageVector = Icons.Filled.Search, // Use Material icon for search
                            contentDescription = "Search",
                            modifier = Modifier.size(24.dp) // Optional: Set icon size
                        )
                    }
                }

                // Время управления
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
                    CUIGraph().StatisticsGraph(statisticsSevenDaysData)
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
                        ProgressItem(title = item.title, time = item.time, progress = item.progress)
                    }
                }

                // Статистика и ближайшая цель
                Column {
                    Text(
                        text = "Total Statistics",
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Time: 2000 H 56 M\nStreak days: 202 d"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ProgressItem(
                        title = "Nearest goal: Java",
                        time = "460 / 520 H",
                        progress = 0.91f
                    )
                }

                // Кнопки внизу
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {}) { Text("ГЛ") }
                    Button(onClick = {}) { Text("ГР") }
                    Button(onClick = {}) { Text("НА") }
                    Button(onClick = {}) { Text("ОТ") }
                }
            }
        }
    }

}

data class ProgressData(val title: String, val time: String, val progress: Float)

@Composable
fun ProgressItem(title: String, time: String, progress: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(SecondColorLight)
            .padding(vertical = 20.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = {
                progress
            },
            modifier = Modifier.fillMaxWidth(),
            color = ProgressFillColorLight,
            trackColor = ProgressBackgroundColorLight,
        )
        Spacer(modifier = Modifier.height(4.dp))


        val percent: String = (progress * 100).roundToInt().toString();

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = time, style = MaterialTheme.typography.bodySmall)
            Text(text = "$percent%", style = MaterialTheme.typography.bodySmall)
        }

    }
}
