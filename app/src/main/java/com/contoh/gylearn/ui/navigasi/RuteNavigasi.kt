package com.contoh.gylearn.ui.navigasi

/**
 * Definisi rute navigasi dalam aplikasi Gylearn.
 */
sealed class RuteNavigasi(val rute: String) {
    data object Percikan : RuteNavigasi("percikan")
    data object Beranda : RuteNavigasi("beranda")

    data object DaftarKartu : RuteNavigasi("daftar_kartu/{idDek}") {
        fun buatRute(idDek: Long) = "daftar_kartu/$idDek"
    }

    data object FormKartu : RuteNavigasi("form_kartu/{idDek}?idKartu={idKartu}") {
        fun buatRute(idDek: Long, idKartu: Long = 0L) = "form_kartu/$idDek?idKartu=$idKartu"
    }

    data object Belajar : RuteNavigasi("belajar/{idDek}") {
        fun buatRute(idDek: Long) = "belajar/$idDek"
    }

    data object HasilBelajar : RuteNavigasi("hasil_belajar/{idDek}") {
        fun buatRute(idDek: Long) = "hasil_belajar/$idDek"
    }
}
