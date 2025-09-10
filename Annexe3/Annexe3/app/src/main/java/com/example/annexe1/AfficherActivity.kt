package com.example.annexe1

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
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

        liste.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lireMemos()!!)

    }

    private fun lireMemos(): ArrayList<String>?
    {
        //si on veux initialiser a nulll ou none on mets point ? pis il faut le trimbler
        var v: ArrayList<Memo>? = null
        var triee : ArrayList<String>? = null
        try {
            v = SinglrtonMemos.deSerialiserListe(this@AfficherActivity) // liste de memos qui vient du singleton
            v.sortWith(compareBy{it.echance}) // fonction a haut niveau car elle prend pas de parametre on peux donc coder directement dans les accolade (trie en fonction de l'écheance
            triee = ArrayList<String>() // liste de string vide
            for (memo in v) // pour chaque memos de la liste
                triee.add( memo.message +" "+ memo.echance) //ajoute les message du memo
        }
        catch (f:FileNotFoundException)
        {
            Toast.makeText(this@AfficherActivity, "pas de fichier de serialisation", Toast.LENGTH_LONG).show()
            finish()
        }

        return triee
    }
}