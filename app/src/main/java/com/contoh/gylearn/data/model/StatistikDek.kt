package com.contoh.gylearn.data.model

/**
 * Model pembantu untuk menampilkan ringkasan informasi dek beserta statistiknya.
 */
data class StatistikDek(
    val dek: Dek,
    val jumlahKartu: Int = 0,
    val sudahDihafal: Int = 0,
    val persentaseHafalan: Float = 0f
)
