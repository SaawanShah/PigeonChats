package com.chattingMessengerApp.pigeonapp
data class Message(

    var senderId: String = "",
    var receiverId: String = "",
    var message: String = "",
    var timestamp: Long = 0
)