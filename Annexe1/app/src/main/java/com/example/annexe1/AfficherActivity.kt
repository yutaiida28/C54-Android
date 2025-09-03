package com.example.annexe1

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader

class AfficherActivity : AppCompatActivity() {
    lateinit var liste: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_afficher)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        liste = findViewById(R.id.liste)
//        liste.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_1, lireMemos()))

        liste.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lireMemos())

    }

    private fun lireMemos(): ArrayList<String>
    {
        var arrayliste = ArrayList<String>()
        try {
            val fis = openFileInput("memo.txt")
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)

            // fermer le br lorsque terminé 3 exception
            br.use {
                var line = br.readLine()

                while (line != null) {
                    arrayliste.add(line)  // Add the actual line content
                    line = br.readLine()
                }
            }
            //autre facon
            br.use {
                br.forEachLine { ligne -> arrayliste }
                //alternative
                br.forEachLine { arrayliste.add(it) } // it : cette ligne-la
            }
            // 3e facon
            br.use {
                arrayliste = br.readLine() as ArrayList<String> // transtypage
            }
        }
        catch ( fnfe : FileNotFoundException)
        {
            fnfe.printStackTrace()
        }
        return arrayliste

    }
}