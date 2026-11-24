package com.contoh.gylearn.ui.layar

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contoh.gylearn.ui.tema.WarnaTema
import com.contoh.gylearn.ui.viewmodel.BelajarViewModel

/**
 * Layar ringkasan hasil belajar yang menampilkan jumlah kartu diingat, dilupakan,
 * serta opsi untuk mengulang kartu yang terlupa atau kembali ke Beranda.
 */
@Composable
fun LayarHasilBelajar(
    viewModel: BelajarViewModel,
    padaKembaliKeBeranda: () -> Unit,
    padaUlangiBelajar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val kondisi by viewModel.kondisi.collectAsState()
    val statusGulir = rememberScrollState()

    val totalDipelajari = kondisi.kartuDiingat.size + kondisi.kartuDilupakan.size
    val jumlahDiingat = kondisi.kartuDiingat.size
    val jumlahDilupakan = kondisi.kartuDilupakan.size
    val persentaseSkor = if (totalDipelajari > 0) {
        ((jumlahDiingat.toFloat() / totalDipelajari.toFloat()) * 100).toInt()
    } else 0

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(statusGulir)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Lambang Piala / Medali Emas
            Surface(
                shape = CircleShape,
                color = WarnaTema.EmasLembut.copy(alpha = 0.15f),
                border = BorderStroke(2.dp, WarnaTema.EmasLembut),
                modifier = Modifier.size(96.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.EmojiEvents,
                        contentDescription = null,
                        tint = WarnaTema.EmasLembut,
                        modifier = Modifier.size(52.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Sesi Belajar Selesai!",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = kondisi.dek?.nama ?: "Dek Flashcard",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Kartu Skor Utama
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.5.dp, WarnaTema.EmasBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tingkat Penguasaan",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$persentaseSkor%",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = WarnaTema.EmasLembut,
                            fontSize = 44.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Rincian Kartu: Diingat & Dilupakan
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Kolom Diingat
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = WarnaTema.HijauIngat.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, WarnaTema.HijauIngat.copy(alpha = 0.4f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = null,
                                        tint = WarnaTema.HijauIngat,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Diingat",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = WarnaTema.HijauIngat,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "$jumlahDiingat",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        // Kolom Dilupakan
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = WarnaTema.MerahLupa.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, WarnaTema.MerahLupa.copy(alpha = 0.4f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.Close,
                                        contentDescription = null,
                                        tint = WarnaTema.MerahLupa,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Lupa",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = WarnaTema.MerahLupa,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "$jumlahDilupakan",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Tombol "Ulangi yang Lupa" (hanya aktif jika ada kartu yang dilupakan)
            if (jumlahDilupakan > 0) {
                Button(
                    onClick = {
                        viewModel.ulangiYangLupa()
                        padaUlangiBelajar()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WarnaTema.EmasLembut,
                        contentColor = WarnaTema.BiruNavyTua
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Replay,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ulangi yang Lupa ($jumlahDilupakan Kartu)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Tombol "Ulangi Semua"
            OutlinedButton(
                onClick = {
                    viewModel.ulangiSemua()
                    padaUlangiBelajar()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, WarnaTema.EmasLembut)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Replay,
                    contentDescription = null,
                    tint = WarnaTema.EmasLembut
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ulangi Semua Kartu",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = WarnaTema.EmasLembut
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tombol Kembali ke Beranda
            Button(
                onClick = padaKembaliKeBeranda,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Kembali ke Beranda",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
