package com.chattingMessengerApp.pigeonapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.chattingMessengerApp.pigeonapp.User
import com.chattingMessengerApp.pigeonapp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUpFragment : Fragment() {

    lateinit var binding: FragmentSignUpBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var uid: String = ""
    private var phone: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().reference

        uid = arguments?.getString("uid") ?: ""
        phone = arguments?.getString("phone") ?: ""

        binding.btSignUp.setOnClickListener {
            saveUserToFirebase()
        }
    }

    private fun saveUserToFirebase() {

        val name = binding.etName.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Enter name", Toast.LENGTH_SHORT).show()
            return
        }


        val user = User(
            uid = uid,
            phoneNumber = phone,
            name = name,

        )

        // 🔥 SAVE USING PHONE / UID (SAME AS OTP CHECK)
        database.child("user").child(uid)
            .setValue(user)
            .addOnSuccessListener {
                startActivity(
                    Intent(requireActivity(), MainActivity::class.java)
                )
                requireActivity().finish()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
    }
}
