package com.example.pendataaninventarissekolah.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * InventarisDao - Tempat menulis Query SQL.
 */
@Dao
interface InventarisDao {

    @Insert
    suspend fun insertBarang(barang: Barang)

    @Update
    suspend fun updateBarang(barang: Barang)

    @Query("SELECT * FROM barang ORDER BY id DESC")
    fun getAllBarang(): LiveData<List<Barang>>

    @Query("SELECT * FROM barang WHERE checkoutDate IS NULL ORDER BY id DESC")
    fun getActiveBarang(): LiveData<List<Barang>>

    @Query("SELECT * FROM barang WHERE checkoutDate IS NOT NULL ORDER BY id DESC")
    fun getCheckedOutBarang(): LiveData<List<Barang>>

    @Delete
    suspend fun deleteBarang(barang: Barang)
}
