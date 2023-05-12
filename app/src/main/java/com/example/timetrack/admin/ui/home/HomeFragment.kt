package com.example.timetrack.admin.menu.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timetrack.R
import com.example.timetrack.auth.AuthClientActivity
import com.example.timetrack.databinding.FragmentAddUsersBinding
import com.example.timetrack.databinding.NavHeaderMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentAddUsersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var navHeaderBinding: NavHeaderMainBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentAddUsersBinding.inflate(inflater, container, false)

        firebaseAuth = Firebase.auth
        firebaseFirestore = Firebase.firestore

        binding.btnCreate.setOnClickListener {
            val errors = validateForm()
            if (errors != 0) return@setOnClickListener
            CreateAccount(
                binding.userName.text.toString(),
                binding.userLastName.text.toString(),
                binding.userEmail.text.toString(),
                binding.userPassword.text.toString(),
                binding.userProfession.text.toString()
            )
        }

        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validateForm(): Int {
        var errors = 0
        if (binding.userName.text.toString().isEmpty()) {
            binding.userName.error = "Name is required"
            errors++
        }
        if (binding.userLastName.text.toString().isEmpty()) {
            binding.userLastName.error = "Last name is required"
            errors++
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.userEmail.text.toString()).matches()) {
            binding.userEmail.error = "Email no válido"
            errors++
        }
        if (binding.userPassword.text.toString().length < 6) {
            binding.userPassword.error = "Password debe ser mayor a 6 caracteres"
            errors++
        }
        if (binding.userRepitPassword.text.toString() != binding.userPassword.text.toString()) {
            binding.userRepitPassword.error = "The password must be equal to password field"
            errors++
        }
        if (binding.userProfession.text.toString().isEmpty()) {
            binding.userProfession.error = "The profession is required"
            errors++
        }
        return errors
    }

    private fun CreateAccount(
        name: String,
        lastName: String,
        email: String,
        password: String,
        profession: String
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                firebaseFirestore
                    .collection("users")
                    .document(it.user?.uid.toString())
                    .set(
                        hashMapOf(
                            "name" to name,
                            "lastName" to lastName,
                            "profession" to profession
                        )
                    )
                    .addOnSuccessListener {
                        Toast.makeText(
                            this.context,
                            getString(R.string.message_account_created),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }

            }
            .addOnFailureListener {
                Toast.makeText(this.context, "Error: ${it.message}", Toast.LENGTH_LONG)
                    .show()
            }
    }
}