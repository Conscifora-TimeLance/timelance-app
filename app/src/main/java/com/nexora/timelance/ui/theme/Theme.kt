package com.nexora.timelance.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nexora.timelance.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColorLight,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun TimelanceTheme(
    // TODO HARD CODE FOR THEME COLOR IS HERE DUDE
//    darkTheme: Boolean = isSystemInDarkTheme(),
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current

            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = appTypography(),
        content = content
    )
}

@Composable
fun PreviewTypography(): Typography {
    return Typography(
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodySmall = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
    )
}

val AlegreyaSC = FontFamily(
    Font(R.font.alegreya_sc_regular),
    Font(R.font.alegreya_sc_bold, FontWeight.Bold),
    Font(R.font.alegreya_sc_italic, FontWeight.Normal)
)

@Composable
fun appTypography() = Typography(
    displayLarge = TextStyle(
        fontFamily = AlegreyaSC,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    titleLarge = TextStyle(
        fontFamily = AlegreyaSC,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = AlegreyaSC,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = AlegreyaSC,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)