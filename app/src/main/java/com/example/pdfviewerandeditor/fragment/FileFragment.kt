package com.example.pdfviewerandeditor.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.example.pdfviewerandeditor.FragTwo.ExternalFragment
import com.example.pdfviewerandeditor.FragTwo.InternalFragment
import com.example.pdfviewerandeditor.R
class FileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_file, container, false)

        val linearLayout: LinearLayout = view.findViewById(R.id.LinearLayout)
        linearLayout.setOnClickListener {
            // Navigate to InternalFragment when LinearLayout is clicked
            val newFragment = InternalFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, newFragment)
            transaction.addToBackStack(null) // Optional: Adds the transaction to the back stack for back navigation
            transaction.commit()
        }

        val cardView2: View = view.findViewById(R.id.CardView2)
        cardView2.setOnClickListener {
            Toast.makeText(activity, "Open External Storage", Toast.LENGTH_SHORT).show()

            val exFragment = ExternalFragment()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout, exFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        return view
    }
}