package com.example.todoku

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.todoku.databinding.ActivityEditTodoBinding
import com.example.todoku.entity.text
import com.example.todoku.usecase.Todousecase
import kotlinx.coroutines.launch

class EditTodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTodoBinding
    private lateinit var todoItemId: String
    private lateinit var todoUseCase: Todousecase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        todoItemId = intent.getStringExtra("todo_item_id") ?: ""
        todoUseCase = Todousecase()
    }

    override fun onStart() {
        super.onStart()
        loadData()
        registerEvent()
    }

    private fun registerEvent() {
        binding.tomboledit.setOnClickListener {
            lifecycleScope.launch {

                val title = binding.title.text.toString()
                val description = binding.description.text.toString()

                val payload = text(
                    id = todoItemId,
                    title = title,
                    description = description
                )

                try {
                    todoUseCase.updateTodo(payload)
                    displayMessage("Berhasil memperbarui task")
                    back()
                } catch (e: Exception) {
                    displayMessage("Gagal memperbarui: ${e.message}")
                }
            }
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            val data = todoUseCase.getTodo(todoItemId)

            if (data == null) {
                displayMessage("Data task tidak tersedia di server")
                back()
                return@launch
            }

            binding.title.setText(data.title)
            binding.description.setText(data.description)
        }
    }

    private fun back() {
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
        finish()
    }

    private fun displayMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
