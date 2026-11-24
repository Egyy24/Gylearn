package com.contoh.gylearn.ui.layar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contoh.gylearn.ui.tema.WarnaTema
import kotlinx.coroutines.delay

/**
 * Layar pembuka (splash screen) singkat yang menampilkan logo dan nama aplikasi "Gylearn".
 */
@Composable
fun LayarPercikan(
    padaSelesai: () -> Unit,
    modifier: Modifier = Modifier
) {
    val skalaAnimasi = remember { Animatable(0.7f) }
    val opasitasAnimasi = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        skalaAnimasi.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 700)
        )
        opasitasAnimasi.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600)
        )
        delay(600) // Total durasi sekitar 1.3 detik
        padaSelesai()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarnaTema.BiruNavyTua),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(skalaAnimasi.value)
                .alpha(opasitasAnimasi.value)
        ) {
            // Lambang / Ikon Mewah
            Surface(
                shape = CircleShape,
                color = WarnaTema.BiruNavyKartu,
                border = BorderStroke(2.dp, WarnaTema.EmasLembut),
                modifier = Modifier.size(110.dp),
                shadowElevation = 12.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.AutoStories,
                        contentDescription = "Logo Gylearn",
                        tint = WarnaTema.EmasLembut,
                        modifier = Modifier.size(54.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Nama Aplikasi
            Text(
                text = "Gylearn",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = WarnaTema.EmasLembut
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Slogan
            Text(
                text = "Kuasai Pengetahuan dengan Kartu Memori",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = WarnaTema.KremLembut.copy(alpha = 0.75f),
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}
