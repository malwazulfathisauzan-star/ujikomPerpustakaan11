package com.example.pendataaninventarissekolah
import com.example.pendataaninventarissekolah.AddBarangActivity
import com.example.pendataaninventarissekolah.BarangDetailActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pendataaninventarissekolah.R
import com.example.pendataaninventarissekolah.adapter.InventarisAdapter
import com.example.pendataaninventarissekolah.viewmodel.BarangViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

/**
 * MainActivity - Halaman Utama.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: BarangViewModel
    private lateinit var adapter: InventarisAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Siapkan List
        val listItem = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = InventarisAdapter { item ->
            val intent = Intent(this, BarangDetailActivity::class.java)
            intent.putExtra("BARANG", item)
            startActivity(intent)
        }
        listItem.adapter = adapter
        listItem.layoutManager = LinearLayoutManager(this)

        // 2. Siapkan Data
        viewModel = ViewModelProvider(this)[BarangViewModel::class.java]
        viewModel.daftarItem.observe(this) { data ->
            adapter.setData(data)
        }

        // 3. Tombol Tambah
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, AddBarangActivity::class.java))
        }

        // 4. Tab Menu (Filter)
        val tabs = findViewById<TabLayout>(R.id.tabLayout)
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.gantiFilter(BarangViewModel.Filter.TERSEDIA)
                    1 -> viewModel.gantiFilter(BarangViewModel.Filter.KELUAR)
                    2 -> viewModel.gantiFilter(BarangViewModel.Filter.SEMUA)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}
