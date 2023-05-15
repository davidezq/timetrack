package com.example.timetrack.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.timetrack.client.menu.MainActivity
import com.example.timetrack.databinding.ActivityAuthClientBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.timetrack.admin.menu.MainActivity as AdminMainActivity

class AuthClientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthClientBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var firebaseFirestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = Firebase.auth
        firebaseFirestore = Firebase.firestore

        binding.btnLogIn.setOnClickListener {
            val errors = checkForm(
                binding.etEmail,
                binding.etPassword
            )
            if (errors != 0) return@setOnClickListener
            LogIn(
                it,
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

    }

    private fun checkForm(vararg editTexts: EditText): Int {
        var errors = 0
        for (editText in editTexts) {
            if (editText.inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
                Log.d("inputTye", "Entro en el if")
                if (Patterns.EMAIL_ADDRESS.matcher(editText.text.toString()).matches()) {
                    editText.error = "Email wrong"
                    errors++
                }
            }
            if (editText.text.toString().isEmpty()) {
                editText.error = "se requiere ${editText.hint.toString()}"
                errors++
                continue
            }

        }
        return errors
    }

    private fun LogIn(view: View, email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                user = firebaseAuth.currentUser!!
                firebaseFirestore
                    .collection("admins")
                    .get()
                    .addOnSuccessListener { documents ->

                        val admins = documents.map { it.id  }
                        if (admins.contains(user?.uid)) {
                            val i = Intent(this, AdminMainActivity::class.java)
                            i.putExtra("email",user?.email)
                            Toast.makeText(this.baseContext, "Welcome ${user.email}", Toast.LENGTH_LONG).show()
                            startActivity(i)
                        } else {
                            val i = Intent(this, MainActivity::class.java)
                            Toast.makeText(this.baseContext, "Welcome ${user.email}", Toast.LENGTH_LONG).show()
                            startActivity(i)
                        }
                        binding.etEmail.text.clear()
                        binding.etPassword.text.clear()
                    }

            }
            .addOnFailureListener {
                Toast.makeText(view.context, "${it.message}", Toast.LENGTH_LONG).show()
            }

    }
    override fun onBackPressed() {
    }
}