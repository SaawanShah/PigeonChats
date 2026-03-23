package com.chattingMessengerApp.pigeonapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.chattingMessengerApp.pigeonapp.databinding.FragmentLoginBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    var staticOtp = 123456

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        binding.buttonContinue.setOnClickListener {
//            Toast.makeText(requireContext(), "Button Clicked", Toast.LENGTH_SHORT).show()

            loginUser()

        }
    }

    private fun loginUser() {
        val phone = binding.etPhoneNo.text.toString().trim()

        if (phone.length != 10) {
            Toast.makeText(requireContext(), "Enter valid number", Toast.LENGTH_SHORT).show()
        } else {

            val bundle = Bundle().apply {
                putString("phone", phone)
                putString("otp", "123456") // static otp
            }

            findNavController()
                .navigate(R.id.action_loginFragment_to_OTPFragment, bundle)
        }
    }
}