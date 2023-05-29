package com.example.fitnesstracker

import android.os.Bundle
import android.text.TextUtils.replace
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment

class FitnessDialogFragment: DialogFragment(R.layout.fragment_dialog) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firstFragment = SignUpFragment()
        val secondFragment = LoginFragment()
        val signupBtn: Button = view.findViewById(R.id.signupBtn)
        val loginBtn: Button = view.findViewById(R.id.loginBtn)
        val closeBtn: ImageView = view.findViewById(R.id.closeBtn)
        childFragmentManager.beginTransaction().apply {
            replace(R.id.changeFragment, firstFragment)
            commit()
        }
        signupBtn.setOnClickListener{
            childFragmentManager.beginTransaction().apply {
                replace(R.id.changeFragment, firstFragment)
                commit()
            }
        }
        loginBtn.setOnClickListener{
            childFragmentManager.beginTransaction().apply {
                replace(R.id.changeFragment, secondFragment)
                commit()
            }
        }
        closeBtn.setOnClickListener{
            dismiss()
        }

    }
}
