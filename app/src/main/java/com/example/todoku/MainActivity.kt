package com.example.todoku

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.todoku.databinding.ActivityMainBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    // 1. buat binding dari main activity
    private lateinit var binding: ActivityMainBinding
    private lateinit var credentialManager: CredentialManager
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 2. Inisiasi Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        // 3. Set content dari Binding
        setContentView(
            binding.root
        )
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        credentialManager = CredentialManager.create(this)
        auth = Firebase.auth
        // 4. Daftarkan event yang diperlukan
        registerEvent()
    }

    fun registerEvent() {
        binding.btnLogin.setOnClickListener {
            // 5. Daftarkan event ketika button di klik
            lifecycleScope.launch {
                val request = prepareRequest()
                loginByGoogle(request)
            }
        }
    }

    fun prepareRequest(): GetCredentialRequest {
        val serverClientId =
            "197548912185-8oe0haci1404bcntqmikvmht0ndp2mdm.apps.googleusercontent.com"
        val googleIdOption = GetGoogleIdOption
            .Builder()
            .setServerClientId(serverClientId)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest
            .Builder()
            .addCredentialOption(googleIdOption)
            .build()
        return request
    }

    suspend fun loginByGoogle(request: GetCredentialRequest) {
        try {
            val result = credentialManager.getCredential(
                context = this@MainActivity,
                request = request
            )
            val credential = result.credential
            val idToken = GoogleIdTokenCredential.createFrom(credential.data)

            firebaseLoginCallback(idToken.idToken)
        } catch (exc: NoCredentialException) {
            Toast.makeText(this, "login gagal:" + exc.message, Toast.LENGTH_SHORT).show()
        } catch (exc: Exception) {
            exc.printStackTrace()
            Toast.makeText(this, "login gagal:" + exc.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun firebaseLoginCallback(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "login berhasil", Toast.LENGTH_LONG).show()
                    toTodoListPage()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "login gagal", Toast.LENGTH_LONG).show()
                }
            }

    }

    fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    private fun toTodoListPage() {
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            toTodoListPage()
        }
    }
}
