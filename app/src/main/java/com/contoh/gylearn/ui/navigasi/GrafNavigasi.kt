package com.contoh.gylearn.ui.navigasi

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.contoh.gylearn.data.repositori.RepositoriKartu
import com.contoh.gylearn.ui.layar.LayarBelajar
import com.contoh.gylearn.ui.layar.LayarBeranda
import com.contoh.gylearn.ui.layar.LayarDaftarKartu
import com.contoh.gylearn.ui.layar.LayarFormKartu
import com.contoh.gylearn.ui.layar.LayarHasilBelajar
import com.contoh.gylearn.ui.layar.LayarPercikan
import com.contoh.gylearn.ui.viewmodel.BelajarViewModel
import com.contoh.gylearn.ui.viewmodel.BerandaViewModel
import com.contoh.gylearn.ui.viewmodel.DaftarKartuViewModel
import com.contoh.gylearn.ui.viewmodel.PabrikViewModel

/**
 * Graf navigasi utama aplikasi Gylearn dengan transisi animasi halus antar layar (slide + fade).
 */
@Composable
fun GrafNavigasi(
    kontrolerNavigasi: NavHostController,
    repositori: RepositoriKartu,
    modifier: Modifier = Modifier
) {
    val pabrik = PabrikViewModel(repositori)

    NavHost(
        navController = kontrolerNavigasi,
        startDestination = RuteNavigasi.Percikan.rute,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(350)
            ) + fadeIn(animationSpec = tween(350))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(350)
            ) + fadeOut(animationSpec = tween(350))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(350)
            ) + fadeIn(animationSpec = tween(350))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(350)
            ) + fadeOut(animationSpec = tween(350))
        }
    ) {
        // Layar Splash Percikan
        composable(RuteNavigasi.Percikan.rute) {
            LayarPercikan(
                padaSelesai = {
                    kontrolerNavigasi.navigate(RuteNavigasi.Beranda.rute) {
                        popUpTo(RuteNavigasi.Percikan.rute) { inclusive = true }
                    }
                }
            )
        }

        // Layar Beranda
        composable(RuteNavigasi.Beranda.rute) {
            val berandaViewModel: BerandaViewModel = viewModel(factory = pabrik)
            LayarBeranda(
                viewModel = berandaViewModel,
                padaPilihDek = { idDek ->
                    kontrolerNavigasi.navigate(RuteNavigasi.DaftarKartu.buatRute(idDek))
                },
                padaMulaiBelajar = { idDek ->
                    kontrolerNavigasi.navigate(RuteNavigasi.Belajar.buatRute(idDek))
                }
            )
        }

        // Layar Daftar Kartu
        composable(
            route = RuteNavigasi.DaftarKartu.rute,
            arguments = listOf(
                navArgument("idDek") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val idDek = backStackEntry.arguments?.getLong("idDek") ?: 0L
            val daftarKartuViewModel: DaftarKartuViewModel = viewModel(
                key = "daftar_kartu_$idDek",
                factory = pabrik
            )
            LayarDaftarKartu(
                idDek = idDek,
                viewModel = daftarKartuViewModel,
                padaKembali = { kontrolerNavigasi.popBackStack() },
                padaTambahKartu = { id ->
                    kontrolerNavigasi.navigate(RuteNavigasi.FormKartu.buatRute(id))
                },
                padaUbahKartu = { id, idKartu ->
                    kontrolerNavigasi.navigate(RuteNavigasi.FormKartu.buatRute(id, idKartu))
                },
                padaMulaiBelajar = { id ->
                    kontrolerNavigasi.navigate(RuteNavigasi.Belajar.buatRute(id))
                }
            )
        }

        // Layar Form Kartu (Tambah / Ubah)
        composable(
            route = RuteNavigasi.FormKartu.rute,
            arguments = listOf(
                navArgument("idDek") { type = NavType.LongType },
                navArgument("idKartu") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val idDek = backStackEntry.arguments?.getLong("idDek") ?: 0L
            val idKartu = backStackEntry.arguments?.getLong("idKartu") ?: 0L
            LayarFormKartu(
                idDek = idDek,
                idKartu = idKartu,
                repositori = repositori,
                padaKembali = { kontrolerNavigasi.popBackStack() }
            )
        }

        // Layar Belajar
        composable(
            route = RuteNavigasi.Belajar.rute,
            arguments = listOf(
                navArgument("idDek") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val idDek = backStackEntry.arguments?.getLong("idDek") ?: 0L
            val belajarViewModel: BelajarViewModel = viewModel(
                key = "belajar_$idDek",
                factory = pabrik
            )
            LayarBelajar(
                idDek = idDek,
                viewModel = belajarViewModel,
                padaKembali = { kontrolerNavigasi.popBackStack() },
                padaSelesaiBelajar = {
                    kontrolerNavigasi.navigate(RuteNavigasi.HasilBelajar.buatRute(idDek)) {
                        popUpTo(RuteNavigasi.Belajar.buatRute(idDek)) { inclusive = true }
                    }
                }
            )
        }

        // Layar Hasil Belajar
        composable(
            route = RuteNavigasi.HasilBelajar.rute,
            arguments = listOf(
                navArgument("idDek") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val idDek = backStackEntry.arguments?.getLong("idDek") ?: 0L
            val belajarViewModel: BelajarViewModel = viewModel(
                key = "belajar_$idDek",
                factory = pabrik
            )
            LayarHasilBelajar(
                viewModel = belajarViewModel,
                padaKembaliKeBeranda = {
                    kontrolerNavigasi.navigate(RuteNavigasi.Beranda.rute) {
                        popUpTo(RuteNavigasi.Beranda.rute) { inclusive = true }
                    }
                },
                padaUlangiBelajar = {
                    kontrolerNavigasi.navigate(RuteNavigasi.Belajar.buatRute(idDek)) {
                        popUpTo(RuteNavigasi.HasilBelajar.buatRute(idDek)) { inclusive = true }
                    }
                }
            )
        }
    }
}
