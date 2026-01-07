package com.example.todoku.usecase

import android.util.Log
import com.example.todoku.entity.text
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class Todousecase {

    private val db = Firebase.firestore

    // Ambil semua todo
    suspend fun getMainActivity2(): List<text> {
        return try {
            val result = db.collection("todo")
                .get()
                .await()

            if (!result.isEmpty) {
                result.documents.map {
                    text(
                        id = it.id,
                        title = it.getString("title") ?: "",
                        description = it.getString("description") ?: ""
                    )
                }
            } else emptyList()

        } catch (e: Exception) {
            Log.e("ERROR_GET_LIST", e.message ?: "Unknown error")
            emptyList()
        }
    }

    // Ambil 1 todo by id
    suspend fun getTodo(id: String): text? {
        return try {
            val data = db.collection("todo")
                .document(id)
                .get()
                .await()

            if (!data.exists()) null else text(
                id = data.id,
                title = data.getString("title") ?: "",
                description = data.getString("description") ?: ""
            )

        } catch (e: Exception) {
            Log.e("ERROR_GET_TODO", e.message ?: "Unknown error")
            null
        }
    }

    // Update todo
    suspend fun updateTodo(todo: text) {
        try {
            val payload = mapOf(
                "title" to todo.title,
                "description" to todo.description
            )

            db.collection("todo")
                .document(todo.id)
                .set(payload)
                .await()

        } catch (e: Exception) {
            throw Exception("Gagal Mengupdate Data : ${e.message}")
        }
    }

    // Delete todo
    suspend fun deleteTodo(id: String) {
        try {
            db.collection("todo")
                .document(id)
                .delete()
                .await()
        } catch (e: Exception) {
            throw Exception("Gagal Menghapus Data : ${e.message}")
        }
    }

    // Create todo
    suspend fun createTodo(todo: text): text {
        return try {
            val payload = mapOf(
                "title" to todo.title,
                "description" to todo.description
            )

            val data = db.collection("todo")
                .add(payload)
                .await()

            todo.copy(id = data.id)

        } catch (e: Exception) {
            throw Exception("Gagal menyimpan data ke Firestore : ${e.message}")
        }
    }
}
