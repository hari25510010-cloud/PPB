package com.example.todoku

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoku.adapter.TodoAdapter
import com.example.todoku.databinding.ActivityMain2Binding
import com.example.todoku.entity.text
import com.example.todoku.usecase.Todousecase
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

        // Lebih aman: pakai binding.root
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        todoUseCase = Todousecase()

        // Siapkan RecyclerView
        setupRecyclerView()

        // Ambil data dari Firestore
        initializeData()

        // Daftarkan tombol event
        registerEvents()
    }

    fun onCreateTodoPage() {
        val intent = Intent(this, CreateTodoActivity::class.java)
        startActivity(intent)
        finish()
        // hapus kalau mau kembali ke MainActivity2 setelah create
    }

    private fun initializeData() {
        lifecycleScope.launch {
            binding.container.visibility = View.GONE
            binding.uiLoading.visibility = View.VISIBLE

            try {
                val todoList = todoUseCase.getMainActivity2()
                Log.d("INFO_LIST", todoList.toString())

                binding.container.visibility = View.VISIBLE
                binding.uiLoading.visibility = View.GONE

                todoAdapter.updateData(todoList)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ERROR_FIREBASE", e.message.toString())
                binding.uiLoading.visibility = View.GONE
            }
        }
    }

    private fun registerEvents() {

        binding.tomboltambah.setOnClickListener {
            val intent = Intent(this, CreateTodoActivity::class.java)
            startActivity(intent)
            finish()
            onCreateTodoPage()
        }
    }

    private fun setupRecyclerView() {
        // <--- perbaikan: panggil konstruktor
        todoAdapter = TodoAdapter(
            mutableListOf(),
            object : TodoAdapter.TodoItemEvent {
                
                override fun onTodoItemEdit(todo: text) {
                    val intent = Intent(this@MainActivity2, EditTodoActivity::class.java)
                    intent.putExtra("todo_item_id", todo.id)
                    startActivity(intent)
                }

                override fun onTodoItemDelete(todo: text) {
                    val builder = AlertDialog.Builder(this@MainActivity2)
                    builder.setTitle("Konfirmasi Hapus Data")
                    builder.setMessage("Apakah Anda yakin ingin menghapus data ini?")
                    builder.setPositiveButton("Ya") { _, _ ->
                        // Hapus data dari Firestore
                        lifecycleScope.launch {
                            try {
                                todoUseCase.deleteTodo(todo.id)
                                initializeData()
                            } catch (e: Exception) {
                                displayErrorMessage(e.message.toString())
                            }
                        }
                    }
                    builder.setNegativeButton("Tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.show()
                }

            },
        )
        binding.container.apply {
            layoutManager = LinearLayoutManager(this@MainActivity2)
            adapter = todoAdapter
        }
    }

    fun displayErrorMessage(message: String) {
        Toast.makeText(this@MainActivity2, message, Toast.LENGTH_SHORT).show()
    }
}
