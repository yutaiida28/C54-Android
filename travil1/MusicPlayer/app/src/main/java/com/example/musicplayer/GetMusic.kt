package com.example.musicplayer

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon

class GetMusic(context: Context, url: String) : Sujet{
    private val url = url
    private val context = context  // Garder le context
    private var mObs: MusicUpdateObserver? = null

    init {
        fetchMusic()
    }

    fun fetchMusic() {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val lm = Klaxon().parse<ListeMusics>(response) ?: ListeMusics()
                mObs?.succes(lm) // le .? est un verification si la variable est null on renvoie null pour eviter un nullpointer exception
            },
            { error ->
                mObs?.error()
            }
        )
        queue.add(stringRequest)
    }

    override fun addObserver(o: MusicUpdateObserver) {
        this.mObs = o
    }

    override fun removeObserver(o: MusicUpdateObserver) {
        this.mObs = null // il y en a un seul
    }
}
