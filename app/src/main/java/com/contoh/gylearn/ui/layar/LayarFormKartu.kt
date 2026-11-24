package com.contoh.gylearn.ui.layar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.contoh.gylearn.data.model.Kartu
import com.contoh.gylearn.data.repositori.RepositoriKartu
import com.contoh.gylearn.ui.tema.WarnaTema
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Layar form untuk menambah kartu baru atau mengubah kartu yang sudah ada.
 * Dilengkapi validasi input yang ketat (pertanyaan dan jawaban tidak boleh kosong).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayarFormKartu(
    idDek: Long,
    idKartu: Long,
    repositori: RepositoriKartu,
    padaKembali: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cakupanKorutin = rememberCoroutineScope()
    val statusGulir = rememberScrollState()

    var pertanyaan by remember { mutableStateOf("") }
    var jawaban by remember { mutableStateOf("") }
    var kartuLama by remember { mutableStateOf<Kartu?>(null) }

    var errorPertanyaan by remember { mutableStateOf(false) }
    var errorJawaban by remember { mutableStateOf(false) }
    var sedangMenyimpan by remember { mutableStateOf(false) }

    val apakahModeUbah = idKartu > 0

    // Muat data jika dalam mode ubah
    LaunchedEffect(idKartu) {
        if (apakahModeUbah) {
            val kartu = repositori.ambilKartuBerdasarkanId(idKartu).first()
            if (kartu != null) {
                kartuLama = kartu
                pertanyaan = kartu.pertanyaan
                jawaban = kartu.jawaban
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (apakahModeUbah) "Ubah Kartu" else "Tambah Kartu Baru",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(statusGulir)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, WarnaTema.EmasBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Sisi Depan (Pertanyaan)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tuliskan konsep, pertanyaan, atau kata yang ingin Anda hafalkan.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = pertanyaan,
                        onValueChange = {
                            pertanyaan = it
                            if (it.isNotBlank()) errorPertanyaan = false
                        },
                        placeholder = { Text("Contoh: Apa arti Polimorfisme?") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                                contentDescription = null,
                                tint = WarnaTema.EmasLembut
                            )
                        },
                        isError = errorPertanyaan,
                        supportingText = {
                            if (errorPertanyaan) {
                                Text(
                                    text = "Pertanyaan wajib diisi",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        minLines = 3,
                        maxLines = 6,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = WarnaTema.EmasLembut,
                            cursorColor = WarnaTema.EmasLembut
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, WarnaTema.EmasBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Sisi Belakang (Jawaban)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tuliskan penjelasan lengkap, definisi, atau jawaban yang benar.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = jawaban,
                        onValueChange = {
                            jawaban = it
                            if (it.isNotBlank()) errorJawaban = false
                        },
                        placeholder = { Text("Contoh: Kemampuan suatu objek memiliki banyak bentuk...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lightbulb,
                                contentDescription = null,
                                tint = WarnaTema.EmasLembut
                            )
                        },
                        isError = errorJawaban,
                        supportingText = {
                            if (errorJawaban) {
                                Text(
                                    text = "Jawaban wajib diisi",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        minLines = 4,
                        maxLines = 8,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = WarnaTema.EmasLembut,
                            cursorColor = WarnaTema.EmasLembut
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Simpan Kartu
            Button(
                onClick = {
                    val validPertanyaan = pertanyaan.isNotBlank()
                    val validJawaban = jawaban.isNotBlank()

                    errorPertanyaan = !validPertanyaan
                    errorJawaban = !validJawaban

                    if (validPertanyaan && validJawaban && !sedangMenyimpan) {
                        sedangMenyimpan = true
                        cakupanKorutin.launch {
                            if (apakahModeUbah && kartuLama != null) {
                                repositori.perbaruiKartu(
                                    kartuLama!!.copy(
                                        pertanyaan = pertanyaan.trim(),
                                        jawaban = jawaban.trim()
                                    )
                                )
                            } else {
                                repositori.tambahKartu(
                                    Kartu(
                                        idDek = idDek,
                                        pertanyaan = pertanyaan.trim(),
                                        jawaban = jawaban.trim()
                                    )
                                )
                            }
                            padaKembali()
                        }
                    }
                },
                enabled = !sedangMenyimpan,
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
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(
                    text = if (apakahModeUbah) "Perbarui Kartu" else "Simpan Kartu",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
