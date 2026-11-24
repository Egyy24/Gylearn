package com.contoh.gylearn.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.contoh.gylearn.data.model.Dek
import com.contoh.gylearn.data.model.Kartu
import com.contoh.gylearn.data.repositori.RepositoriKartu
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Representasi status UI pada sesi Belajar Flashcard.
 */
data class KondisiBelajar(
    val dek: Dek? = null,
    val daftarKartu: List<Kartu> = emptyList(),
    val indeksSaatIni: Int = 0,
    val apakahDibalik: Boolean = false,
    val kartuDiingat: List<Kartu> = emptyList(),
    val kartuDilupakan: List<Kartu> = emptyList(),
    val apakahAcak: Boolean = false,
    val selesai: Boolean = false,
    val sedangMemuat: Boolean = true
) {
    val totalKartu: Int get() = daftarKartu.size
    val kartuSaatIni: Kartu? get() = daftarKartu.getOrNull(indeksSaatIni)
    val nomorKartuTampil: Int get() = if (totalKartu > 0) indeksSaatIni + 1 else 0
    val progresFloat: Float get() = if (totalKartu > 0) indeksSaatIni.toFloat() / totalKartu.toFloat() else 0f
}

/**
 * ViewModel untuk mengelola alur sesi belajar, animasi flip, penilaian kartu, dan hasil belajar.
 */
class BelajarViewModel(
    private val repositori: RepositoriKartu
) : ViewModel() {

    private val _kondisi = MutableStateFlow(KondisiBelajar())
    val kondisi: StateFlow<KondisiBelajar> = _kondisi.asStateFlow()

    private var daftarKartuAsli: List<Kartu> = emptyList()

    fun muatSesiBelajar(idDek: Long, hanyaYangLupa: Boolean = false) {
        viewModelScope.launch {
            _kondisi.update { it.copy(sedangMemuat = true) }

            val dataDek = repositori.ambilDekBerdasarkanId(idDek).first()

            val kumpulanKartu = if (hanyaYangLupa && _kondisi.value.kartuDilupakan.isNotEmpty()) {
                _kondisi.value.kartuDilupakan
            } else {
                repositori.ambilKartuBerdasarkanDek(idDek).first()
            }

            daftarKartuAsli = kumpulanKartu
            val daftarFinal = if (_kondisi.value.apakahAcak) kumpulanKartu.shuffled() else kumpulanKartu

            _kondisi.update {
                it.copy(
                    dek = dataDek,
                    daftarKartu = daftarFinal,
                    indeksSaatIni = 0,
                    apakahDibalik = false,
                    kartuDiingat = emptyList(),
                    kartuDilupakan = emptyList(),
                    selesai = false,
                    sedangMemuat = false
                )
            }
        }
    }

    fun balikKartu() {
        _kondisi.update { it.copy(apakahDibalik = !it.apakahDibalik) }
    }

    fun jawabIngat() {
        val kartu = _kondisi.value.kartuSaatIni ?: return
        viewModelScope.launch {
            repositori.nilaiKartu(kartu, apakahIngat = true)
        }

        val diingatBaru = _kondisi.value.kartuDiingat + kartu
        lanjutKartuBerikutnya(diingat = diingatBaru, dilupakan = _kondisi.value.kartuDilupakan)
    }

    fun jawabLupa() {
        val kartu = _kondisi.value.kartuSaatIni ?: return
        viewModelScope.launch {
            repositori.nilaiKartu(kartu, apakahIngat = false)
        }

        val dilupakanBaru = _kondisi.value.kartuDilupakan + kartu
        lanjutKartuBerikutnya(diingat = _kondisi.value.kartuDiingat, dilupakan = dilupakanBaru)
    }

    private fun lanjutKartuBerikutnya(diingat: List<Kartu>, dilupakan: List<Kartu>) {
        val indeksBerikutnya = _kondisi.value.indeksSaatIni + 1
        val apakahSelesaiSemua = indeksBerikutnya >= _kondisi.value.daftarKartu.size

        _kondisi.update {
            it.copy(
                indeksSaatIni = indeksBerikutnya,
                apakahDibalik = false,
                kartuDiingat = diingat,
                kartuDilupakan = dilupakan,
                selesai = apakahSelesaiSemua
            )
        }
    }

    fun gantiModeAcak() {
        val modeAcakBaru = !_kondisi.value.apakahAcak
        val sisaKartu = if (modeAcakBaru) {
            _kondisi.value.daftarKartu.shuffled()
        } else {
            daftarKartuAsli
        }

        _kondisi.update {
            it.copy(
                apakahAcak = modeAcakBaru,
                daftarKartu = sisaKartu,
                indeksSaatIni = 0,
                apakahDibalik = false
            )
        }
    }

    fun ulangiSemua() {
        val daftarBaru = if (_kondisi.value.apakahAcak) daftarKartuAsli.shuffled() else daftarKartuAsli
        _kondisi.update {
            it.copy(
                daftarKartu = daftarBaru,
                indeksSaatIni = 0,
                apakahDibalik = false,
                kartuDiingat = emptyList(),
                kartuDilupakan = emptyList(),
                selesai = false
            )
        }
    }

    fun ulangiYangLupa() {
        val kartuLupa = _kondisi.value.kartuDilupakan
        if (kartuLupa.isEmpty()) return

        daftarKartuAsli = kartuLupa
        val daftarBaru = if (_kondisi.value.apakahAcak) kartuLupa.shuffled() else kartuLupa
        _kondisi.update {
            it.copy(
                daftarKartu = daftarBaru,
                indeksSaatIni = 0,
                apakahDibalik = false,
                kartuDiingat = emptyList(),
                kartuDilupakan = emptyList(),
                selesai = false
            )
        }
    }
}
