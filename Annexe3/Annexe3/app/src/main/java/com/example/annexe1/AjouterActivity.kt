package com.example.annexe1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedWriter
import java.io.OutputStreamWriter

class AjouterActivity : AppCompatActivity() {
    lateinit var boutonAjouterMemo :Button
    lateinit var champMemo :EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ajouter)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        boutonAjouterMemo = findViewById(R.id.boutonAjouterMemo)
        champMemo = findViewById(R.id.champMemo)

        //simpification interface  fonctionnelle + lambda + lambda come dernier param -> a revoir
        boutonAjouterMemo.setOnClickListener{
            var texteMemo = champMemo.text.toString()
            //verifier si texteMemo est vide
            if(!texteMemo.isEmpty())
            {
                val fos = openFileOutput("memo.text", MODE_APPEND) // mode append est le mode ou on rajoute du contenue sans formater le contenue existent
                // parce que cest du texte on veux un flux de character, et non un flux de binaire
                val osw = OutputStreamWriter(fos)
                val bw = BufferedWriter(osw)

                bw.write(texteMemo)
                bw.newLine()

                bw.close()
                champMemo.text.clear()

                finish()  //pour revenir au mainActivity

                //pour aller voir le contenu du fichier memo.txt on vas sur device manager option(3point) puis open in device explorer puis data data le nom du package pui files il devrait y etre
            }
        }
    }
}