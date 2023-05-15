package com.example.timetrack.admin.menu.ui.gallery

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timetrack.admin.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ShowUsersViewModel : ViewModel() {

    val users = MutableLiveData<List<User>>()

    fun getAllUsers(
        context: Context,
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ) {

        var usersDB = mutableListOf<String>()
        var collector = mutableListOf<User>()
        val uid = firebaseAuth.currentUser?.uid
        Log.d("UsersService", "$uid")

        if (uid.isNullOrEmpty()) {
            users.postValue(emptyList())
        }

        firebaseFirestore
            .collection("admins")
            .document("$uid")
            .get()
            .addOnSuccessListener { doc ->
                usersDB = doc.get("users") as MutableList<String>
                Log.d("UsersService", "$usersDB")
                usersDB.forEach { userUID ->
                    firebaseFirestore
                        .collection("users")
                        .document("$userUID")
                        .get()
                        .addOnSuccessListener { userInfo ->
                            collector.add(
                                User(
                                    userInfo.id,
                                    userInfo.getString("name")!!,
                                    userInfo.getString("lastName")!!,
                                    userInfo.getString("profession")!!
                                )
                            )
                            Log.d("UsersService", "$collector")
                            users.postValue(collector)
                        }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Error al obtener los usuarios: $exception",
                    Toast.LENGTH_LONG
                )
            }


    }
}