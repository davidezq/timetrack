package com.example.timetrack.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.timetrack.R
import com.example.timetrack.databinding.ActivityEditUserBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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
                            getString(R.string.user_edited),
                            Toast.LENGTH_LONG
                        )
                        .show()
                    finish()
                }.addOnFailureListener {
                    Toast
                        .makeText(
                            applicationContext,
                            getString(R.string.could_not_edit_user),
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