package com.chattingMessengerApp.pigeonapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.chattingMessengerApp.pigeonapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var adapter: UserAdapter
    private val userList = ArrayList<User>()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("user")

        adapter = UserAdapter(userList){user ->
//            Toast.makeText(
//                requireContext(),
//                "Clicked: ${user.name}",
//                Toast.LENGTH_SHORT
//            ).show()

            val bundle = Bundle().apply {
                putString("receiverId", user.uid)
                putString("receiverName", user.name)
            }

            findNavController().navigate(R.id.action_homeFragment_to_chatFragment,bundle)
        }

        binding.rvUsers.layoutManager=androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = adapter

    loadUsers()
    }

    private fun loadUsers(){
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (data in snapshot.children) {
                    val user = data.getValue(User::class.java)
                    if (user != null && user.uid != auth.currentUser?.uid) {
                        userList.add(user)
                    }
                }
                Toast.makeText(
                    requireContext(),
                    "Users count: ${userList.size}",
                    Toast.LENGTH_SHORT
                ).show()

                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}