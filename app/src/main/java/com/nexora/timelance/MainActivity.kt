package com.nexora.timelance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexora.timelance.data.model.DailyStat
import com.nexora.timelance.data.model.ProgressData
import com.nexora.timelance.ui.screen.SkillHubScreen
import com.nexora.timelance.ui.components.HomeDrawer
import com.nexora.timelance.ui.components.CUIGraph
import com.nexora.timelance.ui.components.ProgressComp
import com.nexora.timelance.ui.components.TimelanceMenu
import com.nexora.timelance.ui.theme.TimelanceTheme

import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimelanceTheme {
                MainNavHost()
            }
        }
    }

    val skillHubScreen = SkillHubScreen()
    val menu = TimelanceMenu()
    val progressComp = ProgressComp();

    @Composable
    fun MainNavHost(navController: NavHostController = rememberNavController()) {
        // TODO I DO NO LIKE THIS DUDE




        NavHost(navController = navController, startDestination = "screen1") {
            composable("screen1") { MainScreen(navController) }
            composable("SkillHub") { skillHubScreen.Screen(navController) }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewMainScreen() {
        TimelanceTheme {
            MainScreen(rememberNavController())
        }
    }

    @Composable
    fun MainScreen(navController: NavHostController) {

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
            Scaffold(
                topBar = {
                    menu.Header(drawerState = drawerState, scope = scope)
                },
                bottomBar = {
                    menu.BottomMenu(navController)
                },
                modifier = Modifier
                    .padding(10.dp,5.dp)
                    .statusBarsPadding()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
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
                                progressComp.ProgressItem(
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
                            progressComp.ProgressItem(
                                title = "Nearest goal: Java",
                                time = "460 / 520 H",
                                progress = 0.91f
                            )
                        }

                        // TODO BOTTOM MENU IS HERE


                    }
                }
            }

        }
    }




}


