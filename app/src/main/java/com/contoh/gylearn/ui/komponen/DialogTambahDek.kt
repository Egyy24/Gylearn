package com.contoh.gylearn.ui.komponen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.contoh.gylearn.ui.tema.WarnaTema

/**
 * Dialog untuk menambah atau mengubah informasi Dek.
 */
@Composable
fun DialogTambahDek(
    padaTutup: () -> Unit,
    padaSimpan: (nama: String, deskripsi: String) -> Unit,
    namaAwal: String = "",
    deskripsiAwal: String = "",
    apakahModeUbah: Boolean = false
) {
    var namaDek by remember { mutableStateOf(namaAwal) }
    var deskripsiDek by remember { mutableStateOf(deskripsiAwal) }
    var apakahError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = padaTutup,
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        title = {
            Text(
                text = if (apakahModeUbah) "Ubah Dek" else "Tambah Dek Baru",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Beri nama dan deskripsi singkat untuk kelompok kartu belajar Anda.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input Nama Dek
                OutlinedTextField(
                    value = namaDek,
                    onValueChange = {
                        namaDek = it
                        if (it.isNotBlank()) apakahError = false
                    },
                    label = { Text("Nama Dek") },
                    placeholder = { Text("Misal: Kosakata Bahasa Jepang") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Folder,
                            contentDescription = null,
                            tint = WarnaTema.EmasLembut
                        )
                    },
                    isError = apakahError,
                    supportingText = {
                        if (apakahError) {
                            Text(
                                text = "Nama dek tidak boleh kosong",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = WarnaTema.EmasLembut,
                        focusedLabelColor = WarnaTema.EmasLembut,
                        cursorColor = WarnaTema.EmasLembut
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Input Deskripsi Dek
                OutlinedTextField(
                    value = deskripsiDek,
                    onValueChange = { deskripsiDek = it },
                    label = { Text("Deskripsi (Opsional)") },
                    placeholder = { Text("Misal: Persiapan ujian level N5") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Description,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    maxLines = 3,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = WarnaTema.EmasLembut,
                        focusedLabelColor = WarnaTema.EmasLembut,
                        cursorColor = WarnaTema.EmasLembut
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (namaDek.isBlank()) {
                        apakahError = true
                    } else {
                        padaSimpan(namaDek.trim(), deskripsiDek.trim())
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarnaTema.EmasLembut,
                    contentColor = WarnaTema.BiruNavyTua
                )
            ) {
                Text(
                    text = "Simpan",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = padaTutup,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Text(
                    text = "Batal",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}
