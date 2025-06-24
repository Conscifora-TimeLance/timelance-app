package com.nexora.timelance.ui.components.navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nexora.timelance.R
import com.nexora.timelance.ui.components.button.ButtonPrimary
import com.nexora.timelance.ui.theme.PrimaryAccentColorLight
import com.nexora.timelance.ui.theme.SecondAccentColorLight
import com.nexora.timelance.ui.theme.SecondColorLight
import com.nexora.timelance.ui.theme.TimelanceTheme

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TimelanceMenu {

    @Preview
    @Composable
    fun PreviewMenus() {
        TimelanceTheme {
            BottomMenu(rememberNavController())
        }
    }

    @Composable
    fun SearchTextField(
        query: String,
        onQueryChange: (String) -> Unit,
        onSearchSubmit: () -> Unit,
        focusRequester: FocusRequester,
        placeholderText: String = "Search..."
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .focusRequester(focusRequester)
                .onKeyEvent {
                    if (it.key == Key.Enter) {
                        onSearchSubmit()
                        true
                    } else {
                        false
                    }
                },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            ),
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchSubmit()
                }
            ),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.padding(horizontal = 0.dp) // Убираем лишние паддинги
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = placeholderText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    innerTextField()
                }
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppHeader(
        title: String,
        drawerState: DrawerState,
        scope: CoroutineScope,
        isGlobalSearchActive: Boolean,
        globalSearchQuery: String,
        onGlobalSearchQueryChange: (String) -> Unit,
        onGlobalSearchToggle: () -> Unit,
        onGlobalSearchSubmit: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        LaunchedEffect(isGlobalSearchActive) {
            if (isGlobalSearchActive) {
                focusRequester.requestFocus()
                keyboardController?.show()
            }
        }

        TopAppBar(
            modifier = modifier,
            colors = TopAppBarDefaults.topAppBarColors( /* ... ваши цвета ... */ ),
            title = {
                // Поле ввода для глобального поиска, если он активен
                AnimatedVisibility(
                    visible = isGlobalSearchActive,
                    enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { it / 2 }, animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { it / 2 }, animationSpec = tween(300))
                ) {
                    SearchTextField( // Тот же SearchTextField, что и раньше
                        query = globalSearchQuery,
                        onQueryChange = onGlobalSearchQueryChange,
                        onSearchSubmit = {
                            onGlobalSearchSubmit(globalSearchQuery)
                            focusManager.clearFocus()
                        },
                        focusRequester = focusRequester,
                        placeholderText = "Search all skills..."
                    )
                }
                // Если поиск не активен, можно показать заголовок текущего экрана (если нужно)
                // или оставить пустым, чтобы хедер был компактнее.
                if (!isGlobalSearchActive) {
                    Text(title) // Или динамический заголовок
                }
            },
            navigationIcon = {
                AnimatedContent(
                    targetState = isGlobalSearchActive,
                    /* ... transitionSpec ... */
                    label = "nav_icon_global_search_animation"
                ) { searchMode ->
                    if (searchMode) {
                        IconButton(onClick = {
                            onGlobalSearchToggle() // Закрывает поиск
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Close Global Search")
                        }
                    } else {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, "Меню")
                        }
                    }
                }
            },
            actions = {
                // Иконка поиска для активации/деактивации
                if (!isGlobalSearchActive) {
                    IconButton(onClick = onGlobalSearchToggle) {
                        Icon(Icons.Filled.Search, contentDescription = "Open Global Search")
                    }
                }
                // Иконка очистки появляется только в режиме поиска и если есть текст
                if (isGlobalSearchActive && globalSearchQuery.isNotEmpty()) {
                    AnimatedVisibility(visible = globalSearchQuery.isNotEmpty()) {
                        IconButton(onClick = { onGlobalSearchQueryChange("") }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear Global Search")
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun Header(
        drawerState: DrawerState,
        scope: CoroutineScope,
        title: String = "TimeLance",
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { scope.launch { drawerState.open() } },
                content = { Icon(Icons.Filled.Menu, "Меню") }
            )

            Text(title)

            IconButton(onClick = { /* Handle search action */ }) {
                Icon(
                    imageVector = Icons.Filled.Search, // Use Material icon for search
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp) // Optional: Set icon size
                )
            }
        }
    }

    @Composable
    fun BottomMenu(navController: NavHostController) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SecondColorLight)
                .padding(3.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MenuButtonItem(
                navController, AppDestinations.ROUTE_HOME_SCREEN,
                "Home Icon", R.drawable.home
            )
            MenuButtonItem(
                navController, AppDestinations.ROUTE_SKILL_HUB_SCREEN,
                "SkillHube Icon", R.drawable.skill_hub
            )
        }
    }

    @Composable
    private fun MenuButtonItem(
        navController: NavHostController, route: String,
        contentDescription: String, icon: Int
    ) {
        ButtonPrimary(
            onClick = { navController.navigate(route) },
            containerColor = PrimaryAccentColorLight,
            contentColor = SecondAccentColorLight,
            contentDescription = contentDescription,
            icon = icon,
        )
    }
}

