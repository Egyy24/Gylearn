package com.contoh.gylearn.data.sumber

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.contoh.gylearn.data.model.Dek
import com.contoh.gylearn.data.model.Kartu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * BasisData utama aplikasi Gylearn menggunakan Room Database.
 */
@Database(
    entities = [Dek::class, Kartu::class],
    version = 1,
    exportSchema = false
)
abstract class BasisData : RoomDatabase() {

    abstract fun dekDao(): DekDao
    abstract fun kartuDao(): KartuDao

    companion object {
        @Volatile
        private var INSTANSI: BasisData? = null

        fun dapatkanBasisData(konteks: Context): BasisData {
            return INSTANSI ?: synchronized(this) {
                val instansi = Room.databaseBuilder(
                    konteks.applicationContext,
                    BasisData::class.java,
                    "basis_data_gylearn.db"
                )
                    .addCallback(PanggilanBalikBasisData())
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANSI = instansi
                instansi
            }
        }

        private class PanggilanBalikBasisData : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Menyisipkan data awal contoh saat database pertama kali dibuat
                INSTANSI?.let { basisData ->
                    CoroutineScope(Dispatchers.IO).launch {
                        isiDataAwalContoh(basisData.dekDao(), basisData.kartuDao())
                    }
                }
            }
        }

        suspend fun isiDataAwalContoh(dekDao: DekDao, kartuDao: KartuDao) {
            val idDekContoh = dekDao.sisipDek(
                Dek(
                    nama = "Istilah Pemrograman Dasar",
                    deskripsi = "Kosakata fundamental bagi pemula untuk memahami konsep rekayasa perangkat lunak."
                )
            )

            val kartuContoh = listOf(
                Kartu(
                    idDek = idDekContoh,
                    pertanyaan = "Apa yang dimaksud dengan Variabel?",
                    jawaban = "Wadah di memori komputer untuk menyimpan data atau nilai yang isinya dapat berubah selama program berjalan.",
                    tingkatHafalan = 2
                ),
                Kartu(
                    idDek = idDekContoh,
                    pertanyaan = "Apa itu Fungsi (Function)?",
                    jawaban = "Sekumpulan instruksi terstruktur yang dirancang untuk menyelesaikan tugas spesifik dan dapat digunakan kembali berkali-kali.",
                    tingkatHafalan = 1
                ),
                Kartu(
                    idDek = idDekContoh,
                    pertanyaan = "Jelaskan konsep Rekursi!",
                    jawaban = "Metode pemecahan masalah di mana sebuah fungsi memanggil dirinya sendiri secara berulang hingga mencapai kondisi henti (base case).",
                    tingkatHafalan = 0
                ),
                Kartu(
                    idDek = idDekContoh,
                    pertanyaan = "Apa pengertian Immutability?",
                    jawaban = "Prinsip di mana keadaan atau isi suatu objek tidak dapat diubah setelah objek tersebut selesai dibuat.",
                    tingkatHafalan = 3
                ),
                Kartu(
                    idDek = idDekContoh,
                    pertanyaan = "Apa arti Polimorfisme dalam OOP?",
                    jawaban = "Kemampuan sebuah antarmuka atau objek untuk mengekspresikan perilaku yang berbeda sesuai konteks kelas turunannya.",
                    tingkatHafalan = 1
                )
            )
            kartuDao.sisipSemuaKartu(kartuContoh)
        }
    }
}
