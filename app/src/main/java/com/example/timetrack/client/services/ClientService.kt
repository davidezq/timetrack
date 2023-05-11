package com.example.timetrack.client.services

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.timetrack.client.models.ClientActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class ClientService(
    val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) {

    fun getAllActivities(): List<ClientActivity> {
        var activities = mutableListOf<ClientActivity>()

        val uid = firebaseAuth.currentUser?.uid
        Log.d("ClientService", "$uid")

        if (uid.isNullOrEmpty()) {
            return emptyList()
        }

        firebaseFirestore
            .collection("users/$uid/activities")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    Log.d("ClientService", "${doc.getString("activityName")}")
                    var clientACtivity = ClientActivity(
                        doc.getString("activityDescription") ?: "",
                        doc.getLong("activityDuration") ?: 0,
                        doc.getString("activityName") ?: "",
                        doc.getDate("doneAt") ?: Date()
                    )
                    activities.add(clientACtivity)
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Error al obtener documentos: $exception",
                    Toast.LENGTH_LONG
                )
            }
        return activities
    }


}