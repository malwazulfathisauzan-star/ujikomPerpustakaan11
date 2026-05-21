package com.example.pendataaninventarissekolah.repository

import androidx.lifecycle.LiveData
import com.example.pendataaninventarissekolah.data.Barang
import com.example.pendataaninventarissekolah.data.InventarisDao

/**
 * BarangRepository - Jembatan data.
 */
class BarangRepository(private val inventarisDao: InventarisDao) {

    val allBarang: LiveData<List<Barang>> = inventarisDao.getAllBarang()
    val activeBarang: LiveData<List<Barang>> = inventarisDao.getActiveBarang()
    val checkedOutBarang: LiveData<List<Barang>> = inventarisDao.getCheckedOutBarang()

    suspend fun insert(barang: Barang) {
        inventarisDao.insertBarang(barang)
    }

    suspend fun update(barang: Barang) {
        inventarisDao.updateBarang(barang)
    }

    suspend fun delete(barang: Barang) {
        inventarisDao.deleteBarang(barang)
    }
}
