package com.example.timetrack.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.timetrack.client.menu.MainActivity
import com.example.timetrack.databinding.ActivityEditUserBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

class EditUser : AppCompatActivity() {
    private lateinit var binding: ActivityEditUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // actionBar
        title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // getting user fields
        val userId = intent.getStringExtra("id")
        val userName = intent.getStringExtra("name")
        val userLastName = intent.getStringExtra("lastName")
        val profession = intent.getStringExtra("profession")

        // setting the fields in the activity
        binding.userName.setText(userName)
        binding.userLastName.setText(userLastName)
        binding.userProfession.setText(profession)

        // updating the user on firebase
        binding.btnEdit.setOnClickListener {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId!!)
                .set(
                    hashMapOf(
                        "name" to binding.userName.text.toString(),
                        "lastName" to binding.userLastName.text.toString(),
                        "profession" to binding.userProfession.text.toString()
                    ),
                    SetOptions.merge()
                ).addOnSuccessListener {
                    Toast
                        .makeText(
                            applicationContext,
                            "Usuario editado",
                            Toast.LENGTH_LONG
                        )
                        .show()
                    finish()
                }.addOnFailureListener {
                    Toast
                        .makeText(
                            applicationContext,
                            "No se pudo editar al usuario",
                            Toast.LENGTH_LONG
                        )
                        .show()
                }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}