# Arsitektur AI-dev pasaree-app-kmp

Dokumen tambahan untuk `versions/v1.0.0/docs/40-TECHSTACK.md` dan `60-BLAST-RADIUS.md` (kedua file itu beku v1.0.0 dan hanya berisi ringkasan; jangan ditimpa). Rujukan versi: [Pasaree-TownHall v1.0.0](https://github.com/Coding-Skuy/Pasaree-TownHall/blob/main/platform/versions/v1.0.0/CHANGELOG.md).

## Matriks stack dan fitur

| Aspek | Terkunci | Catatan |
|---|---|---|
| Bahasa | Kotlin 2.2.20 | pin di `gradle/libs.versions.toml` |
| UI | Compose Multiplatform 1.8.2 | satu-satunya framework UI; dilarang framework lain |
| Navigasi | Navigation3 1.0.0 (`androidx.navigation3:navigation3-ui`) | rute type-safe `@Serializable` |
| Jaringan | Ktor 3.1.3 + kotlinx.serialization | timeout 15 dtk; retry 2x hanya GET idempoten |
| Offline | SQLDelight 2.0.3 + antrean aksi + `SyncWorker` | offline-first; bayar tidak pernah antre offline |
| Platform | Android 9+ dan iOS 16+ | TANPA desktop/JVM (Pasaree bukan Lumbung gudang / Titeny analis) |
| Build | AGP 8.6.3, JDK 17, Xcode 16+ | |

Larangan: framework UI selain Compose Multiplatform; target desktop; menghitung total checkout di klien; format harga manual di luar `formatIdr()`.

| Fitur | Android | iOS | Desktop |
|---|---|---|---|
| Katalog, pencarian, detail produk | ya | ya | tidak ada |
| Keranjang + checkout pratinjau | ya | ya | tidak ada |
| Bayar idempoten (VA/QRIS/e-wallet) | ya | ya | tidak ada |
| Status pesanan, konfirmasi terima, retur non-pangan segar | ya | ya | tidak ada |
| Ulasan | ya | ya | tidak ada |
| Tayang produk / kurasi / admin | tidak (milik web) | tidak (milik web) | tidak ada |
| Cache offline + `SyncWorker` | WorkManager + DataStore | BGTask + NSUserDefaults | tidak ada |

Navigation graph (dari `produk/30-modul-KMP-bersama.md` §4):

```
Beranda -> Pencarian -> DetailProduk({produkId}) -> Keranjang -> Checkout({tripId}) -> Bayar({pesananId}) -> StatusPesanan({pesananId})
Beranda -> PesananSaya -> DetailPesanan({pesananId}) -> Ulasan
```

Deep-link: `chefgenie://pasaree/produk/{id}` dan `https://pasaree.chefgenie.id/p/{id}` membuka DetailProduk.

## Blast radius

Batas context repo (townhall-roadmap §2: komisi 8% transparan, escrow T+1; katalog jujur; fulfillment wajib via trip Lumbung).

Boleh disentuh:

- UI beli, navigasi, `shared` (model/domain/network/storage/sync/util), cache lokal SQLDelight.
- Memanggil `https://api.pasaree.chefgenie.id/v1` (katalog, keranjang, checkout, pesanan, ulasan, trip proksi).

Dilarang:

- Menghitung total/komisi/ongkir di klien (tampilkan hasil pratinjau server).
- Memanggil API Lumbung langsung (hanya lewat proksi backend Pasaree).
- Fungsi tayang produk atau kurasi (milik `pasaree-web`).
- Target desktop/JVM; menyimpan data kartu/rekening.

Kontrak dikonsumsi:

- REST Pasaree v1 (kontrak `produk/20-kontrak-api-KMP-mobile.md`), termasuk `GET /trip` proksi Lumbung (cache 60 detik, flag `basi`).

Kontrak diproduksi:

- Tidak memproduksi event atau API; hanya klien.

Catatan: Pasaree tidak mengonsumsi modul `:shared:pantry-resep` (milik Pawonee). Failure utama: kontrak berubah tanpa update SRS, test tidak mengikuti acceptance criteria, rollback tidak siap. Rollback: revert commit repo ini dan kembali ke kontrak Pasaree-TownHall v1.0.0.
