package com.example.annexe6

import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var buttonAdd : Button
    lateinit var imageZone: ImageView
    var launcher: ActivityResultLauncher<Intent>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        launcher = registerForActivityResult( ActivityResultContracts.StartActivityForResult(), CallBackImage() )
        buttonAdd = findViewById(R.id.buttonAdd)
        imageZone = findViewById(R.id.imageView)

        buttonAdd.setOnClickListener{
            val intent = Intent( ACTION_OPEN_DOCUMENT)
            intent.setType("image/*");
            launcher ?.launch(intent)
        }

    }

    inner class CallBackImage : ActivityResultCallback<ActivityResult> {
        override fun onActivityResult(result: ActivityResult) {
            var intent = result.data
            var uri = intent !!. data
            imageZone.setImageURI(uri)
        }

    }
}