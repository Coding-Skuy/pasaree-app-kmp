# pasaree-app-kmp

Aplikasi beli-mobile Marketplace Pasaree. Divisi Pasaree (Marketplace), org Coding-Skuy. Template Opsi A.

Rujukan utama: [Pasaree-TownHall](https://github.com/Coding-Skuy/Pasaree-TownHall) — baca `lapak/00-piagam-kurasi.md`, `katalog/10-model-data.md`, `produk/10-alur-beli.md`, `produk/20-kontrak-api-KMP-mobile.md`, `platform/40-mobile-KMP.md`, `produk/30-modul-KMP-bersama.md`.

## Peran

- Jelajah katalog, keranjang, checkout, bayar, lacak pesanan, ulasan.
- Tidak mengurus armada, gudang, jadwal trip, kas/payout — seluruhnya milik Lumbung dan diakses via proksi backend.
- Verifikasi higiene produsen pangan mengikuti jalur dapur bersama Pawonee/Pedaree.

## Stack Terkunci

- Kotlin 2.2.20
- Compose Multiplatform 1.8.2
- Navigation3 1.0.0 (`androidx.navigation3:navigation3-ui`)
- Target Android 9+ dan iOS 16+. Tanpa modul desktop dan tanpa target JVM-desktop.
- Kontrak bersama: ID ULID string, waktu ISO-8601 UTC, uang integer IDR, paginasi cursor, `idempotency-key` untuk bayar.

## Struktur

```
composeApp/src/commonMain/kotlin/com/pasaree/app/
  BeliApp.kt        Navigasi beli: Katalog, Keranjang, Bayar, Lacak
gradle/libs.versions.toml   Pin versi Kotlin, Compose, Navigation3
docs/MODUL.md       Pembagian shared/domain/network/storage/sync
```

## Mulai Cepat

1. Pasang JDK 17 dan Android SDK.
2. Buka proyek ini di Android Studio.
3. Jalankan konfigurasi `composeApp` untuk Android.
4. Untuk iOS, buka `iosApp` di Xcode 16+ dan jalankan skema `iosApp`.
5. Backend bawaan menunjuk ke `pasaree-backend-service`. Atur basis URL di `local.properties` dengan kunci `pasaree.apiBaseUrl`.
