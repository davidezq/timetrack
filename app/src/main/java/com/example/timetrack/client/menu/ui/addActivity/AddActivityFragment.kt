package com.example.timetrack.client.menu.ui.addActivity

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timetrack.CustomDialog
import com.example.timetrack.databinding.FragmentAddActivityBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AddActivityFragment : Fragment(), CustomDialog.CustomDialogListener {

    private var _binding: FragmentAddActivityBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //Chronometer variables
    private var isPaused = false
    private var pastTime: Long = 0L
    private var startTime: Long = 0L
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addActivityViewModel =
            ViewModelProvider(this).get(AddActivityViewModel::class.java)

        _binding = FragmentAddActivityBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.reset.isEnabled = false
        binding.pause.isEnabled = false

        binding.activityTitle.text.toString()
        binding.start.setOnClickListener {
            if (binding.activityTitle.text.toString().isEmpty()) {
                binding.activityTitle.error = "Agregue un titulo a la actividad"
                return@setOnClickListener
            }

            binding.activityTitle.isEnabled = false
            binding.start.isEnabled = false
            binding.pause.isEnabled = true
            binding.reset.isEnabled = true

            startChronometer()

        }

        binding.pause.setOnClickListener {
            pauseResumeChronometer()
        }

        binding.reset.setOnClickListener {
            binding.activityTitle.isEnabled = true
            binding.start.isEnabled = true
            binding.reset.isEnabled = false
            binding.pause.isEnabled = false

            resetChronometer()
            openDialog(binding.activityTitle.text.toString())
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun openDialog(dialogTitle: String) {
        val customDialog = CustomDialog(dialogTitle)
        customDialog.setCustomDialogListener(this)
        customDialog.show(requireActivity().supportFragmentManager, "example dialog")
    }

    fun startChronometer() {
        binding.chronometer.start()
        binding.chronometer.base = SystemClock.elapsedRealtime()
        isPaused = false
        pastTime = 0L
    }

    fun pauseResumeChronometer() {
        if (!isPaused) {
            pastTime = SystemClock.elapsedRealtime() - binding.chronometer.base
            binding.chronometer.stop()
            isPaused = true
        } else {
            binding.chronometer.base = SystemClock.elapsedRealtime() - pastTime
            binding.chronometer.start()
            isPaused = false
        }
    }

    fun resetChronometer() {
        binding.chronometer.stop()
        pastTime = SystemClock.elapsedRealtime() - binding.chronometer.base
        binding.chronometer.base = SystemClock.elapsedRealtime()
        isPaused = false
    }

    override fun applyDescription(description: String) {
        val auth = Firebase.auth
        val firestore = FirebaseFirestore.getInstance()
        val uid = auth.currentUser?.uid
        val activityTitle = binding.activityTitle.text.toString()

        if (uid.isNullOrEmpty()) {
            Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
            return
        }
        firestore
            .document("users/$uid")
            .collection("activities")
            .document()
            .set(
                hashMapOf(
                    "activityDescription" to description.trim(),
                    "activityDuration" to pastTime,
                    "activityName" to activityTitle,
                    "doneAt" to Date()
                )
            ).addOnSuccessListener {
                Toast.makeText(context, "Actividad guardada", Toast.LENGTH_SHORT)
                    .show()
                binding.activityTitle.text.clear()
            }
    }
}