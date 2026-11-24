package com.contoh.gylearn

import android.app.Application
import com.contoh.gylearn.data.repositori.RepositoriKartu
import com.contoh.gylearn.data.sumber.BasisData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Kelas Application utama untuk Gylearn.
 * Menginisialisasi basis data dan repositori secara terpusat.
 */
class AplikasiGylearn : Application() {

    lateinit var repositori: RepositoriKartu
        private set

    override fun onCreate() {
        super.onCreate()
        val basisData = BasisData.dapatkanBasisData(this)
        repositori = RepositoriKartu(basisData.dekDao(), basisData.kartuDao())

        // Memastikan data awal terisi saat aplikasi diluncurkan
        CoroutineScope(Dispatchers.IO).launch {
            repositori.pastikanDataAwal()
        }
    }
}
