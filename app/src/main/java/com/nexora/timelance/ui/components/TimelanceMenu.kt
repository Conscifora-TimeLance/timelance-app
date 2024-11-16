package com.nexora.timelance.ui.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TimelanceMenu {

    @Composable
    fun Header(
        drawerState: DrawerState,
        scope: CoroutineScope,
        onSearchClick: () -> Unit = {}
    ) {
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
    }

    @Composable
    fun BottomMenu(navController: NavHostController) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {}) { Text("ГЛ") }
            Button(onClick = { navController.navigate("SkillHub") }) {
                Text("ГР")
            }
            Button(onClick = {}) { Text("НА") }
            Button(onClick = {}) { Text("ОТ") }
        }
    }

}

