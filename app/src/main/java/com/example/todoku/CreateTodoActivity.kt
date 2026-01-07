package com.example.todoku

import android.R.attr.description
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.todoku.databinding.ActivityCreateTodoBinding
import com.example.todoku.entity.text
import com.example.todoku.usecase.Todousecase
import kotlinx.coroutines.launch

class CreateTodoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTodoBinding
    private lateinit var todousecase: Todousecase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        todousecase = Todousecase()
        registerEvents()
    }

    fun registerEvents() {
        binding.tomboltambah.setOnClickListener {
            saveDataToFireStore()
        }
    }

    fun saveDataToFireStore() {
        val title = binding.title.text.toString()
        val description = binding.description.text.toString()


        if (title == "" || description == "") {
            Toast.makeText(this, "Judul dan deskripsi tidak boleh kosong", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val todo = text(
            id = "",
            title = title,
            description = description,

            )
        lifecycleScope.launch {
            try {
                todousecase.createTodo(todo)
                Toast.makeText(
                    this@CreateTodoActivity,
                    "sukses menambahkan data",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    this@CreateTodoActivity,
                    "gagal menambahkan data",
                    Toast.LENGTH_SHORT
                ).show()
            }

            Toast.makeText(this@CreateTodoActivity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT)
                .show()

            toTodoListActivity()
        }
    }

    fun toTodoListActivity() {
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
        finish()
    }
}