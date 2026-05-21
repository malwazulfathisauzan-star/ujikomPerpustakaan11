package com.example.pendataaninventarissekolah.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.pendataaninventarissekolah.data.AppDatabase
import com.example.pendataaninventarissekolah.data.Barang
import com.example.pendataaninventarissekolah.repository.BarangRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * BarangViewModel - Controller (Pengatur Data).
 */
class BarangViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BarangRepository

    // Filter Kategori: Masih Tersedia, Keluar, Semua
    enum class Filter { TERSEDIA, KELUAR, SEMUA }
    private val filterTersedia = MutableLiveData(Filter.TERSEDIA)

    // Data yang tampil di layar
    val daftarItem: LiveData<List<Barang>>

    init {
        val dao = AppDatabase.getDatabase(application).barangDao()
        repository = BarangRepository(dao)

        // Switch data berdasarkan filter tab
        daftarItem = filterTersedia.switchMap { filter ->
            when (filter) {
                Filter.TERSEDIA -> repository.activeBarang
                Filter.KELUAR -> repository.checkedOutBarang
                Filter.SEMUA -> repository.allBarang
            }
        }
    }

    fun gantiFilter(baru: Filter) {
        filterTersedia.value = baru
    }

    // INSERT (Tambah Barang)
    fun tambahItem(item: Barang) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(item)
    }

    // UPDATE (Barang Keluar) - Cukup catat waktu keluarnya saja
    fun prosesKeluar(item: Barang) = viewModelScope.launch(Dispatchers.IO) {
        val update = item.copy(checkoutDate = System.currentTimeMillis())
        repository.update(update)
    }

    fun hapusItem(item: Barang) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(item)
    }
}
