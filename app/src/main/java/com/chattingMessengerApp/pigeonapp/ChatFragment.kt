package com.chattingMessengerApp.pigeonapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.chattingMessengerApp.pigeonapp.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatFragment : Fragment() {

    private lateinit var receiverId: String
    private lateinit var senderId: String
    private lateinit var binding: FragmentChatBinding
    private lateinit var messageList: ArrayList<Message>
    private lateinit var adapter: ChatAdapter
    private lateinit var database: DatabaseReference
    private lateinit var chatKey: String
    private lateinit var receiverName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get senderId from SharedPreferences
        val pref = requireActivity().getSharedPreferences("user", 0)
        senderId = pref.getString("phone", "") ?: ""

        // Get receiverId from arguments
        receiverId = arguments?.getString("receiverId") ?: ""
        receiverName = arguments?.getString("receiverName") ?: "Chat"

        binding.toolbar.title = receiverName
        Log.d("CHAT_DEBUG", "Sender ID: $senderId")
        Log.d("CHAT_DEBUG", "Receiver ID: $receiverId")

        // Generate chatKey
        chatKey = listOf(senderId, receiverId).sorted().joinToString("_")

        Log.d("CHAT_DEBUG", "ChatKey: $chatKey")

        database = FirebaseDatabase
            .getInstance()
            .getReference("chats")
            .child(chatKey)

        messageList = ArrayList()
        adapter = ChatAdapter(messageList, senderId)

        binding.rvChat.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true

        binding.rvChat.layoutManager = layoutManager
        binding.rvChat.adapter = adapter

        receiveMessages()
        sendMessage()
    }

    private fun sendMessage() {

        binding.btnSend.setOnClickListener {

            val text = binding.etMessage.text.toString().trim()

            if (text.isEmpty()) return@setOnClickListener

            val message = Message(
                senderId = senderId,
                receiverId = receiverId,
                message = text,
                timestamp = System.currentTimeMillis()
            )

            database.push().setValue(message).addOnSuccessListener {

                binding.etMessage.setText("")
            }
        }
    }

    private fun receiveMessages() {

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                messageList.clear()

                for (data in snapshot.children) {

                    val msg = data.getValue(Message::class.java)

                    if (msg != null) {
                        messageList.add(msg)
                    }
                }

                adapter.notifyDataSetChanged()

                if (messageList.isNotEmpty()) {
                    binding.rvChat.scrollToPosition(messageList.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}