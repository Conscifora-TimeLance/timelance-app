package com.nexora.timelance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexora.timelance.data.repository.list.SkillRepositoryImpl
import com.nexora.timelance.data.repository.list.TagRepositoryImpl
import com.nexora.timelance.ui.components.HomeDrawer
import com.nexora.timelance.ui.components.TimelanceMenu
import com.nexora.timelance.ui.screen.ShowSkillAddScreen
import com.nexora.timelance.ui.screen.ShowHomeScreen
import com.nexora.timelance.ui.screen.ShowSkillHubScreen
import com.nexora.timelance.ui.screen.ShowSkillScreen
import com.nexora.timelance.ui.theme.TimelanceTheme

import kotlinx.coroutines.launch


const val HOME_SCREEN = "HomeScreen"
const val SKILL_SCREEN = "SkillScreen"
const val SKILL_HUB_SCREEN = "SkillHubScreen"
const val SKILL_ADD_SCREEN = "SkillAddScreen"

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

    val menu = TimelanceMenu()
    val tagRepository = TagRepositoryImpl()
    val skillRepository = SkillRepositoryImpl(tagRepository)

    @Preview(showBackground = true)
    @Composable
    fun MainNavHost(navController: NavHostController = rememberNavController()) {
        // TODO I DO NO LIKE THIS DUDE

        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val homeDrawer = HomeDrawer()
        val scope = rememberCoroutineScope()

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
                    .padding(bottom = 16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    NavHost(navController = navController, startDestination = HOME_SCREEN) {
                        composable(HOME_SCREEN) { ShowHomeScreen() }
                        composable(SKILL_HUB_SCREEN) { ShowSkillHubScreen(navController, skillRepository) }
                        composable(SKILL_SCREEN) { ShowSkillScreen() }
                        composable(SKILL_ADD_SCREEN) { ShowSkillAddScreen(skillRepository) }
                    }
                }
            }

        }

    }






}


