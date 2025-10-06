package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.TodoAdapter
import com.example.myapplication.databinding.ActivityMain2Binding
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.usecase.Todousecase
import kotlinx.coroutines.launch


class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var todoUseCase: Todousecase
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        todoUseCase = Todousecase()
        //siapkan kebutuhan recylerciew terlebib dahulu
        setupRecyclerView()

        //inisiasi mengambil data dari firestore
        initiazeData()
    }

    private fun initiazeData() {
        lifecycleScope.launch {
            //sembunyikan tampilan recylerview terlebih dahulu dan tampilan ui loading
            binding.container.visibility = View.GONE
            binding.uiLoading.visibility = View.VISIBLE

            try {
                //ambil data dari firebase
                var todolist = todoUseCase.getMainActivity2()

                // jika sudah mendapatkan data dan tidak error tampilkan kembali recyclerview dan sembunyikan ui loading nya
                binding.container.visibility = View.VISIBLE
                binding.uiLoading.visibility = View.GONE

                //update data yang ada ada di adapter
                todoAdapter.updateData(todolist)
            } catch (e: Exception) {
                e.printStackTrace()


            }


        }
    }

    private fun setupRecyclerView() {
        todoAdapter = TodoAdapter(arrayListOf())
        binding.container.apply {
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(this@MainActivity2)
        }
    }
}