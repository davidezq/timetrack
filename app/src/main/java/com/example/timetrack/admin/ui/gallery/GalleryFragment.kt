package com.example.timetrack.admin.menu.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetrack.admin.ui.list.UsersAdapter
import com.example.timetrack.client.list.ActivityAdapter
import com.example.timetrack.databinding.FragmentGalleryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: UsersAdapter
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var galleryViewModel:GalleryViewModel
    private fun initRecyclerView() {
        adapter = UsersAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        initRecyclerView()
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = Firebase.auth

        galleryViewModel.users.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = UsersAdapter(it)
        }

        galleryViewModel.getAllUsers(
            this.requireContext(),
            firebaseAuth,
            firebaseFirestore
        )

        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        galleryViewModel.getAllUsers(
            this.requireContext(),
            firebaseAuth,
            firebaseFirestore
        )
    }
}