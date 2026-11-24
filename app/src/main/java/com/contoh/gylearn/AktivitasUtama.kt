package com.contoh.gylearn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.contoh.gylearn.ui.navigasi.GrafNavigasi
import com.contoh.gylearn.ui.tema.TemaGylearn

/**
 * Aktivitas utama aplikasi Gylearn.
 */
class AktivitasUtama : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repositori = (application as AplikasiGylearn).repositori

        setContent {
            TemaGylearn {
                val kontrolerNavigasi = rememberNavController()
                GrafNavigasi(
                    kontrolerNavigasi = kontrolerNavigasi,
                    repositori = repositori
                )
            }
        }
    }
}
