package com.example.timetrack.client.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timetrack.R
import com.example.timetrack.client.models.ClientActivity

class ActivityAdapter(private val activities: List<ClientActivity>): RecyclerView.Adapter<ActivityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ActivityViewHolder(layoutInflater.inflate(R.layout.item_activity,parent,false))
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val item = activities[position]
        holder.render(
            item.activityName,
            item.activityDuration,
            item.doneAt,
            item.activityDescription
        )
    }

    override fun getItemCount(): Int{
        return activities.size
    }
}