package com.nexora.timelance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.nexora.timelance.ui.theme.PrimaryColorLight
import com.nexora.timelance.ui.theme.TextColorLight
import com.nexora.timelance.ui.theme.TextColorLightSecond
import kotlinx.coroutines.launch

class HomeDrawer {

    @Composable
    fun DrawerContent(
        drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
        onMenuItemClick: (String) -> Unit
    ) {
        ModalDrawerSheet(
            drawerContainerColor = MaterialTheme.colorScheme.background
        ) {
            val items = listOf("Home", "Contact", "About")
            val selectedItem = remember { mutableStateOf(items[0]) }
            val scope = rememberCoroutineScope()


            items.forEach { item ->
                NavigationDrawerItem(
                    label= { Text(item, fontSize = 22.sp) },
                    selected = selectedItem.value==item,
                    onClick = {
                        scope.launch { drawerState.close() }
                        selectedItem.value = item
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.Transparent,
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = TextColorLight,
                        unselectedTextColor = TextColorLightSecond
                    )
                )
            }
        }

//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.background),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(text = "Меню", style = MaterialTheme.typography.bodyLarge)
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Example items
//                MenuItem("Элемент 1", onMenuItemClick, drawerState)
//                MenuItem("Элемент 2", onMenuItemClick, drawerState)
//                MenuItem("Элемент 3", onMenuItemClick, drawerState)
//            }
//        }
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