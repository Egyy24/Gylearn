package com.contoh.gylearn.ui.layar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.contoh.gylearn.ui.komponen.DialogTambahDek
import com.contoh.gylearn.ui.komponen.KomponenItemDek
import com.contoh.gylearn.ui.komponen.KomponenStatusKosong
import com.contoh.gylearn.ui.tema.WarnaTema
import com.contoh.gylearn.ui.viewmodel.BerandaViewModel

/**
 * Layar utama Beranda yang menampilkan kumpulan dek flashcard dan ringkasan progres belajar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayarBeranda(
    viewModel: BerandaViewModel,
    padaPilihDek: (idDek: Long) -> Unit,
    padaMulaiBelajar: (idDek: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val kondisi by viewModel.kondisi.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = WarnaTema.EmasLembut.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, WarnaTema.EmasBorder),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.AutoStories,
                                    contentDescription = null,
                                    tint = WarnaTema.EmasLembut,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Gylearn",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.bukaDialogTambah() },
                shape = RoundedCornerShape(18.dp),
                containerColor = WarnaTema.EmasLembut,
                contentColor = WarnaTema.BiruNavyTua,
                elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(6.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null
                    )
                },
                text = {
                    Text(
                        text = "Tambah Dek",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { paddingValues ->
        if (kondisi.daftarDek.isEmpty() && !kondisi.sedangMemuat) {
            // Status Kosong Jika Belum Ada Dek
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                KomponenStatusKosong(
                    ikon = Icons.Outlined.Layers,
                    judul = "Belum Ada Dek",
                    deskripsi = "Buat dek pertama Anda untuk mulai mengelompokkan kartu belajar.",
                    teksTombol = "Tambah Dek Pertama",
                    padaKlikTombol = { viewModel.bukaDialogTambah() }
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Kartu Ringkasan Statistik Beranda
                item {
                    val totalKartu = kondisi.daftarDek.sumOf { it.jumlahKartu }
                    val totalDihafal = kondisi.daftarDek.sumOf { it.sudahDihafal }
                    val rataRataProgres = if (totalKartu > 0) {
                        (totalDihafal.toFloat() / totalKartu.toFloat()) * 100f
                    } else 0f

                    KartuRingkasanStatistik(
                        totalDek = kondisi.daftarDek.size,
                        totalKartu = totalKartu,
                        persentaseHafalan = rataRataProgres.toInt()
                    )
                }

                // Header Daftar Dek
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Koleksi Dek Saya",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        Text(
                            text = "${kondisi.daftarDek.size} Dek",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                // Daftar Item Dek
                items(kondisi.daftarDek, key = { it.dek.id }) { itemStatistik ->
                    KomponenItemDek(
                        statistik = itemStatistik,
                        padaKlikDek = { padaPilihDek(itemStatistik.dek.id) },
                        padaMulaiBelajar = { padaMulaiBelajar(itemStatistik.dek.id) },
                        padaUbahDek = { viewModel.pilihDekUntukUbah(itemStatistik.dek) },
                        padaHapusDek = { viewModel.pilihDekUntukHapus(itemStatistik.dek) }
                    )
                }

                // Spasi di bawah untuk FAB
                item {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        }
    }

    // Dialog Tambah Dek Baru
    if (kondisi.dialogTambahTerbuka) {
        DialogTambahDek(
            padaTutup = { viewModel.tutupDialogTambah() },
            padaSimpan = { nama, deskripsi ->
                viewModel.tambahDek(nama, deskripsi)
            }
        )
    }

    // Dialog Ubah Dek
    kondisi.dekDipilihUntukUbah?.let { dek ->
        DialogTambahDek(
            namaAwal = dek.nama,
            deskripsiAwal = dek.deskripsi,
            apakahModeUbah = true,
            padaTutup = { viewModel.pilihDekUntukUbah(null) },
            padaSimpan = { nama, deskripsi ->
                viewModel.perbaruiDek(dek.id, nama, deskripsi)
            }
        )
    }

    // Dialog Konfirmasi Hapus Dek
    kondisi.dekDipilihUntukHapus?.let { dek ->
        AlertDialog(
            onDismissRequest = { viewModel.pilihDekUntukHapus(null) },
            shape = RoundedCornerShape(22.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Hapus Dek?",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                )
            },
            text = {
                Text(
                    text = "Dek \"${dek.nama}\" beserta seluruh kartu di dalamnya akan dihapus secara permanen. Tindakan ini tidak dapat dibatalkan.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.hapusDek(dek) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    )
                ) {
                    Text("Hapus", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { viewModel.pilihDekUntukHapus(null) },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Batal")
                }
            }
        )
    }
}

/**
 * Komponen kartu ringkasan di bagian atas Beranda.
 */
@Composable
private fun KartuRingkasanStatistik(
    totalDek: Int,
    totalKartu: Int,
    persentaseHafalan: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(1.dp, WarnaTema.EmasBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Ringkasan Belajar",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ItemStatistikKecil(
                    ikon = Icons.Outlined.Layers,
                    nilai = totalDek.toString(),
                    label = "Total Dek"
                )
                ItemStatistikKecil(
                    ikon = Icons.AutoMirrored.Outlined.LibraryBooks,
                    nilai = totalKartu.toString(),
                    label = "Total Kartu"
                )
                ItemStatistikKecil(
                    ikon = Icons.Outlined.School,
                    nilai = "$persentaseHafalan%",
                    label = "Dikuasai"
                )
            }
        }
    }
}

@Composable
private fun ItemStatistikKecil(
    ikon: ImageVector,
    nilai: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = WarnaTema.EmasLembut.copy(alpha = 0.15f),
            modifier = Modifier.size(42.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = ikon,
                    contentDescription = null,
                    tint = WarnaTema.EmasLembut,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = nilai,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
