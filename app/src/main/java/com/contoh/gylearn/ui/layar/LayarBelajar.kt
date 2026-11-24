package com.contoh.gylearn.ui.layar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.RotateRight
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.contoh.gylearn.ui.komponen.KomponenKartuBalik
import com.contoh.gylearn.ui.komponen.KomponenStatusKosong
import com.contoh.gylearn.ui.tema.WarnaTema
import com.contoh.gylearn.ui.viewmodel.BelajarViewModel

/**
 * Layar Mode Belajar dengan animasi kartu balik 3D, tombol Ingat & Lupa,
 * progres beranimasi, dan opsi acak urutan.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayarBelajar(
    idDek: Long,
    viewModel: BelajarViewModel,
    padaKembali: () -> Unit,
    padaSelesaiBelajar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val kondisi by viewModel.kondisi.collectAsState()

    LaunchedEffect(idDek) {
        viewModel.muatSesiBelajar(idDek)
    }

    // Navigasi otomatis ke layar hasil saat semua kartu selesai dipelajari
    LaunchedEffect(kondisi.selesai) {
        if (kondisi.selesai && kondisi.daftarKartu.isNotEmpty()) {
            padaSelesaiBelajar()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = kondisi.dek?.nama ?: "Belajar Kartu",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = padaKembali) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Kembali",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    // Tombol Acak Urutan Kartu
                    FilledIconButton(
                        onClick = { viewModel.gantiModeAcak() },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = if (kondisi.apakahAcak) WarnaTema.EmasLembut else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (kondisi.apakahAcak) WarnaTema.BiruNavyTua else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Shuffle,
                            contentDescription = "Acak Urutan Kartu"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        if (kondisi.daftarKartu.isEmpty() && !kondisi.sedangMemuat) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                KomponenStatusKosong(
                    ikon = Icons.Outlined.Check,
                    judul = "Tidak Ada Kartu untuk Dipelajari",
                    deskripsi = "Tambahkan kartu ke dalam dek ini terlebih dahulu untuk memulai sesi belajar.",
                    teksTombol = "Kembali",
                    padaKlikTombol = padaKembali
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Bar Progres Belajar
                val progresTeranimasi by animateFloatAsState(
                    targetValue = kondisi.progresFloat,
                    animationSpec = tween(durationMillis = 400),
                    label = "AnimasiProgresBelajar"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kartu ${kondisi.nomorKartuTampil} dari ${kondisi.totalKartu}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = WarnaTema.EmasLembut.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${(kondisi.progresFloat * 100).toInt()}% Selesai",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WarnaTema.EmasLembut
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { progresTeranimasi },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = WarnaTema.EmasLembut,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Kartu Balik 3D Interaktif
                kondisi.kartuSaatIni?.let { kartu ->
                    KomponenKartuBalik(
                        pertanyaan = kartu.pertanyaan,
                        jawaban = kartu.jawaban,
                        apakahDibalik = kondisi.apakahDibalik,
                        padaKlikBalik = { viewModel.balikKartu() },
                        tingkatHafalan = kartu.tingkatHafalan,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                } ?: Box(modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.height(24.dp))

                // Panel Tombol Evaluasi: Lupa - Balik - Ingat
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tombol Lupa
                    Button(
                        onClick = { viewModel.jawabLupa() },
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WarnaTema.MerahLupa,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Lupa",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Tombol Cepat Balik Kartu di Tengah
                    OutlinedIconButton(
                        onClick = { viewModel.balikKartu() },
                        shape = CircleShape,
                        border = BorderStroke(1.5.dp, WarnaTema.EmasLembut),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.RotateRight,
                            contentDescription = "Balik Kartu",
                            tint = WarnaTema.EmasLembut,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Tombol Ingat
                    Button(
                        onClick = { viewModel.jawabIngat() },
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WarnaTema.HijauIngat,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ingat",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
