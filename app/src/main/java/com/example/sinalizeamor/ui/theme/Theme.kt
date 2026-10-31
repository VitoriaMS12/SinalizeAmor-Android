package com.example.sinalizeamor.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = CoresSinalizeAmor.Azul,
    onPrimary = CoresSinalizeAmor.Marrom,
    secondary = CoresSinalizeAmor.Bege,
    onSecondary = CoresSinalizeAmor.Marrom,
    tertiary = CoresSinalizeAmor.BegeClaro,
    background = CoresSinalizeAmor.Marrom,
    onBackground = CoresSinalizeAmor.Fundo,
    surface = CoresSinalizeAmor.Marrom,
    onSurface = CoresSinalizeAmor.Fundo,
    outline = CoresSinalizeAmor.BegeClaro
)

private val LightColorScheme = lightColorScheme(
    primary = CoresSinalizeAmor.Marrom,
    onPrimary = CoresSinalizeAmor.Branco,
    secondary = CoresSinalizeAmor.Azul,
    onSecondary = CoresSinalizeAmor.Marrom,
    tertiary = CoresSinalizeAmor.Bege,
    background = CoresSinalizeAmor.Fundo,
    onBackground = CoresSinalizeAmor.Texto,
    surface = CoresSinalizeAmor.FundoCartao,
    onSurface = CoresSinalizeAmor.Texto,
    outline = CoresSinalizeAmor.Borda,
    surfaceVariant = CoresSinalizeAmor.AzulClaro,
    onSurfaceVariant = CoresSinalizeAmor.Texto
)

@Composable
fun SinalizeAmorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}