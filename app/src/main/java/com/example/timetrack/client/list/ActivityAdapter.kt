package com.example.timetrack.client.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.timetrack.R
import com.example.timetrack.client.models.ClientActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActivityAdapter(
    private val activities: List<ClientActivity>,
) : RecyclerView.Adapter<ActivityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ActivityViewHolder(layoutInflater.inflate(R.layout.item_activity, parent, false))
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val item = activities[position]
        holder.render(
            item.activityName,
            item.activityDuration,
            item.doneAt,
            item.activityDescription,
            item.id,
        )

    }

    override fun getItemCount(): Int {
        return activities.size
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
                val uid = Firebase.auth.currentUser?.uid
                Firebase.firestore.collection("users/$uid/activities")
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
}