package com.nexora.timelance.ui.components.button

import android.widget.Space
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nexora.timelance.R

@Composable
fun ButtonPrimary(
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    contentDescription: String? = null,
    icon: Int? = null,
    textContent: String? = null
) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor,
            contentColor
        )
    ) {
        textContent?.let {
            Text(textContent)
            if (icon != null) {
                Spacer(Modifier.padding(2.dp))
            }
        }

        icon?.let {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(25.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview () {
    ButtonPrimary(
        onClick = {},
        containerColor = Color(0xFF134074),
        contentColor = Color.White,
        contentDescription = "Button Primary",
        icon = R.drawable.add,
        textContent = "SAVE"
    )
}