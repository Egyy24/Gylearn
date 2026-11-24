package com.contoh.gylearn.ui.tema

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SkemaWarnaGelap = darkColorScheme(
    primary = WarnaTema.EmasLembut,
    onPrimary = WarnaTema.BiruNavyTua,
    primaryContainer = WarnaTema.BiruNavyElevasi,
    onPrimaryContainer = WarnaTema.EmasTerang,
    secondary = WarnaTema.EmasTerang,
    onSecondary = WarnaTema.BiruNavyTua,
    secondaryContainer = WarnaTema.BiruNavyElevasi,
    onSecondaryContainer = WarnaTema.KremLembut,
    background = WarnaTema.BiruNavyTua,
    onBackground = WarnaTema.KremLembut,
    surface = WarnaTema.BiruNavyKartu,
    onSurface = WarnaTema.KremLembut,
    surfaceVariant = WarnaTema.BiruNavyElevasi,
    onSurfaceVariant = WarnaTema.AbuHangat,
    outline = WarnaTema.EmasBorder,
    outlineVariant = WarnaTema.BiruNavyBorder,
    error = WarnaTema.MerahLupa,
    onError = Color.White
)

private val SkemaWarnaTerang = lightColorScheme(
    primary = WarnaTema.EmasTua,
    onPrimary = Color.White,
    primaryContainer = WarnaTema.KremElevasi,
    onPrimaryContainer = WarnaTema.BiruNavyTua,
    secondary = WarnaTema.BiruNavyTua,
    onSecondary = Color.White,
    secondaryContainer = WarnaTema.KremElevasi,
    onSecondaryContainer = WarnaTema.BiruNavyTua,
    background = WarnaTema.KremLatar,
    onBackground = WarnaTema.TeksGelap,
    surface = WarnaTema.KremKartu,
    onSurface = WarnaTema.TeksGelap,
    surfaceVariant = WarnaTema.KremElevasi,
    onSurfaceVariant = WarnaTema.AbuLembut,
    outline = WarnaTema.EmasBorder,
    outlineVariant = Color(0xFFE2D9CC),
    error = WarnaTema.MerahLupa,
    onError = Color.White
)

/**
 * Tema utama Gylearn dengan estetika mewah di mode terang dan gelap.
 */
@Composable
fun TemaGylearn(
    gelap: Boolean = isSystemInDarkTheme(),
    konten: @Composable () -> Unit
) {
    val skemaWarna = if (gelap) SkemaWarnaGelap else SkemaWarnaTerang
    val tampilan = LocalView.current

    if (!tampilan.isInEditMode) {
        SideEffect {
            val jendela = (tampilan.context as Activity).window
            jendela.statusBarColor = skemaWarna.background.toArgb()
            jendela.navigationBarColor = skemaWarna.background.toArgb()
            val kontroler = WindowCompat.getInsetsController(jendela, tampilan)
            kontroler.isAppearanceLightStatusBars = !gelap
            kontroler.isAppearanceLightNavigationBars = !gelap
        }
    }

    MaterialTheme(
        colorScheme = skemaWarna,
        typography = TipografiTema,
        shapes = BentukTema,
        content = konten
    )
}
