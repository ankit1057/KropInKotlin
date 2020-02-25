package com.example.cropinkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.IntegerRes
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var target: KropTarget
    private lateinit var uri: Uri
    var REQUEST_PICK_IMAGE=1057
    private lateinit var galleryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        galleryButton=findViewById(R.id.buttonPic)

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
                imgRec.setImageURI(uri)
                intent= Intent(this,KropActivity::class.java)
                intent.putExtra("uri",uri.toString())
                startActivityForResult(intent,10)

            }
        }else if(requestCode==10){
            imgRec?.setImageURI(Uri.parse(data?.getStringExtra("ret")))
            Toast.makeText(this,"You Did it",Toast.LENGTH_LONG).show()
        }
    }



}


