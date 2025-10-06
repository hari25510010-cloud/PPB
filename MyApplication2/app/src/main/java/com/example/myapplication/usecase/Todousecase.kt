package com.example.myapplication.usecase

import android.util.Log
import com.example.myapplication.entity.text
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class Todousecase {
    val db = Firebase.firestore

    suspend fun getMainActivity2(): List<text> {
        try {
            val data = db.collection("App")
                .get()
                .await()

            if (!data.isEmpty) {
                return data.documents.map {
                    text(
                        id = it.id,
                        title = it.get("title") as String,
                        description = it.get("Description") as String
                    )
                }
            }
        } catch (e: Exception) {
            Log.d("tesr", "error : " + e.message)
            e.printStackTrace()
        }

        return arrayListOf()
    }
}