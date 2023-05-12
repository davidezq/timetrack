package com.example.timetrack.admin.ui.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.timetrack.databinding.ItemUsersBinding

class UsersViewHolder(val view: View):ViewHolder(view) {
    private val binding = ItemUsersBinding.bind(view)

    fun render(
        name:String,
        lastName:String,
        profession:String
    ){
        binding.name.text = "Name: $name"
        binding.lastName.text = "Last name: $lastName"
        binding.profession.text = "Profession: $profession"
    }
}