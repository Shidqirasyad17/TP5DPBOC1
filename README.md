## JANJI
Saya Shidqi Rasyad Firjatulah dengan NIM 2408156 mengerjakan TP5 pada mata kuliah DPBO untuk keberkahannya saya menyatakan bahwa saya tidak melakukan kecurangan sebagaimana yang dispesifikasikan.


---

## Desain Program
### `Class DataBase`
kelas ini berfungsi untuk menyambungkan dengan database MySql.

Method :
- selectQuery -> menjalankan query SELECT dan mengembalikan ResultSet
- insertupdatedeleteQuery -> menjalankan query INSERT, UPDATE, dan DELETE
### `Class Product`
kelas ini memiliki beberapa atribut :
- `id` -> ID produk
- `nama` -> nama produk
- `harga` -> harga produk
- `kategori` -> kategori produk
- `rating` -> rating untuk produk (1-5)
  
Method :
- **Constructor**
- **Getter & Setter**

### `Class ProductMenu`
Kelas ini mengatur GUI serta logika CRUD untuk data produk.

Fungsi : 
 - Menampilkan data produk dari database pada tabel
 - Menambahkan produk baru ke database
 - mengubah data produk yang sudah ada
 - menghapus produk dari database
 - rating produk menggunakan slider

---

## Alur
1. Program dijalankan
2. Tabel menampilkan data dari database 'Produk'.
3. user dapat melakukan aksi :
   - tambah data:
     - isi form -> klik tombol Add -> data masuk ke database
   - Edit data:
     - klik salah satu baris di tabel -> data muncul di form -> ubah data pada form -> klik            update
   - Hapus data:
     - Klik baris -> klik Delete -> konfirmasi penghapusan.
   - Cancel :
     - Klik cancel -> form dikosongkan.
4. Slider Rating:
   - Saat digeser, rating menampilkan bintang sesuai nilai slider.
---

# Dokumentasi

![dokumentasitp5](https://github.com/user-attachments/assets/27de6679-e0bb-403a-ba59-6fec3f0a8c75)





