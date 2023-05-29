package com.example.fitnesstracker

import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment


class SignUpFragment : Fragment() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var signUpButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_signup, container, false)

        etUsername = rootView.findViewById(R.id.usernameEditText)
        etPassword = rootView.findViewById(R.id.passwordEditText)
        signUpButton = rootView.findViewById(R.id.signupButton)

        signUpButton.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            val values = ContentValues().apply {
                put("username", username)
                put("password", password)
            }

            val uri = Uri.parse("content://com.example.fitnesstracker.provider/users")
            val insertedUri = requireContext().contentResolver.insert(uri, values)

            if (insertedUri != null) {
                Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Registration Failed", Toast.LENGTH_SHORT).show()
            }
        }

        return rootView
    }
}