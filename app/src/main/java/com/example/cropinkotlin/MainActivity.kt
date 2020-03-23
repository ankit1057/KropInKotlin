package com.example.cropinkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.avito.android.krop.KropView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var uri: Uri
    var REQUEST_PICK_IMAGE = 1057
    private lateinit var galleryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        galleryButton = findViewById(R.id.buttonPic)

        galleryButton.setOnClickListener(View.OnClickListener {
            selectPicture()
        })
    }

    private fun selectPicture() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(Intent.createChooser(intent, "Select"), REQUEST_PICK_IMAGE)
    }

    @SuppressLint("MissingSuperCall")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data?.data ?: return
                //imgRec.setImageURI(uri)
                //uri = Uri.parse(intent.getStringExtra("uri"))

                val dialog = Dialog(this)
                val view = layoutInflater.inflate(R.layout.activity_krop, null, false)
                dialog.setContentView(view)
                dialog.show()

                var krop_view = view.findViewById<KropView>(R.id.krop_view)
                var buttonCropped = view.findViewById<Button>(R.id.btnCropped)

                val bitmap = getBitmap(this.contentResolver, uri)
                krop_view.setBitmap(bitmap)

                buttonCropped.setOnClickListener {
                    val image = krop_view?.getCroppedBitmap()

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
                        image?.compress(Bitmap.CompressFormat.JPEG, 100, stream)

                        // Flush the stream
                        stream.flush()

                        // Close stream
                        stream.close()
                    } catch (e: IOException) { // Catch the exception
                        e.printStackTrace()
                    }

                    imgRec.setImageURI(Uri.parse(file.absoluteFile.toString()))
                    dialog.dismiss()
                }

                dialog.window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                )


            }
        }
    }


}


