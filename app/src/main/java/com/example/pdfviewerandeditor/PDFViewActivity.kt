package com.example.pdfviewerandeditor

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.shockwave.pdfium.PdfDocument
import java.io.File
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Environment
import java.io.FileOutputStream

class PDFViewActivity : AppCompatActivity(), OnPageChangeListener, OnLoadCompleteListener {
    private lateinit var pdfView: PDFView
    private var pageNumber = 0
    private lateinit var pdfFilePath: String
    private val TAG = "PDFViewActivity"
    private var position = -1

    companion object {
        private const val REQUEST_CODE_OPEN_PDF = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfview)
        init()
        openPDF()
    }

    private fun init() {
        pdfView = findViewById(R.id.pdfview)
        position = intent.getIntExtra("position", -1)
    }

    private fun openPDF() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }

        startActivityForResult(intent, REQUEST_CODE_OPEN_PDF)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_OPEN_PDF && resultCode == RESULT_OK) {
            pdfFilePath = data?.data?.path ?: ""
            loadPDF() // Change this line
        } else {
            super.onActivityResult(requestCode, resultCode, data)
            this.finish()
        }
    }
    private fun loadPDF() {
        try {
            pdfView.fromUri(Uri.parse(pdfFilePath))
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(DefaultScrollHandle(this))
                .load()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("PDFViewActivity", "Error loading PDF: ${e.message}")
            // Handle or log the exception as needed
        }
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page
        title = String.format("%s %s / %s", pdfFilePath, page + 1, pageCount)
    }

    override fun loadComplete(nbPages: Int) {
        val meta = pdfView.documentMeta
        printBookmarksTree(pdfView.tableOfContents, "-")
    }

    private fun printBookmarksTree(tree: List<PdfDocument.Bookmark>, sep: String) {
        for (b in tree) {
            Log.e(TAG, String.format("%s %s, p %d", sep, b.title, b.pageIdx))

            if (b.hasChildren()) {
                printBookmarksTree(b.children, sep + "-")
            }
        }
    }

}