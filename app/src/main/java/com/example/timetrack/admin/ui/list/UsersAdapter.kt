package com.example.timetrack.admin.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.timetrack.R
import com.example.timetrack.admin.models.User

class UsersAdapter(private val users: List<User>) : Adapter<UsersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UsersViewHolder(layoutInflater.inflate(R.layout.item_users,parent,false))
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val item = users[position]

        holder.render(
            item.id,
            item.name,
            item.lastName,
            item.profession
        )
    }

    override fun getItemCount(): Int = users.size

}