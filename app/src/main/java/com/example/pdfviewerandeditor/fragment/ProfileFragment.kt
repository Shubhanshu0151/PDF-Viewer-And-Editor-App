package com.example.pdfviewerandeditor.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.pdfviewerandeditor.R

class ProfileFragment : Fragment() {

    companion object {
        const val ARG_NAME = "name"
        const val ARG_MAIL = "mail"
        const val ARG_UNIQUE_ID = "uniqueId"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val name = arguments?.getString(ARG_NAME)
        val mail = arguments?.getString(ARG_MAIL)
        val uniqueId = arguments?.getString(ARG_UNIQUE_ID)

        val nameTextView = view.findViewById<TextView>(R.id.tvUserName)
        val mailTextView = view.findViewById<TextView>(R.id.tvUserMail)
        val uniqueIdTextView = view.findViewById<TextView>(R.id.tvUserId)

        if (name != null && mail != null && uniqueId != null) {
            nameTextView.text = "Name: $name"
            mailTextView.text = "Email: $mail"
            uniqueIdTextView.text = "UniqueId: $uniqueId"
        } else {
            // Handle the case where user data is not available
            // You can show an error message or take appropriate action
        }

        return view
    }
}