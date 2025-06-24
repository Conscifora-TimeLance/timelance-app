package com.nexora.timelance.ui.components.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nexora.timelance.data.service.impl.SkillServiceImpl
import com.nexora.timelance.ui.components.navigation.AppDestinations.ROUTE_HOME_SCREEN
import com.nexora.timelance.ui.components.navigation.AppDestinations.ROUTE_SKILL_ADD_SCREEN
import com.nexora.timelance.ui.components.navigation.AppDestinations.ROUTE_SKILL_HUB_SCREEN
import com.nexora.timelance.ui.components.search.GlobalSearchResultsOverlay
import com.nexora.timelance.ui.screen.SkillAddScreen
import com.nexora.timelance.ui.screen.HomeScreenDestination
import com.nexora.timelance.ui.screen.SkillHubScreen
import com.nexora.timelance.ui.screen.SkillDetailsScreen
import com.nexora.timelance.ui.screen.SkillHubState
import com.nexora.timelance.ui.theme.TimelanceTheme
import com.nexora.timelance.ui.viewmodel.GlobalSearchViewModel
import com.nexora.timelance.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object AppDestinations {

    const val SKILL_ID_ARG = "skillId"
    const val TAG_ID_ARG = "tagId"

    const val ROUTE_HOME_SCREEN = "home_screen"
    const val ROUTE_SKILL_HUB_SCREEN = "skill_hub_screen"
    const val ROUTE_SKILL_ADD_SCREEN = "skill_add_screen"
    const val ROUTE_SKILLS_DETAIL_SCREEN = "skill_screen"

    const val ROUTE_SKILL_DETAILS_SCREEN_WITH_ARG = "$ROUTE_SKILLS_DETAIL_SCREEN/{$SKILL_ID_ARG}"
    const val ROUTE_SKILL_HUB_SCREEN_WITH_ARG = "$ROUTE_SKILL_HUB_SCREEN/{$TAG_ID_ARG}"

}

@Preview(showBackground = true)
@Composable
private fun PreviewScreen() {
    TimelanceTheme {
        val skillService = SkillServiceImpl()
        AppNavigationGraph(skillService = skillService)
    }
}

@Composable
fun AppNavigationGraph(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    scope: CoroutineScope = rememberCoroutineScope(),
    homeDrawer: HomeDrawer = HomeDrawer(),
    menu: TimelanceMenu = TimelanceMenu(),
    skillService: SkillServiceImpl,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val globalSearchViewModel: GlobalSearchViewModel = remember { GlobalSearchViewModel(skillService) }
    val globalSearchUiState by globalSearchViewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    BackHandler(enabled = globalSearchUiState.isSearchActive) {
        globalSearchViewModel.toggleSearchActive()
        keyboardController?.hide()
    }

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

        val screenTitle = when (currentRoute) {
            ROUTE_HOME_SCREEN -> "Home"
            ROUTE_SKILL_HUB_SCREEN, AppDestinations.ROUTE_SKILL_HUB_SCREEN_WITH_ARG -> "Skill Hub"
            ROUTE_SKILL_ADD_SCREEN -> "Add Skill"
            AppDestinations.ROUTE_SKILL_DETAILS_SCREEN_WITH_ARG -> "Skill Details"
            else -> "TimeLance"
        }

        Scaffold(

            topBar = {
//                menu.Header(drawerState = drawerState, scope = scope, screenTitle)

                menu.AppHeader(
                    title = screenTitle,
                    drawerState = drawerState,
                    scope = scope,
                    isGlobalSearchActive = globalSearchUiState.isSearchActive,
                    globalSearchQuery = globalSearchUiState.searchQuery,
                    onGlobalSearchQueryChange = globalSearchViewModel::onSearchQueryChange,
                    onGlobalSearchToggle = {
                        globalSearchViewModel.toggleSearchActive()
                        if (!globalSearchUiState.isSearchActive) keyboardController?.hide() // Скрываем клаву при закрытии
                    },
                    onGlobalSearchSubmit = { query ->
                        globalSearchViewModel.onSearchSubmit(query)
                        keyboardController?.hide()
                    }
                )
            },

            bottomBar = {
                menu.BottomMenu(navController)
            },
            modifier = Modifier
                .padding(10.dp, 5.dp)
                .statusBarsPadding()
                .systemBarsPadding()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                NavHost(navController = navController, startDestination = ROUTE_HOME_SCREEN) {
                    composable(ROUTE_HOME_SCREEN) {
                        val homeViewModel = remember { HomeViewModel(skillService) }
                        HomeScreenDestination(viewModel = homeViewModel)
                    }
                    composable(ROUTE_SKILL_HUB_SCREEN) {
                        SkillHubScreen(
                            null,
                            navController,
                            onClickItemSkillNavigationToDetails = { skillId ->
                                navController.navigate("${AppDestinations.ROUTE_SKILLS_DETAIL_SCREEN}/$skillId") },
                            skillService,
                        )
                    }
                    composable(
                        route = AppDestinations.ROUTE_SKILL_HUB_SCREEN_WITH_ARG,
                        arguments = listOf(navArgument(AppDestinations.TAG_ID_ARG) {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->
                        val tagId = backStackEntry.arguments?.getString(AppDestinations.TAG_ID_ARG)

                        if (tagId != null) {
                            SkillHubScreen(
                                tagId,
                                navController,
                                onClickItemSkillNavigationToDetails = { skillId ->
                                    navController.navigate("${AppDestinations.ROUTE_SKILLS_DETAIL_SCREEN}/$skillId") },
                                skillService,
                            )
                        } else {
                            Text("Error: Skill ID not provided.")
                            LaunchedEffect(Unit) {
                                kotlinx.coroutines.delay(2000)
                                navController.popBackStack()
                            }
                        }
                    }
//                    composable(ROUTE_SKILLS_DETAIL_SCREEN) { SkillDetailsScreen(null, skillService) }
                    composable(
                        route = AppDestinations.ROUTE_SKILL_DETAILS_SCREEN_WITH_ARG,
                        arguments = listOf(navArgument(AppDestinations.SKILL_ID_ARG) {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->
                        val skillId = backStackEntry.arguments?.getString(AppDestinations.SKILL_ID_ARG)

                        if (skillId != null) {
                            SkillDetailsScreen(
                                skillId,
                                skillService,
                                onTagToSkillHubFilter = {
                                    tagId ->
                                        navController.navigate("${AppDestinations.ROUTE_SKILL_HUB_SCREEN}/$tagId")
                                }
                            )
                        } else {
                            Text("Error: Skill ID not provided.")
                            LaunchedEffect(Unit) {
                                kotlinx.coroutines.delay(2000)
                                navController.popBackStack()
                            }
                        }
                    }
                    composable(ROUTE_SKILL_ADD_SCREEN) {
                        SkillAddScreen(
                            skillService,
                            onSkillSavedAndNavigateBack = { navController.navigate(AppDestinations.ROUTE_SKILL_HUB_SCREEN) }
                        )
                    }
                }

                GlobalSearchResultsOverlay(
                    uiState = globalSearchUiState,
                    onSkillClick = { skill ->
                        globalSearchViewModel.toggleSearchActive()
                        keyboardController?.hide()
                        globalSearchViewModel.clearSearchResultsAndQuery()
                        navController.navigate("${AppDestinations.ROUTE_SKILLS_DETAIL_SCREEN}/${skill.skillId}")
                    },
                    onDismissRequest = {
                        globalSearchViewModel.toggleSearchActive()
                        keyboardController?.hide()
                    }
                )
            }
        }
    }
}