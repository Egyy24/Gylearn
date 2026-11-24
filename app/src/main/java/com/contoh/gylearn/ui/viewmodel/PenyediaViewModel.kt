package com.contoh.gylearn.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.contoh.gylearn.data.repositori.RepositoriKartu

/**
 * Pabrik instansiasi ViewModel untuk menyuntikkan RepositoriKartu.
 */
class PabrikViewModel(
    private val repositori: RepositoriKartu
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BerandaViewModel::class.java) -> {
                BerandaViewModel(repositori) as T
            }
            modelClass.isAssignableFrom(DaftarKartuViewModel::class.java) -> {
                DaftarKartuViewModel(repositori) as T
            }
            modelClass.isAssignableFrom(BelajarViewModel::class.java) -> {
                BelajarViewModel(repositori) as T
            }
            else -> throw IllegalArgumentException("Kelas ViewModel tidak dikenal: ${modelClass.name}")
        }
    }
}
