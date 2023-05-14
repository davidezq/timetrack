package com.example.timetrack.admin.ui.list

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.timetrack.admin.EditUser
import com.example.timetrack.admin.UserChart
import com.example.timetrack.databinding.ItemUsersBinding
import com.google.firebase.firestore.auth.User

class UsersViewHolder(val view: View):ViewHolder(view) {
    private val binding = ItemUsersBinding.bind(view)

    fun render(
        id:String,
        name:String,
        lastName:String,
        profession:String
    ){
        binding.name.text = "Name: $name"
        binding.lastName.text = "Last name: $lastName"
        binding.profession.text = "Profession: $profession"

        binding.root.setOnLongClickListener {
            val i = Intent(it.context, EditUser::class.java)
            i.putExtra("id",id)
            i.putExtra("name",name)
            i.putExtra("lastName",lastName)
            i.putExtra("profession",profession)
            it.context.startActivity(i)
            true
        }
        binding.root.setOnClickListener{
            val i = Intent(it.context, UserChart::class.java)
            i.putExtra("id",id)
            it.context.startActivity(i)
        }
    }
}