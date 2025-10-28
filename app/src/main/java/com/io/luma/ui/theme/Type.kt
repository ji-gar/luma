package com.io.luma.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.io.luma.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val verandaRegular= FontFamily(
    Font(R.font.verdana,FontWeight.Normal)
)
val verandaBold= FontFamily(
    Font(R.font.verdanabold,FontWeight.Bold)
)

val manropebold= FontFamily(
    Font(R.font.manropebold,FontWeight.Normal)
)
val manropesemibold= FontFamily(
    Font(R.font.manropesemibold,FontWeight.Normal)
)
val monospaceRegular= FontFamily(
    Font(R.font.manroperegular,FontWeight.Normal)
)
