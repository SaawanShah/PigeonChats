package com.chattingMessengerApp.pigeonapp
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.chattingMessengerApp.pigeonapp.databinding.FragmentLoginBinding
import com.chattingMessengerApp.pigeonapp.databinding.FragmentOTPBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit


class OTPFragment : Fragment() {

    private lateinit var binding: FragmentOTPBinding
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOTPBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().reference

        val phone = arguments?.getString("phone") ?: ""

        val pref = requireActivity().getSharedPreferences("user", 0)
        pref.edit().putString("phone", phone).apply()
        val staticOtp = "123456"

        //  UID FIX (Firebase key)
        val uid = phone


//        Toast.makeText(requireContext(), "Checking user: $uid", Toast.LENGTH_LONG).show()
        Toast.makeText(
            requireContext(),
            "Your OTP is $staticOtp",
            Toast.LENGTH_LONG
        ).show()

        binding.buttonContinue.setOnClickListener {

            val enteredOtp = binding.etOtp.text.toString().trim()

            if (enteredOtp != staticOtp) {
                Toast.makeText(requireContext(), "Invalid OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //  PHONE IS THE UID
            val uid = phone

            database.child("user").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()) {

                            //  USER ALREADY EXISTS
                            startActivity(
                                Intent(requireActivity(), MainActivity::class.java)
                            )
                            requireActivity().finish()
                        } else {

                            val bundle = Bundle().apply {
                                putString("uid", uid)
                                putString("phone", phone)
                            }

                            findNavController().navigate(
                                R.id.action_OTPFragment_to_signUpFragment3,
                                bundle
                            )
                        }
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
}