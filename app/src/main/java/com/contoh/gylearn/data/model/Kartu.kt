package com.contoh.gylearn.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entitas Kartu mewakili satu flashcard yang terkait dengan suatu Dek.
 * Terhapus secara otomatis jika Dek terkait dihapus (CASCADE).
 */
@Entity(
    tableName = "tabel_kartu",
    foreignKeys = [
        ForeignKey(
            entity = Dek::class,
            parentColumns = ["id"],
            childColumns = ["idDek"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idDek"])
    ]
)
data class Kartu(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val idDek: Long,
    val pertanyaan: String,
    val jawaban: String,
    val tingkatHafalan: Int = 0, // Rentang 0 sampai 5
    val tanggalReview: Long = System.currentTimeMillis()
)
