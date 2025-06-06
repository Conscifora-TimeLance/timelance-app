package com.nexora.timelance.ui.components.navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
            IconButton(onClick = { scope.launch { drawerState.open() } },
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
            contentColor = SecondAccentColorLight,
            containerColor = PrimaryAccentColorLight,
            contentDescription = contentDescription,
            icon = icon
        )
    }
}

