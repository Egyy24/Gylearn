package com.contoh.gylearn.data.repositori

import com.contoh.gylearn.data.model.Dek
import com.contoh.gylearn.data.model.Kartu
import com.contoh.gylearn.data.model.StatistikDek
import com.contoh.gylearn.data.sumber.BasisData
import com.contoh.gylearn.data.sumber.DekDao
import com.contoh.gylearn.data.sumber.KartuDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

/**
 * Repositori untuk mengelola operasi data Dek dan Kartu.
 */
class RepositoriKartu(
    private val dekDao: DekDao,
    private val kartuDao: KartuDao
) {

    /**
     * Mengambil daftar semua dek yang tersimpan.
     */
    fun ambilSemuaDek(): Flow<List<Dek>> = dekDao.ambilSemuaDek()

    /**
     * Mengambil satu dek berdasarkan ID.
     */
    fun ambilDekBerdasarkanId(idDek: Long): Flow<Dek?> = dekDao.ambilDekBerdasarkanId(idDek)

    /**
     * Menambahkan dek baru ke basis data.
     */
    suspend fun tambahDek(dek: Dek): Long = dekDao.sisipDek(dek)

    /**
     * Memperbarui informasi dek yang ada.
     */
    suspend fun perbaruiDek(dek: Dek) = dekDao.perbaruiDek(dek)

    /**
     * Menghapus dek dari basis data beserta seluruh kartunya.
     */
    suspend fun hapusDek(dek: Dek) = dekDao.hapusDek(dek)

    /**
     * Mengambil daftar kartu yang tergabung dalam suatu dek.
     */
    fun ambilKartuBerdasarkanDek(idDek: Long): Flow<List<Kartu>> =
        kartuDao.ambilKartuBerdasarkanDek(idDek)

    /**
     * Mengambil satu kartu berdasarkan ID.
     */
    fun ambilKartuBerdasarkanId(idKartu: Long): Flow<Kartu?> =
        kartuDao.ambilKartuBerdasarkanId(idKartu)

    /**
     * Mencari kartu dalam dek tertentu berdasarkan kata kunci pertanyaan atau jawaban.
     */
    fun cariKartu(idDek: Long, kataKunci: String): Flow<List<Kartu>> {
        return if (kataKunci.isBlank()) {
            kartuDao.ambilKartuBerdasarkanDek(idDek)
        } else {
            kartuDao.cariKartuDalamDek(idDek, kataKunci.trim())
        }
    }

    /**
     * Menambahkan kartu baru.
     */
    suspend fun tambahKartu(kartu: Kartu): Long = kartuDao.sisipKartu(kartu)

    /**
     * Memperbarui isi kartu.
     */
    suspend fun perbaruiKartu(kartu: Kartu) = kartuDao.perbaruiKartu(kartu)

    /**
     * Menghapus kartu.
     */
    suspend fun hapusKartu(kartu: Kartu) = kartuDao.hapusKartu(kartu)

    /**
     * Memperbarui tingkat hafalan kartu berdasarkan evaluasi "Ingat" atau "Lupa".
     * Tingkat hafalan berada pada rentang 0 sampai 5.
     */
    suspend fun nilaiKartu(kartu: Kartu, apakahIngat: Boolean) {
        val tingkatBaru = if (apakahIngat) {
            (kartu.tingkatHafalan + 1).coerceAtMost(5)
        } else {
            (kartu.tingkatHafalan - 1).coerceAtLeast(0)
        }
        kartuDao.perbaruiTingkatHafalan(
            idKartu = kartu.id,
            tingkat = tingkatBaru,
            tanggalReview = System.currentTimeMillis()
        )
    }

    /**
     * Menggabungkan aliran data Dek dan Kartu untuk menghasilkan statistik lengkap per dek.
     */
    fun ambilSemuaDekDenganStatistik(): Flow<List<StatistikDek>> {
        return combine(
            dekDao.ambilSemuaDek(),
            kartuDao.ambilSemuaKartu()
        ) { daftarDek, semuaKartu ->
            daftarDek.map { dek ->
                val kartuDek = semuaKartu.filter { it.idDek == dek.id }
                val total = kartuDek.size
                // Kartu dianggap sudah dihafal jika tingkat hafalan >= 3
                val dihafal = kartuDek.count { it.tingkatHafalan >= 3 }
                val persentase = if (total > 0) (dihafal.toFloat() / total.toFloat()) * 100f else 0f
                StatistikDek(
                    dek = dek,
                    jumlahKartu = total,
                    sudahDihafal = dihafal,
                    persentaseHafalan = persentase
                )
            }
        }
    }

    /**
     * Memastikan data awal contoh terisi jika basis data masih kosong.
     */
    suspend fun pastikanDataAwal() {
        val dekList = dekDao.ambilSemuaDek().first()
        if (dekList.isEmpty()) {
            BasisData.isiDataAwalContoh(dekDao, kartuDao)
        }
    }
}
