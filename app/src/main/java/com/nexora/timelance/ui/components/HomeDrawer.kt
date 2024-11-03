package com.nexora.timelance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nexora.timelance.ui.theme.SecondColorLight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeDrawer {

    @Composable
    fun DrawerContent(
        drawerState: DrawerState,
        onMenuItemClick: (String) -> Unit // Callback при нажатии на элемент меню
    ) {
        val scope = rememberCoroutineScope()

        // ModalNavigationDrawer с элементами меню

        if (drawerState.isOpen) {
            // Показываем ModalNavigationDrawer только если он открыт
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SecondColorLight)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center // Align content to center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally // Center items in the column
                        ) {
                            Text(text = "Меню", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(8.dp))

                            // Call the method to create menu items
                            MenuItem("Элемент 1", onMenuItemClick, drawerState)
                            MenuItem("Элемент 2", onMenuItemClick, drawerState)
                            MenuItem("Элемент 3", onMenuItemClick, drawerState)
                        }
                    }
                }
            ) {
                MainContent(scope, drawerState)
            }
        } else {
            MainContent(scope, drawerState)
        }
    }

    // Method to create a clickable menu item
    @Composable
    fun MenuItem(text: String, onMenuItemClick: (String) -> Unit, drawerState: DrawerState) {
        val scope = rememberCoroutineScope()

        Text(
            text = text,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable {
                    onMenuItemClick(text)
                    scope.launch { drawerState.close() }
                }
        )
    }

    @Composable
    private fun MainContent(
        scope: CoroutineScope,
        drawerState: DrawerState
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(Icons.Filled.Menu, contentDescription = "Открыть меню")
            }

            IconButton(onClick = { /* Handle search action */ }) {
                Icon(
                    imageVector = Icons.Filled.Search, // Use Material icon for search
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp) // Optional: Set icon size
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewDrawerContent() {
        // Mocking the DrawerState
        val mockDrawerState = remember { DrawerState(DrawerValue.Open) }

        // Mocking the menu item click callback
        val mockOnMenuItemClick: (String) -> Unit = { menuItem ->
            // Handle menu item click (this will just print in the preview)
            println("Clicked on: $menuItem")
        }

        // Preview of DrawerContent
        DrawerContent(
            drawerState = mockDrawerState,
            onMenuItemClick = mockOnMenuItemClick
        )
    }

}