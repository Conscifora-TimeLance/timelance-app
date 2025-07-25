package com.nexora.timelance.ui.components.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexora.timelance.R

@Composable
fun ButtonPrimary(
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    contentDescription: String? = null,
    icon: Int? = null,
    textContent: String? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor,
            contentColor
        ),
        enabled = enabled
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