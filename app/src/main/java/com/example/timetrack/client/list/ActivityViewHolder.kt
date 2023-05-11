package com.example.timetrack.client.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.timetrack.databinding.ItemActivityBinding
import java.util.Date

class ActivityViewHolder(view: View) : ViewHolder(view) {
    private val binding = ItemActivityBinding.bind(view)

    fun render(
        activityName: String,
        activityDuration: Long,
        doneAt: Date,
        activityDescription:String
    ) {
        binding.name.text = "Nombre: $activityName"
        binding.duration.text = "Duración: ${activityDuration}"
        binding.doneAt.text = "Hecho el: $doneAt"
        binding.description.text = "Descripción: $activityDescription"
    }
}