package com.contoh.gylearn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entitas Dek mewakili kumpulan atau kelompok kartu belajar (flashcard).
 */
@Entity(tableName = "tabel_dek")
data class Dek(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nama: String,
    val deskripsi: String,
    val tanggalDibuat: Long = System.currentTimeMillis()
)
