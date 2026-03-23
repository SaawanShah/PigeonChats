package com.chattingMessengerApp.pigeonapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chattingMessengerApp.pigeonapp.databinding.ItemChatSenderBinding
import com.chattingMessengerApp.pigeonapp.databinding.ItemMessageReceiverBinding
class ChatAdapter(
    private val messageList: ArrayList<Message>,
    private val currentUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_SENDER = 1
    private val ITEM_RECEIVER = 2

    // Decide sender or receiver
    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]

        return if (message.senderId == currentUserId) {
            ITEM_SENDER
        } else {
            ITEM_RECEIVER
        }
    }

    // Create ViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return if (viewType == ITEM_SENDER) {

            val binding = ItemChatSenderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            SenderViewHolder(binding)

        } else {

            val binding = ItemMessageReceiverBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ReceiverViewHolder(binding)
        }
    }

    // Bind data
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {

        val message = messageList[position]

        if (holder is SenderViewHolder) {
            holder.binding.tvSenderMessage.text = message.message
        }

        if (holder is ReceiverViewHolder) {
            holder.binding.tvReceiverMessage.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    // Sender ViewHolder
    class SenderViewHolder(
        val binding: ItemChatSenderBinding
    ) : RecyclerView.ViewHolder(binding.root)

    // Receiver ViewHolder
    class ReceiverViewHolder(
        val binding: ItemMessageReceiverBinding
    ) : RecyclerView.ViewHolder(binding.root)
}
