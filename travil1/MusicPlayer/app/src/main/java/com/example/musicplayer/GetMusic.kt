package com.example.musicplayer

import android.content.Context
import android.content.Intent
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
/*


class GetMusic(context: Context) {
    val url = "https://api.jsonbin.io/v3/b/680a6a1d8561e97a5006b822?meta=false"
    private val context = context
    var lm: ListeMusics? = null

    private var mObs: musicUpdateObserver? = null



    fun setObserver(observer: musicUpdateObserver){
        this.mObs = observer
    }

    fun getMusics(context : Context){
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET,url,
            { responce ->
                this.lm = Klaxon().parse<ListeMusics>(responce) ?: ListeMusics()
                mObs!!.succes(lm)
            },
            { error ->
                mObs!!.error()
            }

        )

        queue.add(stringRequest)
    }

*/
class GetMusic(context: Context, url: String) {
    private val url = url
    private val context = context  // Garder le context
    private var mObs: musicUpdateObserver? = null

    fun setObserver(observer: musicUpdateObserver) {
        mObs = observer
    }

    fun fetchMusic() {  // Nom plus clair
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val lm = Klaxon().parse<ListeMusics>(response) ?: ListeMusics()
                mObs?.succes(lm)  // Safe call
            },
            { error ->
                mObs?.error()  // Safe call
            }
        )
        queue.add(stringRequest)
    }
}
