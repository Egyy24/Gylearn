package com.contoh.gylearn.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.contoh.gylearn.data.model.Dek
import com.contoh.gylearn.data.model.StatistikDek
import com.contoh.gylearn.data.repositori.RepositoriKartu
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Representasi status UI pada layar Beranda.
 */
data class KondisiBeranda(
    val daftarDek: List<StatistikDek> = emptyList(),
    val sedangMemuat: Boolean = true,
    val dialogTambahTerbuka: Boolean = false,
    val dekDipilihUntukUbah: Dek? = null,
    val dekDipilihUntukHapus: Dek? = null
)

/**
 * ViewModel untuk mengelola logika bisnis layar Beranda.
 */
class BerandaViewModel(
    private val repositori: RepositoriKartu
) : ViewModel() {

    private val _kondisi = MutableStateFlow(KondisiBeranda())
    val kondisi: StateFlow<KondisiBeranda> = _kondisi.asStateFlow()

    init {
        muatDaftarDek()
    }

    private fun muatDaftarDek() {
        viewModelScope.launch {
            repositori.ambilSemuaDekDenganStatistik().collect { daftar ->
                _kondisi.update { it.copy(daftarDek = daftar, sedangMemuat = false) }
            }
        }
    }

    fun bukaDialogTambah() {
        _kondisi.update { it.copy(dialogTambahTerbuka = true) }
    }

    fun tutupDialogTambah() {
        _kondisi.update { it.copy(dialogTambahTerbuka = false) }
    }

    fun pilihDekUntukUbah(dek: Dek?) {
        _kondisi.update { it.copy(dekDipilihUntukUbah = dek) }
    }

    fun pilihDekUntukHapus(dek: Dek?) {
        _kondisi.update { it.copy(dekDipilihUntukHapus = dek) }
    }

    fun tambahDek(nama: String, deskripsi: String) {
        viewModelScope.launch {
            repositori.tambahDek(
                Dek(
                    nama = nama,
                    deskripsi = deskripsi
                )
            )
            tutupDialogTambah()
        }
    }

    fun perbaruiDek(idDek: Long, nama: String, deskripsi: String) {
        viewModelScope.launch {
            val dekSaatIni = _kondisi.value.dekDipilihUntukUbah
            if (dekSaatIni != null) {
                repositori.perbaruiDek(
                    dekSaatIni.copy(nama = nama, deskripsi = deskripsi)
                )
            }
            pilihDekUntukUbah(null)
        }
    }

    fun hapusDek(dek: Dek) {
        viewModelScope.launch {
            repositori.hapusDek(dek)
            pilihDekUntukHapus(null)
        }
    }
}
