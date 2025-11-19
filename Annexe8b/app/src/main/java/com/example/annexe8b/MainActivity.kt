package com.example.annexe8b

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var buttonHorizon : Button
    lateinit var buttonZoom : Button
    lateinit var buttonSplash : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        buttonHorizon = findViewById(R.id.buttonHorizontal)
        buttonZoom = findViewById(R.id.buttonZoom)
        buttonSplash = findViewById(R.id.buttonSplash)

        val ec = Listener()
        buttonHorizon.setOnClickListener(ec)
        buttonZoom.setOnClickListener(ec)
        buttonSplash.setOnClickListener(ec)

    }
    inner class Listener : OnClickListener {
        override fun onClick(v: View?) {
            when ( v ){
//                boutonQuitter -> finish()
                buttonHorizon -> {
                    val i = Intent(this@MainActivity, AnimationHorizontal::class.java)
                    startActivity(i)
                }
                buttonZoom -> {
                    val i = Intent(this@MainActivity, AnimationZoom::class.java)
                    startActivity(i)
                }
                buttonSplash -> {
                    val i = Intent(this@MainActivity, AnimationSplash::class.java)
                    startActivity(i)
                }
            }
        }
    }
}