package com.example.timetrack.client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.timetrack.client.menu.MainActivity
import com.example.timetrack.databinding.ActivityDeleteClienteBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DeleteCliente : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //toolBar options
        title = "wapalizer"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // getting activity fields
        val activityId = intent.getStringExtra("id")
        val activityName = intent.getStringExtra("name")
        val activityDuration = intent.getStringExtra("duration")
        val activityDoneAt = intent.getStringExtra("done_at")
        val activityDescription = intent.getStringExtra("description")

        // setting the fields in the activity
        binding.activityName.text = activityName
        binding.activityDuration.text = activityDuration
        binding.activityDoneAt.text = activityDoneAt
        binding.activiyDescription.text = activityDescription

        binding.btnDelete.setOnClickListener {
            val uid = Firebase.auth.currentUser?.uid
            Firebase.firestore
                .collection("users/$uid/activities/")
                .document(activityId!!)
                .delete()
                .addOnSuccessListener {
                    Toast
                        .makeText(
                            applicationContext,
                            "Actividad eliminada",
                            Toast.LENGTH_LONG
                        )
                        .show()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
        }

    }


}