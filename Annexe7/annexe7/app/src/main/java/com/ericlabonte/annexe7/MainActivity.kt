package com.ericlabonte.annexe7


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore

import android.view.View
import android.view.View.OnClickListener
import android.widget.Button

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.iterator


class MainActivity : AppCompatActivity() {

    lateinit var parent: LinearLayout
    var mImageView: ImageView? = null
    var launcher: ActivityResultLauncher<Intent>?= null;
//    plus que il nest pas un composent visuel on evite le lateinit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val ec = Ecouteur()
        parent = findViewById(R.id.main)
        mImageView = findViewById(R.id.imageView)

        for ( enfant in parent )
        {
            if ( enfant is Button)
                enfant.setOnClickListener(ec)
        }
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            CallBackPicture()
        )
    }

    inner class Ecouteur : OnClickListener {
        override fun onClick(v: View?) {
            if ( v?.id == R.id.boutonLivre)
            {
                val i = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.cvm.qu.ca"))
                startActivity(i)
            }
            else if ( v?.id == R.id.boutonAppel) // equals
            {
                val i = Intent( Intent.ACTION_DIAL, Uri.parse("tel:+1650444444" ))
                startActivity(i)
            }
            else if ( v?.id == R.id.boutonVille)
            {
                val i = Intent ( Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Hawkesbury, +on, +ca"))
                startActivity(i)
            }
            else if (v?.id == R.id.boutonPhoto)
            {
                val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                launcher?.launch(i)

            }
        }

    }
    inner class CallBackPicture : ActivityResultCallback<ActivityResult>{
        override fun onActivityResult(result: ActivityResult) {
            if( result.resultCode == Activity.RESULT_OK){
                val photo = result.data?.extras
                val bitmap = photo?.get("data") as Bitmap?
                if (bitmap != null)
                    mImageView?.setImageBitmap(bitmap)
            }
        }
    }


}