package com.example.pendataaninventarissekolah

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
 * BarangDetailActivity - Halaman Detail.
 */
class BarangDetailActivity : AppCompatActivity() {

    private lateinit var item: Barang
    private lateinit var viewModel: BarangViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barang_detail)

        item = intent.getSerializableExtra("BARANG") as Barang
        viewModel = ViewModelProvider(this)[BarangViewModel::class.java]

        findViewById<TextView>(R.id.tvDetailName).text = item.nmbarang
        findViewById<TextView>(R.id.tvDetailKategori).text = "Kategori: ${item.kategori}"
        findViewById<TextView>(R.id.tvDetailStok).text = "Stok: ${item.stok}"
        findViewById<TextView>(R.id.tvDetailKondisi).text = "Kondisi: ${item.kondisi}"
        findViewById<TextView>(R.id.tvDetailLokasi).text = "Lokasi: ${item.lokasi}"

        val formatJam = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        findViewById<TextView>(R.id.tvDetailDate).text = "Di Tambahkan Pada: ${formatJam.format(Date(item.registerDate))}"

        val infoPulang = findViewById<TextView>(R.id.tvCheckoutInfo)
        val tombolPulang = findViewById<Button>(R.id.btnCheckout)

        if (item.checkoutDate != null) {
            infoPulang.visibility = View.VISIBLE
            infoPulang.text = "Keluar pada: ${formatJam.format(Date(item.checkoutDate!!))}"
            tombolPulang.visibility = View.GONE
        } else {
            infoPulang.visibility = View.GONE
            tombolPulang.visibility = View.VISIBLE
        }

        Glide.with(this).load(File(item.imagePath)).error(R.drawable.outline_add_photo_alternate_24).into(findViewById(R.id.ivDetailPhoto))

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }

        tombolPulang.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Tandai bahwa barang ini sudah keluar?")
                .setPositiveButton("Ya") { _, _ ->
                    viewModel.prosesKeluar(item)
                    finish()
                }
                .setNegativeButton("Tidak", null)
                .show()
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Data")
                .setMessage("Apakah kamu yakin ingin menghapus barang ini?")
                .setPositiveButton("Ya, Hapus") { _, _ ->
                    viewModel.hapusItem(item) // Panggil fungsi hapus di ViewModel
                    finish() // Tutup halaman detail dan balik ke list
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }
}
