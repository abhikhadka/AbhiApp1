package com.example.eduapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font

// Define a fun, rounded font for a kids' educational app
// This uses Google Fonts (if you add them) or default rounded system fonts
val RoundedFont = FontFamily.Default // In a real app, you'd add custom .ttf here

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = RoundedFont,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = RoundedFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = RoundedFont,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = RoundedFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
)
