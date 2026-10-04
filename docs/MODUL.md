# Modul KMP Bersama Pasaree

Rujukan: [Pasaree-TownHall](https://github.com/Coding-Skuy/Pasaree-TownHall) `produk/30-modul-KMP-bersama.md`.

- `shared`: titik masuk umum aplikasi beli.
- `domain`: model Lapak, Produk, Varian, Keranjang, Pesanan. ID ULID string, uang integer IDR.
- `network`: klien REST katalog, keranjang, bayar, trip proksi. Sertakan `idempotency-key` untuk bayar.
- `storage`: SQLDelight untuk katalog dan keranjang. Bayar tidak disimpan offline.
- `sync`: SyncWorker antrean idempoten, konflik server-menang.

Aturan anti-duplikasi: logika beli hanya di mobile ini. Web hanya untuk lapak dan admin. Lihat matriks di `platform/10-matriks-KMP-web.md` pada TownHall.
