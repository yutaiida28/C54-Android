package com.example.appserialisation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var bouton: Button

    lateinit var ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bouton = findViewById(R.id.button2);
        bouton.setOnClickListener{v:View -> lancer.launch(

        )}
    }
    inner class  registerForActivityResult{

    }
    inner class Ecouteur : View.OnClickListener {
        override fun onClick(v: View?) {
            if (v == bouton) {
                val i = Intent(this@MainActivity, LoginActivity::class.java)

                launch(i)
            }
        }
    }

}