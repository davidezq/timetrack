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

    /*fun showDialog(view: View, id: String, name: String, description: String) {
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle(view.context.getString(R.string.delete_activity))
        builder.setMessage(
            view.context.getString(R.string.do_you_want_to_delete_the_activity) +
                    "\n${view.context.getString(R.string.name)}: $name" +
                    "\n${view.context.getString(R.string.description)}: $description"
        )
            .setPositiveButton(view.context.getString(R.string.yes)) { _, _ ->
                val uid = Firebase.auth.currentUser?.uid
                Firebase.firestore.collection("users/$uid/activities")
                    .document(id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(view.context, "Actividad $name eliminada", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
            .setNegativeButton(view.context.getString(R.string.no)) { _, _ ->
            }
            .create()
            .show()
    }*/
}