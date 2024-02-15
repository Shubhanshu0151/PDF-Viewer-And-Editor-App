package com.example.pdfviewerandeditor.FragTwo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pdfviewerandeditor.R
import com.github.barteksc.pdfviewer.PDFView

class InternalFragment : Fragment() {

    private lateinit var pdfView: PDFView
    private lateinit var pageNumberTextView: TextView
    val PDF_SECLECT_CODE = 100


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_internal, container, false)


        pdfView = view.findViewById(R.id.pdfView)

        pageNumberTextView = view.findViewById(R.id.pageNumberTextView)
        selectPdfFromStorage()
        return view
    }

    private fun selectPdfFromStorage() {
        Toast.makeText(context, "Select PDF File", Toast.LENGTH_SHORT).show()
        val browseStorage = Intent(Intent.ACTION_GET_CONTENT)
        browseStorage.type = "application/pdf"
        browseStorage.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(browseStorage,"Select pdf"),PDF_SECLECT_CODE)
    }
    fun showPdfFromUri(uri: Uri?){
        pdfView.fromUri(uri)
            .defaultPage(0)
            .onPageChange { page, pageCount ->
                // Handle page change, update your page number indicator here
                updatePageNumberIndicator(page + 1, pageCount)
            }
            .load()
    }

    private fun updatePageNumberIndicator(currentPage: Int, pageCount: Int) {
        // Implement your logic to update the page number indicator
        // For example, you can use a TextView to display the page number
        // Assuming you have a TextView with id "pageNumberTextView" in your layout
        val pageNumberTextView = view?.findViewById<TextView>(R.id.pageNumberTextView)
        pageNumberTextView?.text = "Page $currentPage of $pageCount"
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == PDF_SECLECT_CODE && resultCode == Activity.RESULT_OK && data !=  null) {

        val selectedPdf = data.data

        showPdfFromUri(selectedPdf)

    }
}
}