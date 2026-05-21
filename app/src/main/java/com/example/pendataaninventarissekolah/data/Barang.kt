package com.example.pendataaninventarissekolah.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "barang")
data class Barang(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nmbarang: String,
    val kategori: String,
    val stok: String,
    val kondisi: String,
    val lokasi: String,
    val imagePath: String,
    val registerDate: Long = System.currentTimeMillis(),
    val checkoutDate: Long? = null
) : Serializable
