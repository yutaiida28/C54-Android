package com.example.atelier1b

import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon

class MainActivity : AppCompatActivity() {
    val url = "https://api.jsonbin.io/v3/b/67fe6a908a456b796689f63d?meta=false"
    lateinit var listeAccessoires: ListView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listeAccessoires = findViewById(R.id.liste)
        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(
            Request.Method.GET,url,
            { responce ->
                val li:ListeProduits = Klaxon().parse<ListeProduits>(responce) ?: ListeProduits()
                Toast.makeText(this, "response is ${li.articles.size}",LENGTH_LONG).show()
                afficher(li)
            },
            { Toast.makeText(this, "fuck you", LENGTH_LONG).show() }
        )

        queue.add(stringRequest)
    }
    fun afficher(li: ListeProduits){
        val remplir = ArrayList<HashMap<String, String>>()

        for (article in li.articles) {
            val temp = HashMap<String, String>()
            temp["nom"] = article.nom
            temp["prix"] = article.prix
            remplir.add(temp)
        }

        val adap = SimpleAdapter(
            this,
            remplir,
            R.layout.layout,
            arrayOf("nom", "prix"),
            intArrayOf(R.id.nom, R.id.prix)
        )

        listeAccessoires.setAdapter(adap);
    }
}