package com.example.fitnesstracker

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.fitnesstracker.R
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class LoginFragment : Fragment() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)

        etUsername = rootView.findViewById(R.id.usernameEditText)
        etPassword = rootView.findViewById(R.id.passwordEditText)
        btnLogin = rootView.findViewById(R.id.loginButton)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            val projection = arrayOf("_id", "username", "password")
            val selection = "username = ? AND password = ?"
            val selectionArgs = arrayOf(username, password)

            val uri = Uri.parse("content://com.example.fitnesstracker.provider/users")
            val cursor: Cursor? = requireContext().contentResolver.query(uri, projection, selection, selectionArgs, null)

            if (cursor != null && cursor.moveToFirst()) {

                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()

                val parentDialogFragment = parentFragment
                if (parentDialogFragment is DialogFragment) {
                    parentDialogFragment.dismiss()
                }
            } else {

                Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_SHORT).show()
            }

            cursor?.close()
        }

        return rootView
    }
}