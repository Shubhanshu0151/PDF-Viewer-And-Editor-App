package com.example.pdfviewerandeditor.fragment

import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.findFragment
import com.example.pdfviewerandeditor.R
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class ImageToTextFragment : Fragment() {

    lateinit var result : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_image_to_text, container, false)

        val camera: View = view.findViewById(R.id.ivCamera)
        val delete: View = view.findViewById(R.id.ivDelete)
        val copy: View = view.findViewById(R.id.ivCopy)

        result = view.findViewById(R.id.result)
        camera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (intent.resolveActivity(requireActivity().packageManager) != null){
                startActivityForResult(intent,123)
            }else{
                Toast.makeText(activity,"Oops Something Went Wrong",Toast.LENGTH_SHORT).show()
            }
        }

        delete.setOnClickListener {
            result.setText("")
        }

        copy.setOnClickListener {
            val clipBoard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("lable", result.text.toString())
            clipBoard.setPrimaryClip(clip)
            Toast.makeText(activity,"Copy To ClipBoard",Toast.LENGTH_SHORT).show()


        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123 && resultCode == RESULT_OK){
            val extras = data?.extras
            val bitmap = extras?.get("data") as Bitmap
            detectTextUsingMl(bitmap)
        }
    }

    private fun detectTextUsingMl(bitmap: Bitmap) {
        // When using Latin script library
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        // When using Chinese script library
        val recognizerChinese = TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())

        // When using Devanagari script library
        val recognizerDevanagari = TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build())

        // When using Japanese script library
        val recognizerJapanese = TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())

        // When using Korean script library
        val recognizerKorean = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

        val image = InputImage.fromBitmap(bitmap, 0)


        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Task completed successfully
                // ...
                result.setText(visionText.text.toString())
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
                Toast.makeText(activity,"Oops Something Went Wrong",Toast.LENGTH_SHORT).show()

            }
    }

}
