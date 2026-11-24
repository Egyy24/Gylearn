package com.contoh.gylearn.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.contoh.gylearn.data.model.Dek
import com.contoh.gylearn.data.model.Kartu
import com.contoh.gylearn.data.repositori.RepositoriKartu
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Representasi kondisi antarmuka pada layar daftar kartu dalam suatu dek.
 */
data class KondisiDaftarKartu(
    val dek: Dek? = null,
    val daftarKartu: List<Kartu> = emptyList(),
    val kataKunci: String = "",
    val sedangMemuat: Boolean = true,
    val kartuDipilihUntukHapus: Kartu? = null
)

/**
 * ViewModel untuk mengelola daftar kartu di dalam suatu dek beserta pencariannya.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DaftarKartuViewModel(
    private val repositori: RepositoriKartu
) : ViewModel() {

    private val _kondisi = MutableStateFlow(KondisiDaftarKartu())
    val kondisi: StateFlow<KondisiDaftarKartu> = _kondisi.asStateFlow()

    private val aliranKataKunci = MutableStateFlow("")
    private var idDekAktif: Long = 0

    fun inisialisasi(idDek: Long) {
        if (idDekAktif == idDek) return
        idDekAktif = idDek

        // Ambil data Dek
        viewModelScope.launch {
            repositori.ambilDekBerdasarkanId(idDek).collect { dataDek ->
                _kondisi.update { it.copy(dek = dataDek) }
            }
        }

        // Ambil dan filter kartu berdasarkan kata kunci
        viewModelScope.launch {
            aliranKataKunci.flatMapLatest { kata ->
                repositori.cariKartu(idDek, kata)
            }.collect { hasilKartu ->
                _kondisi.update {
                    it.copy(
                        daftarKartu = hasilKartu,
                        sedangMemuat = false
                    )
                }
            }
        }
    }

    fun aturKataKunci(kata: String) {
        _kondisi.update { it.copy(kataKunci = kata) }
        aliranKataKunci.value = kata
    }

    fun pilihKartuUntukHapus(kartu: Kartu?) {
        _kondisi.update { it.copy(kartuDipilihUntukHapus = kartu) }
    }

    fun hapusKartu(kartu: Kartu) {
        viewModelScope.launch {
            repositori.hapusKartu(kartu)
            pilihKartuUntukHapus(null)
        }
    }
}
