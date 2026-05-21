package com.example.pendataaninventarissekolah

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.pendataaninventarissekolah.R
import com.example.pendataaninventarissekolah.data.Barang
import com.example.pendataaninventarissekolah.viewmodel.BarangViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * AddBarangActivity - Halaman Tambah Barang.
 */
class AddBarangActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etKategori: EditText
    private lateinit var etStok: EditText
    private lateinit var etKondisi: EditText
    private lateinit var etLokasi: EditText
    private lateinit var ivFoto: ImageView
    private lateinit var vm: BarangViewModel
    private var pathFoto: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_barang)

        // Ambil ID dari XML
        etNama = findViewById(R.id.etName)
        etKategori = findViewById(R.id.etKategori)
        etStok = findViewById(R.id.etStok)
        etKondisi = findViewById(R.id.etKondisi)
        etLokasi = findViewById(R.id.etLokasi)
        ivFoto = findViewById(R.id.ivPhoto)
        vm = ViewModelProvider(this)[BarangViewModel::class.java]

        // Tombol Ambil Foto
        findViewById<Button>(R.id.btnAddPhoto).setOnClickListener {
            val opsi = arrayOf("Kamera", "Galeri")
            AlertDialog.Builder(this).setItems(opsi) { _, i ->
                if (i == 0) mintaIzinKamera() else bukaGaleri()
            }.show()
        }

        // Tombol Simpan
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val nama = etNama.text.toString()
            val kategori = etKategori.text.toString()
            val stok = etStok.text.toString()
            val kondisi = etKondisi.text.toString()
            val lokasi = etLokasi.text.toString()


            if (nama.isNotEmpty()) {
                val item = Barang(nmbarang = nama, kategori = kategori, stok = stok, kondisi = kondisi, lokasi = lokasi, imagePath = pathFoto)
                vm.tambahItem(item)
                finish()
            } else {
                Toast.makeText(this, "Nama Barang harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mintaIzinKamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            bukaKamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            bukaKamera()
        }
    }

    private fun bukaKamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = buatFile()
        val uri = FileProvider.getUriForFile(this, "com.example.pendataaninventarissekolah.fileprovider", file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, 1)
    }

    private fun bukaGaleri() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 2)
    }

    private fun buatFile(): File {
        val nama = "FOTO_" + SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
        val folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(nama, ".jpg", folder).apply { pathFoto = absolutePath }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                val file = buatFile()
                contentResolver.openInputStream(data?.data!!)?.use { it.copyTo(file.outputStream()) }
            }
            Glide.with(this).load(pathFoto).into(ivFoto)
        }
    }
}
