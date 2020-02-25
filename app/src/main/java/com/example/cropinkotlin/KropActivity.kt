package com.example.cropinkotlin

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_krop.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class KropActivity : AppCompatActivity() {

    private lateinit var uri: Uri
    private lateinit var target: Target


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_krop)
        uri = Uri.parse(intent.getStringExtra("uri"))
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

        krop_view.setBitmap(bitmap)

        var intent2 : Intent=Intent()
        btnCropped.setOnClickListener(View.OnClickListener {
            intent2.putExtra("ret",saveImageToInternalStorage())
            setResult(Activity.RESULT_OK,intent2)
            finish()
        })

    }
    // Method to save an image to internal storage
    private fun saveImageToInternalStorage():String{
        // Get the image from drawable resource as drawable object

        // Get the bitmap from drawable object
        val bitmap = krop_view?.getCroppedBitmap()

        // Get the context wrapper instance
        val wrapper = ContextWrapper(applicationContext)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)


        // Create a file to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image uri
        return Uri.parse(file.absolutePath).toString()
    }

}
