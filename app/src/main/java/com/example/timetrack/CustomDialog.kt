package com.example.timetrack

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.timetrack.databinding.ItemActivityBinding
import com.example.timetrack.databinding.LayoutDialogBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CustomDialog(val dialogTitle: String) : AppCompatDialogFragment() {
    private var listener: CustomDialogListener? = null

    fun setCustomDialogListener(listener: CustomDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().getLayoutInflater()
        val view = inflater.inflate(R.layout.layout_dialog, null)
        val binding = LayoutDialogBinding.bind(view)
        builder.setView(view)
            .setTitle(dialogTitle)
            .setPositiveButton("Guardar") { dialogInterface, i ->
                listener?.applyDescription(binding.editDescription.text.toString())
            }
        return builder.create()
    }

    interface CustomDialogListener {
        fun applyDescription(description: String)
    }
}