package com.example.timetrack.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.timetrack.R
import com.example.timetrack.databinding.ActivityCreateClientBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CreateClientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateClientBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        // ActionBar options
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        title = ""

        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_create_client)
        binding = ActivityCreateClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = Firebase.auth

        binding.btnSignIn.setOnClickListener {
            if (
                binding.etPassword.text.toString()
                == binding.etPassword2.text.toString()
            ) {
                CreateAccount(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            }
        }
    }

    private fun CreateAccount(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.message_account_created), Toast.LENGTH_LONG)
                    .show()
                val i = Intent(this, AuthClientActivity::class.java)
                startActivity(i)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG)
                    .show()
            }
    }
}