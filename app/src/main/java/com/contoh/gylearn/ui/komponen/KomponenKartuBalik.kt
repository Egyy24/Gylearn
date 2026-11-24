package com.contoh.gylearn.ui.komponen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.RotateRight
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contoh.gylearn.ui.tema.WarnaTema

/**
 * Komponen kartu dengan animasi flip 3D saat diketuk.
 */
@Composable
fun KomponenKartuBalik(
    pertanyaan: String,
    jawaban: String,
    apakahDibalik: Boolean,
    padaKlikBalik: () -> Unit,
    tingkatHafalan: Int,
    modifier: Modifier = Modifier
) {
    val kepadatanLayar = LocalDensity.current.density
    // Animasi putaran sudut Y
    val rotasiY by animateFloatAsState(
        targetValue = if (apakahDibalik) 180f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "RotasiKartuY"
    )

    Card(
        modifier = modifier
            .graphicsLayer {
                this.rotationY = rotasiY
                this.cameraDistance = 16f * kepadatanLayar
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                padaKlikBalik()
            },
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.5.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    WarnaTema.EmasLembut,
                    WarnaTema.EmasLembut.copy(alpha = 0.2f),
                    WarnaTema.EmasLembut.copy(alpha = 0.6f)
                )
            )
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        if (rotasiY <= 90f) {
            // Sisi Depan (Pertanyaan)
            KontenSisiKartu(
                judulLabel = "PERTANYAAN",
                ikonLabel = Icons.Outlined.AutoAwesome,
                isiTeks = pertanyaan,
                petunjukBawah = "Ketuk untuk melihat jawaban",
                warnaAksen = WarnaTema.EmasLembut,
                tingkatHafalan = tingkatHafalan
            )
        } else {
            // Sisi Belakang (Jawaban) - dibalik 180 derajat agar teks tidak terbalik
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        this.rotationY = 180f
                    }
            ) {
                KontenSisiKartu(
                    judulLabel = "JAWABAN",
                    ikonLabel = Icons.Outlined.Lightbulb,
                    isiTeks = jawaban,
                    petunjukBawah = "Ketuk untuk membalik kartu",
                    warnaAksen = WarnaTema.EmasTerang,
                    tingkatHafalan = tingkatHafalan
                )
            }
        }
    }
}

@Composable
private fun KontenSisiKartu(
    judulLabel: String,
    ikonLabel: androidx.compose.ui.graphics.vector.ImageVector,
    isiTeks: String,
    petunjukBawah: String,
    warnaAksen: Color,
    tingkatHafalan: Int,
    modifier: Modifier = Modifier
) {
    val statusGulir = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header Kartu
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = warnaAksen.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, warnaAksen.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ikonLabel,
                        contentDescription = null,
                        tint = warnaAksen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = judulLabel,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            color = warnaAksen
                        )
                    )
                }
            }

            // Indikator Bintang Tingkat Hafalan (0 - 5)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 1..5) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = if (i <= tingkatHafalan) {
                            WarnaTema.EmasLembut
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f)
                        }
                    )
                }
            }
        }

        // Teks Utama Kartu
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(statusGulir)
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = isiTeks,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 32.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Petunjuk di bagian bawah
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.RotateRight,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = petunjukBawah,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}
