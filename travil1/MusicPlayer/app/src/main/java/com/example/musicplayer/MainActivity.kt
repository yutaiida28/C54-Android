package com.example.musicplayer

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), MusicUpdateObserver {
    val url = "https://api.jsonbin.io/v3/b/680a6a1d8561e97a5006b822?meta=false"
    var musicUpdate: Sujet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    /*
        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(
            Request.Method.GET,url,
            { responce ->
                val lm:ListeMusics = Klaxon().parse<ListeMusics>(responce) ?: ListeMusics()
                Toast.makeText(this, "response is ${lm.listeMusic.size}", LENGTH_LONG).show()
            },
            { Toast.makeText(this, "fuck you", LENGTH_LONG).show() }
        )
        queue.add(stringRequest)
    */
    }

    override fun onStart() {
        super.onStart()
        musicUpdate = GetMusic(this ,url)
        (musicUpdate as GetMusic).addObserver(this)
    }

    override fun succes(lm: ListeMusics) {
        Toast.makeText(this, "response is ${lm.listeMusic.size}", LENGTH_LONG).show()
        afficher(lm)
    }

    fun afficher(lm: ListeMusics){

    }


    override fun error() {
        TODO("Not yet implemented")
    }


}