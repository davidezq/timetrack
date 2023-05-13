package com.example.timetrack.client.list

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.timetrack.client.menu.ui.dashboard.DashboardViewModel
import com.example.timetrack.databinding.ItemActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class ActivityViewHolder(view: View) : ViewHolder(view) {

    private val binding = ItemActivityBinding.bind(view)
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseFirestore: FirebaseFirestore
    fun render(
        activityName: String,
        activityDuration: Long,
        doneAt: Date,
        activityDescription: String,
        activityId: String,
    ) {
        // firebase variables
        firebaseAuth = Firebase.auth
        firebaseFirestore = Firebase.firestore

        binding.name.text = "Nombre: $activityName"
        binding.duration.text = "Duración: ${formatTime(activityDuration)}"
        binding.doneAt.text = "Hecho el: $doneAt"
        binding.description.text = "Descripción: $activityDescription"

        binding.root.setOnLongClickListener {
            showDialog(
                it,
                activityId,
                activityName,
                activityDescription
            )
            true
        }
    }

    fun showDialog(view: View, id: String, name: String, description: String) {
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("Eliminar activity")
        builder.setMessage(
            "¿Quieres eliminar la actividad?" +
                    "\nNombre: $name" +
                    "\nDescripción: $description"
        )
            .setPositiveButton("Yes") { _, _ ->
                val uid = firebaseAuth.currentUser?.uid
                firebaseFirestore.collection("users/$uid/activities")
                    .document(id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(view.context, "Actividad $name eliminada", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
            .setNegativeButton("No") { _, _ ->
                Log.d("probando", "no")
            }
            .create()
            .show()
    }
    fun formatTime(milliseconds: Long): String {
        val seconds = (milliseconds / 1000).toInt() % 60
        val minutes = (milliseconds / (1000 * 60) % 60).toInt()
        val hours = (milliseconds / (1000 * 60 * 60) % 24).toInt()

        val timeString = StringBuilder()
        if (hours > 0) {
            timeString.append(String.format("%02d", hours)).append(":")
        }
        timeString.append(String.format("%02d", minutes)).append(":").append(String.format("%02d", seconds))

        return timeString.toString()
    }
}