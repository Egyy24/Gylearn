package com.contoh.gylearn.data.sumber

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.contoh.gylearn.data.model.Kartu
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object untuk tabel kartu.
 */
@Dao
interface KartuDao {

    @Query("SELECT * FROM tabel_kartu WHERE idDek = :idDek ORDER BY id ASC")
    fun ambilKartuBerdasarkanDek(idDek: Long): Flow<List<Kartu>>

    @Query("SELECT * FROM tabel_kartu ORDER BY id ASC")
    fun ambilSemuaKartu(): Flow<List<Kartu>>

    @Query("SELECT * FROM tabel_kartu WHERE id = :idKartu LIMIT 1")
    fun ambilKartuBerdasarkanId(idKartu: Long): Flow<Kartu?>

    @Query("SELECT * FROM tabel_kartu WHERE id = :idKartu LIMIT 1")
    suspend fun dapatkanKartuSatu(idKartu: Long): Kartu?

    @Query("""
        SELECT * FROM tabel_kartu 
        WHERE idDek = :idDek 
        AND (pertanyaan LIKE '%' || :kataKunci || '%' OR jawaban LIKE '%' || :kataKunci || '%')
        ORDER BY id ASC
    """)
    fun cariKartuDalamDek(idDek: Long, kataKunci: String): Flow<List<Kartu>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun sisipKartu(kartu: Kartu): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun sisipSemuaKartu(daftarKartu: List<Kartu>): List<Long>

    @Update
    suspend fun perbaruiKartu(kartu: Kartu)

    @Delete
    suspend fun hapusKartu(kartu: Kartu)

    @Query("UPDATE tabel_kartu SET tingkatHafalan = :tingkat, tanggalReview = :tanggalReview WHERE id = :idKartu")
    suspend fun perbaruiTingkatHafalan(idKartu: Long, tingkat: Int, tanggalReview: Long)

    @Query("SELECT COUNT(*) FROM tabel_kartu WHERE idDek = :idDek")
    fun hitungKartuPerDek(idDek: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM tabel_kartu WHERE idDek = :idDek AND tingkatHafalan >= 4")
    fun hitungKartuDihafal(idDek: Long): Flow<Int>
}
