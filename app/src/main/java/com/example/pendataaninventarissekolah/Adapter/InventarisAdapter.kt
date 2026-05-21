package com.example.pendataaninventarissekolah.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pendataaninventarissekolah.R
import com.example.pendataaninventarissekolah.data.Barang
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * InventarisAdapter - Mesin Perulangan (Foreach).
 */
class InventarisAdapter(private val klikItem: (Barang) -> Unit) :
    RecyclerView.Adapter<InventarisAdapter.BarangViewHolder>() {

    private var daftarBarang = emptyList<Barang>()

    inner class BarangViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nmitem = view.findViewById<TextView>(R.id.tvName)
        val kategori = view.findViewById<TextView>(R.id.tvKategori)
        val kondisi = view.findViewById<TextView>(R.id.tvKondisi)
        val waktu = view.findViewById<TextView>(R.id.tvDate)
        val foto = view.findViewById<ImageView>(R.id.ivGambar)

        fun isiData(barang: Barang) {
            nmitem.text = barang.nmbarang
            kategori.text = barang.kategori
            kondisi.text = barang.kondisi

            // Format Waktu pendaftaran
            val formatJam = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("id", "ID"))
            waktu.text = formatJam.format(Date(barang.registerDate))

            Glide.with(itemView.context)
                .load(File(barang.imagePath))
                .placeholder(R.drawable.outline_add_photo_alternate_24)
                .into(foto)

            itemView.setOnClickListener { klikItem(barang) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.barang_item, parent, false)
        return BarangViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        holder.isiData(daftarBarang[position])
    }

    override fun getItemCount() = daftarBarang.size

    fun setData(baru: List<Barang>) {
        daftarBarang = baru
        notifyDataSetChanged()
    }
}
