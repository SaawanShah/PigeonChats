package com.chattingMessengerApp.pigeonapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chattingMessengerApp.pigeonapp.databinding.ItemUserBinding

class UserAdapter(
    private val userList: ArrayList<User>,
    private val onUserClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
        }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val user = userList[position]

        holder.binding.tvUserName.text =
            if (user.name.isNotEmpty()) user.name else user.phoneNumber

        holder.binding.tvLastMessage.text = "Tap to chat"

        //  CLICK WORKING
        holder.binding.root.setOnClickListener {
            onUserClick.invoke(user)
        }
    }

    override fun getItemCount(): Int = userList.size
    }


