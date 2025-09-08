package com.example.annexe1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


import android.widget.Button
import android.view.View
import android.view.View.OnClickListener
import android.content.Intent

class MainActivity : AppCompatActivity() {
    lateinit var boutonAjouter:Button
    lateinit var boutonAficher:Button
    lateinit var boutonQuitter:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        boutonAjouter = findViewById(R.id.boutonAjouter)
        boutonAficher = findViewById(R.id.boutonAficher)
        boutonQuitter = findViewById(R.id.boutonQuitter)


        //1ere
        val ec = Ecouteur()
        // 2e etape
        boutonAjouter.setOnClickListener(ec)
        boutonAficher.setOnClickListener(ec)
        boutonQuitter.setOnClickListener(ec)
    }

    // 3e etape
    inner class Ecouteur : OnClickListener{
        override  fun onClick(v: View?) {
            when ( v ){
                boutonQuitter -> finish()
                boutonAjouter -> {
                    val i = Intent(this@MainActivity, AjouterActivity::class.java)
                    startActivity(i)
                }
                boutonAficher -> {
                    val i = Intent(this@MainActivity, AfficherActivity::class.java)
                    startActivity(i)
                }
            }
        }
    }
}