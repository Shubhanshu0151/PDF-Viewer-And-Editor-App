package com.example.pdfviewerandeditor.fragment

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.pdfviewerandeditor.R

import com.itextpdf.io.exceptions.IOException
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream


class ToolsFragment : Fragment() {

    private val pdfList = mutableListOf<Uri>()
    private val pickPdfLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        uris?.let {
            pdfList.clear()
            pdfList.addAll(it)
        }

        // Print the number of selected PDFs
        println("Selected PDF count: ${pdfList.size}")

        // Perform PDF merge
        if (pdfList.size >= 2) {
            mergePDFs(pdfList)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tools, container, false)

        val cvSelectPDFs: CardView = view.findViewById(R.id.cvMerge)
        cvSelectPDFs.setOnClickListener {
            selectPDFs()
        }

        val imageToText : CardView = view.findViewById(R.id.cvImageToText)
        imageToText.setOnClickListener {
            Toast.makeText(activity,"Click Photo",Toast.LENGTH_SHORT).show()
            val newFragment = ImageToTextFragment()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout, newFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return view
    }

    private fun selectPDFs() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        pickPdfLauncher.launch("application/pdf")
    }

    private fun mergePDFs(pdfUris: List<Uri>) {
        try {
            val outputFile = requireContext().externalCacheDir?.absolutePath + "/merged.pdf"
            val outputStream = FileOutputStream(outputFile)

            val mergedDocument = PdfDocument(PdfWriter(outputStream))

            for (pdfUri in pdfUris) {
                PdfDocument(PdfReader(requireContext().contentResolver.openInputStream(pdfUri))).use { pdfDocument ->
                    pdfDocument.copyPagesTo(1, pdfDocument.numberOfPages, mergedDocument)
                }
            }

            mergedDocument.close()

            // Notify the user that merging is complete
            showToast("PDFs merged successfully")

            // Open the merged PDF in a PDF viewer or provide a download link
            openPdfInViewer(outputFile)
        } catch (e: IOException) {
            e.printStackTrace()
            // TODO: Handle the error
            showToast("Error merging PDFs")
        }
    }

    // merge pdf
    private fun openPdfInViewer(filePath: String) {
        val openPdfIntent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(requireContext(), "com.example.pdfviewerandeditor.fileprovider", File(filePath))
        openPdfIntent.setDataAndType(uri, "application/pdf")
        openPdfIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_CLEAR_TOP
        try {
            startActivity(openPdfIntent)
        } catch (e: ActivityNotFoundException) {
            showToast("No PDF viewer app installed")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
