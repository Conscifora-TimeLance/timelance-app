package com.nexora.timelance.ui.components.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexora.timelance.domain.repository.SkillRepository
import com.nexora.timelance.domain.service.SkillService
import com.nexora.timelance.ui.components.navigation.AppDestinations.ROUTE_HOME_SCREEN
import com.nexora.timelance.ui.components.navigation.AppDestinations.ROUTE_SKILL_ADD_SCREEN
import com.nexora.timelance.ui.components.navigation.AppDestinations.ROUTE_SKILL_HUB_SCREEN
import com.nexora.timelance.ui.components.navigation.AppDestinations.ROUTE_SKILL_SCREEN
import com.nexora.timelance.ui.screen.SkillAddScreen
import com.nexora.timelance.ui.screen.HomeScreen
import com.nexora.timelance.ui.screen.SkillHubScreen
import com.nexora.timelance.ui.screen.SkillScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object AppDestinations {

    const val ROUTE_HOME_SCREEN = "home_screen"
    const val ROUTE_SKILL_HUB_SCREEN = "skill_hub_screen"
    const val ROUTE_SKILL_SCREEN = "skill_screen"
    const val ROUTE_SKILL_ADD_SCREEN = "skill_add_screen"
}

//@Preview(showBackground = true)
@Composable
fun AppNavigationGraph(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    scope: CoroutineScope = rememberCoroutineScope(),
    homeDrawer: HomeDrawer = HomeDrawer(),
    menu: TimelanceMenu = TimelanceMenu(),
    skillService: SkillService,
) {
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
                .padding(10.dp, 5.dp)
                .statusBarsPadding()
                .padding(bottom = 16.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                NavHost(navController = navController, startDestination = ROUTE_HOME_SCREEN) {
                    composable(ROUTE_HOME_SCREEN) { HomeScreen() }
                    composable(ROUTE_SKILL_HUB_SCREEN) {
                        SkillHubScreen(
                            navController,
                            skillService
                        )
                    }
                    composable(ROUTE_SKILL_SCREEN) { SkillScreen(null, null) }
                    composable(ROUTE_SKILL_ADD_SCREEN) {
                        SkillAddScreen(
                            skillService,
                            onSkillSavedAndNavigateBack = { navController.navigate(AppDestinations.ROUTE_SKILL_HUB_SCREEN) }
                        )
                    }
                }
            }
        }
    }
}