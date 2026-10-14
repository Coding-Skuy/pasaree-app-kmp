# AGENTS.md — pasaree-app-kmp

Aplikasi beli mobile (Android + iOS) Pasaree: jelajah katalog, keranjang, checkout, bayar, lacak pesanan, ulasan. Repo ini bagian dari divisi Pasaree (Epic E2 Stabil di Hilir).

## Sumber kebenaran

| Dokumen | Tautan |
|---|---|
| BRD Katalog & Produk (BR-201..BR-206) | [BRD/20-katalog-produk.md](https://github.com/Coding-Skuy/Pasaree-TownHall/blob/main/platform/versions/v1.0.0/BRD/20-katalog-produk.md) |
| BRD Keuangan & Komisi (BR-301..BR-306) | [BRD/30-keuangan-komisi.md](https://github.com/Coding-Skuy/Pasaree-TownHall/blob/main/platform/versions/v1.0.0/BRD/30-keuangan-komisi.md) |
| PRD (pengguna, alur, kriteria) | [PRD/](https://github.com/Coding-Skuy/Pasaree-TownHall/blob/main/platform/versions/v1.0.0/PRD/) |
| FSD (alur, model data, kontrak) | [FSD/](https://github.com/Coding-Skuy/Pasaree-TownHall/blob/main/platform/versions/v1.0.0/FSD/) |
| SRS (fungsional, non-fungsional, acceptance criteria QA) | [SRS/](https://github.com/Coding-Skuy/Pasaree-TownHall/blob/main/platform/versions/v1.0.0/SRS/) |
| Alur beli (living) | [produk/10-alur-beli.md](https://github.com/Coding-Skuy/Pasaree-TownHall/blob/main/produk/10-alur-beli.md) |
| Kontrak API mobile KMP (living) | [produk/20-kontrak-api-KMP-mobile.md](https://github.com/Coding-Skuy/Pasaree-TownHall/blob/main/produk/20-kontrak-api-KMP-mobile.md) |
| Modul KMP bersama (living) | [produk/30-modul-KMP-bersama.md](https://github.com/Coding-Skuy/Pasaree-TownHall/blob/main/produk/30-modul-KMP-bersama.md) |
| Model data katalog (living) | [katalog/10-model-data.md](https://github.com/Coding-Skuy/Pasaree-TownHall/blob/main/katalog/10-model-data.md) |
| Komisi transparan (living) | [keuangan/10-komisi-transparan.md](https://github.com/Coding-Skuy/Pasaree-TownHall/blob/main/keuangan/10-komisi-transparan.md) |
| Konteks lintas divisi | [KONTEKS-LINTAS-DIVISI.md](https://github.com/Coding-Skuy/ChefGenie-TownHall/blob/main/KONTEKS-LINTAS-DIVISI.md) |
| Roadmap master | [townhall-roadmap.md](https://github.com/Coding-Skuy/ChefGenie-TownHall/blob/main/townhall-roadmap.md) |

Aturan: dilarang menduplikasi requirement di repo kode ini. Requirement berubah = PR di `Pasaree-TownHall` terlebih dahulu (dokumen `platform/versions/v1.0.0/` beku; perubahan masuk versi baru). Dokumen BRD/PRD/FRD/FSD/SRS v1.0.0 sebagian masih templat; sumber isi sah ada di folder living TownHall (`produk/`, `keuangan/`, `katalog/`, `lapak/`, `metrik/`) yang dirujuk di atas.

## Tech stack terkunci

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

Perubahan versi Kotlin/Compose/Navigation3 hanya lewat ADR baru. Catatan ketidaksesuaian: TownHall `produk/30-modul-KMP-bersama.md` menyebut Kotlin 2.1.x / CMP 1.7.x, sedangkan repo ini memakai pin 2.2.20 / 1.8.2; pin repo yang berlaku sampai TownHall diselaraskan.

## Aturan kerja AI

1. Baca `AGENTS.md` ini dan semua ADR di `docs/decisions/` sebelum menulis kode.
2. TDD merah-hijau: tulis test dari acceptance criteria SRS dulu; rilis diblokir bila QA gate gagal.
3. Kontrak API/event hanya diubah lewat bump versi + ADR baru.
4. Commit memakai prefiks `docs:`/`feat:`/`fix:` dan wajib memperbarui `CHANGELOG.md` repo.
5. Keputusan arsitektur baru = tambah ADR bernomor di `docs/decisions/`; jangan edit ADR lama (immutable, sama seperti aturan versi TownHall).
6. Dilarang menyentuh DB divisi lain (DB per service; Titeny read-only). DB milik repo ini: `pasaree`; JWT audiens `pasaree`.
7. Aturan uang Pasaree: Komisi 8% (paket-hampers 10%, lapak baru 5% selama 30 hari) dipotong dari payout lapak, BUKAN dari pembeli; biaya layanan Rp2.000 per checkout masuk operasional Pasaree; ongkir 100% ke kas trip Lumbung; escrow via kasir, payout dieksekusi Lumbung T+1 hari kerja; uang integer IDR, komisi `floor()`.
8. Dokumen v1.0.0 di `versions/v1.0.0/docs/` beku dan tidak ditimpa; tambahan arsitektur ada di `docs/ARSITEKTUR-AI-DEV.md`.

## Definisi selesai

Dari [townhall-roadmap.md](https://github.com/Coding-Skuy/ChefGenie-TownHall/blob/main/townhall-roadmap.md) §6:

- Dokumen versi lengkap (BRD/FRD/FSD/PRD/SRS) + `SNAPSHOT-ROADMAP.md` di TownHall.
- QA gate berbasis SRS lulus (TDD merah-hijau); rilis diblokir bila gagal.
- Kontrak API terbit; audit keamanan bersih (JWT audiens `pasaree` + DB per service).
- Bukti metrik lintas-divisi (KONTEKS §7) tercatat.
- CHANGELOG versi diperbarui; dokumen beku tidak diedit.
