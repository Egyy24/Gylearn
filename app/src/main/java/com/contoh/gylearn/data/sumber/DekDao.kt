package com.contoh.gylearn.data.sumber

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.contoh.gylearn.data.model.Dek
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object untuk tabel dek.
 */
@Dao
interface DekDao {

    @Query("SELECT * FROM tabel_dek ORDER BY tanggalDibuat DESC")
    fun ambilSemuaDek(): Flow<List<Dek>>

    @Query("SELECT * FROM tabel_dek WHERE id = :idDek LIMIT 1")
    fun ambilDekBerdasarkanId(idDek: Long): Flow<Dek?>

    @Query("SELECT * FROM tabel_dek WHERE id = :idDek LIMIT 1")
    suspend fun dapatkanDekSatu(idDek: Long): Dek?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun sisipDek(dek: Dek): Long

    @Update
    suspend fun perbaruiDek(dek: Dek)

    @Delete
    suspend fun hapusDek(dek: Dek)

    @Query("DELETE FROM tabel_dek WHERE id = :idDek")
    suspend fun hapusDekBerdasarkanId(idDek: Long)
}
