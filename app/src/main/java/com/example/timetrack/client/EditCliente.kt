package com.example.timetrack.client

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.timetrack.R
import com.example.timetrack.client.menu.MainActivity
import com.example.timetrack.databinding.ActivityEditClienteBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class EditCliente : AppCompatActivity() {
    private lateinit var binding: ActivityEditClienteBinding
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //toolBar options
        title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // getting activity fields
        val activityId = intent.getStringExtra("id")
        val activityName = intent.getStringExtra("name")
        val activityDuration = intent.getStringExtra("duration")
        val activityDoneAt = intent.getStringExtra("done_at")
        val activityDescription = intent.getStringExtra("description")

        // setting the fields in the activity
        binding.activityName.setText(activityName)
        binding.activityDuration.setText(activityDuration)
        binding.activityDoneAt.setText(activityDoneAt)
        binding.activiyDescription.setText(activityDescription)

        binding.btnEdit.setOnClickListener {
            val uid = Firebase.auth.currentUser?.uid
            Firebase.firestore
                .collection("users/$uid/activities/")
                .document(activityId!!)
                .set(
                    hashMapOf(
                        "activityName" to binding.activityName.text.toString(),
                        "activityDescription" to binding.activiyDescription.text.toString(),
                        "activityDuration" to activityDuration.toString().toLong(),
                    ),
                    SetOptions.merge()
                )
                .addOnSuccessListener {
                    Toast
                        .makeText(
                            applicationContext,
                            "Actividad editada",
                            Toast.LENGTH_LONG
                        )
                        .show()
                    finish()
                }
                .addOnFailureListener {
                    Toast
                        .makeText(
                            applicationContext,
                            "No se pudo editar la actividad",
                            Toast.LENGTH_LONG
                        )
                        .show()
                }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                 true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}