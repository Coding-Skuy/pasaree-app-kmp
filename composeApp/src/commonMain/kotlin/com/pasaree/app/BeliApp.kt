package com.pasaree.app

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay

sealed interface RuteBeli {
    data object Katalog : RuteBeli
    data object Keranjang : RuteBeli
    data class Bayar(val pesananId: String) : RuteBeli
    data class Lacak(val pesananId: String) : RuteBeli
}

@Composable
fun BeliApp() {
    val tumpukan = androidx.navigation3.runtime.rememberNavBackStack(RuteBeli.Katalog)
    NavDisplay(
        backStack = tumpukan,
        onBack = { tumpukan.removeLastOrNull() },
        entryProvider = { rute ->
            when (rute) {
                is RuteBeli.Katalog -> NavEntry(rute) { LayarKatalog(keKeranjang = { tumpukan.add(RuteBeli.Keranjang) }) }
                is RuteBeli.Keranjang -> NavEntry(rute) { LayarKeranjang(keBayar = { id -> tumpukan.add(RuteBeli.Bayar(id)) }) }
                is RuteBeli.Bayar -> NavEntry(rute) { LayarBayar(rute.pesananId) }
                is RuteBeli.Lacak -> NavEntry(rute) { LayarLacak(rute.pesananId) }
            }
        }
    )
}

@Composable
private fun LayarKatalog(keKeranjang: () -> Unit) {
    Text("Katalog Pasaree — jelajah lapak dan produk")
}

@Composable
private fun LayarKeranjang(keBayar: (String) -> Unit) {
    Text("Keranjang — checkout multi-lapak dengan idempotency-key")
}

@Composable
private fun LayarBayar(pesananId: String) {
    Text("Bayar pesanan $pesananId — hanya daring, tidak pernah offline")
}

@Composable
private fun LayarLacak(pesananId: String) {
    Text("Lacak pesanan $pesananId — status proksi trip Lumbung")
}
