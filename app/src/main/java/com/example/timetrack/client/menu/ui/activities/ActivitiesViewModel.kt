package com.example.timetrack.client.menu.ui.activities

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timetrack.R
import com.example.timetrack.client.models.ClientActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ActivitiesViewModel : ViewModel() {
    val activities = MutableLiveData<List<ClientActivity>>()

    fun getAllActivities(context:Context,firebaseAuth: FirebaseAuth,firebaseFirestore: FirebaseFirestore) {
        var activitiesFirebase = mutableListOf<ClientActivity>()

        val uid = firebaseAuth.currentUser?.uid

        if (uid.isNullOrEmpty()) {
            activities.postValue(emptyList())
        }

        firebaseFirestore
            .collection("users/$uid/activities")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    Log.d("ClientService", "${doc.getString("activityName")}")
                    var clientActivity = ClientActivity(
                        doc.getString("activityDescription") ?: "",
                        doc.getLong("activityDuration") ?: 0,
                        doc.getString("activityName") ?: "",
                        doc.getDate("doneAt") ?: Date(),
                        doc.id
                    )
                    activitiesFirebase.add(clientActivity)
                }
                activities.postValue(
                    activitiesFirebase
                )
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "${context.getString(R.string.error_get_documents)} $exception",
                    Toast.LENGTH_LONG
                )
            }


    }
}