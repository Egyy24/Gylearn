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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.contoh.gylearn.data.model.Kartu
import com.contoh.gylearn.ui.komponen.KomponenStatusKosong
import com.contoh.gylearn.ui.tema.WarnaTema
import com.contoh.gylearn.ui.viewmodel.DaftarKartuViewModel

/**
 * Layar untuk melihat, mencari, menambah, mengubah, dan menghapus kartu dalam suatu Dek.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayarDaftarKartu(
    idDek: Long,
    viewModel: DaftarKartuViewModel,
    padaKembali: () -> Unit,
    padaTambahKartu: (idDek: Long) -> Unit,
    padaUbahKartu: (idDek: Long, idKartu: Long) -> Unit,
    padaMulaiBelajar: (idDek: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(idDek) {
        viewModel.inisialisasi(idDek)
    }

    val kondisi by viewModel.kondisi.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = kondisi.dek?.nama ?: "Daftar Kartu",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${kondisi.daftarKartu.size} Kartu",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
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
                    if (kondisi.daftarKartu.isNotEmpty()) {
                        Button(
                            onClick = { padaMulaiBelajar(idDek) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WarnaTema.EmasLembut,
                                contentColor = WarnaTema.BiruNavyTua
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Belajar",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium
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
                onClick = { padaTambahKartu(idDek) },
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
                        text = "Tambah Kartu",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Kolom Pencarian Kartu (Fitur 6)
            OutlinedTextField(
                value = kondisi.kataKunci,
                onValueChange = { viewModel.aturKataKunci(it) },
                placeholder = { Text("Cari pertanyaan atau jawaban...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Cari",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (kondisi.kataKunci.isNotEmpty()) {
                        IconButton(onClick = { viewModel.aturKataKunci("") }) {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = "Hapus Pencarian"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = WarnaTema.EmasLembut,
                    cursorColor = WarnaTema.EmasLembut
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            )

            if (kondisi.daftarKartu.isEmpty() && !kondisi.sedangMemuat) {
                // Status Kosong
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (kondisi.kataKunci.isNotBlank()) {
                        KomponenStatusKosong(
                            ikon = Icons.Outlined.Search,
                            judul = "Tidak Ditemukan",
                            deskripsi = "Tidak ada kartu yang cocok dengan kata kunci \"${kondisi.kataKunci}\"."
                        )
                    } else {
                        KomponenStatusKosong(
                            ikon = Icons.Outlined.Style,
                            judul = "Dek Masih Kosong",
                            deskripsi = "Tambahkan kartu pertama ke dalam dek ini untuk mulai belajar.",
                            teksTombol = "Tambah Kartu Sekarang",
                            padaKlikTombol = { padaTambahKartu(idDek) }
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(kondisi.daftarKartu, key = { it.id }) { kartu ->
                        ItemBarisKartu(
                            kartu = kartu,
                            padaUbah = { padaUbahKartu(idDek, kartu.id) },
                            padaHapus = { viewModel.pilihKartuUntukHapus(kartu) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }
        }
    }

    // Dialog Konfirmasi Hapus Kartu
    kondisi.kartuDipilihUntukHapus?.let { kartu ->
        AlertDialog(
            onDismissRequest = { viewModel.pilihKartuUntukHapus(null) },
            shape = RoundedCornerShape(22.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Hapus Kartu?",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                )
            },
            text = {
                Text(
                    text = "Kartu dengan pertanyaan \"${kartu.pertanyaan}\" akan dihapus secara permanen.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.hapusKartu(kartu) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = Color.White
                    )
                ) {
                    Text("Hapus", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { viewModel.pilihKartuUntukHapus(null) },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Batal")
                }
            }
        )
    }
}

/**
 * Komponen item individual dalam daftar kartu.
 */
@Composable
private fun ItemBarisKartu(
    kartu: Kartu,
    padaUbah: () -> Unit,
    padaHapus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, WarnaTema.EmasBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Baris Atas: Pertanyaan & Bintang Tingkat Hafalan
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = WarnaTema.EmasLembut.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "PERTANYAAN",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = WarnaTema.EmasLembut
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = kartu.pertanyaan,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                // Indikator Bintang
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = if (i <= kartu.tingkatHafalan) {
                                WarnaTema.EmasLembut
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Jawaban
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = "JAWABAN",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = kartu.jawaban,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tombol Aksi (Ubah & Hapus)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = padaUbah,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Ubah Kartu",
                        tint = WarnaTema.EmasLembut,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(
                    onClick = padaHapus,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Hapus Kartu",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
