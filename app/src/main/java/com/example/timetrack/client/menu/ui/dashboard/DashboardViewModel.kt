package com.example.timetrack.client.menu.ui.dashboard

import android.content.ContentProvider
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timetrack.client.models.ClientActivity
import com.example.timetrack.client.services.ClientService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.*

class DashboardViewModel : ViewModel() {
    val activities = MutableLiveData<List<ClientActivity>>()

    fun getAllActivities(context:Context,firebaseAuth: FirebaseAuth,firebaseFirestore: FirebaseFirestore) {
        var activitiesFirebase = mutableListOf<ClientActivity>()

        val uid = firebaseAuth.currentUser?.uid
        Log.d("ClientService", "$uid")

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
                    "Error al obtener documentos: $exception",
                    Toast.LENGTH_LONG
                )
            }


    }
}