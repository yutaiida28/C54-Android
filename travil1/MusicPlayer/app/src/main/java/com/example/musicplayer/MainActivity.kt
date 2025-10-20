package com.example.musicplayer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon

class MainActivity : AppCompatActivity() {
    val url = "https://api.jsonbin.io/v3/b/680a6a1d8561e97a5006b822?meta=false"
    lateinit var launcher: ActivityResultLauncher<Intent>;
    var music: Music? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            CallBackUtilisateur()
        )
        val intent = Intent(this, GetMusic::class.java)

        launcher.launch(intent )

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
    }


}