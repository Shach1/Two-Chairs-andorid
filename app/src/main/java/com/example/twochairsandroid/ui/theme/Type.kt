package com.example.twochairsandroid.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.twochairsandroid.R

private val Ntomik = FontFamily(Font(R.font.ntomic_regular))
private val PressStart = FontFamily(Font(R.font.pressstart_regular))

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = PressStart,
        fontWeight = FontWeight.Normal,
        fontSize = 42.sp,
        lineHeight = 48.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = Ntomik,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 28.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = Ntomik,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 38.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Ntomik,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Ntomik,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Ntomik,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    )
)
