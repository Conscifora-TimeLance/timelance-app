package com.nexora.timelance.ui.components.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ButtonPrimary(
    navController: NavHostController,
    route: String,
    containerColor: Color,
    contentColor: Color,
    contentDescription: String, icon: Int
) {
    Button(
        onClick = { navController.navigate(route) },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor,
            contentColor
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(25.dp)
        )
    }
}