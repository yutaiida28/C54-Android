package com.example.musicplayer

import android.content.Context
import android.content.Intent
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon

class GetMusid{
    private val url = "https://api.jsonbin.io/v3/b/680a6a1d8561e97a5006b822?meta=false"
    private var obs: ObservateurChangement? = null

    init{

    }
}

class GetMusic(context: Context) {
    val url = "https://api.jsonbin.io/v3/b/680a6a1d8561e97a5006b822?meta=false"
    var music: Music? = null

    val intentRetour = Intent()
    val queue = Volley.newRequestQueue()
    val stringRequest = StringRequest(
        Request.Method.GET,url,
        { responce ->
            val lm:ListeMusics = Klaxon().parse<ListeMusics>(responce) ?: ListeMusics()
            intentRetour.putExtra("lm", ListeMusics)
        },

    )

    queue.add(stringRequest)

    // Pas de destination !
    intentRetour.putExtra("utilisateur", utilisateur)

    setResult(Activity.RESULT_OK, intentRetour)
    finish()
}