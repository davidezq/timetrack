package com.example.timetrack.client.menu.ui.dashboard

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetrack.client.list.ActivityAdapter
import com.example.timetrack.client.list.ActivityViewHolder
import com.example.timetrack.client.models.ClientActivity
import com.example.timetrack.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var activities = mutableListOf<ClientActivity>()
    private lateinit var adapter: ActivityAdapter
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private fun initRecyclerView() {
        adapter = ActivityAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val root: View = binding.root

        initRecyclerView()

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = Firebase.auth

        dashboardViewModel.activities.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = ActivityAdapter(it)
        }

        dashboardViewModel.getAllActivities(
            this.requireContext(),
            Firebase.auth,
            FirebaseFirestore.getInstance()
        )


        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*fun mostrarDatos(){
        var activitiesFirebase = mutableListOf<ClientActivity>()

        val uid = firebaseAuth.currentUser?.uid
        Log.d("ClientService", "$uid")

        if (uid.isNullOrEmpty()) {
            binding.recyclerView.adapter = ActivityAdapter(emptyList())
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
                        doc.getDate("doneAt") ?: Date()
                    )
                    activitiesFirebase.add(clientActivity)
                }
                ActivityAdapter(emptyList())
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Error al obtener documentos: $exception",
                    Toast.LENGTH_LONG
                )
            }


    }*/
}