package com.example.appexam2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar

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
import org.w3c.dom.Text
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    val url = "https://api.jsonbin.io/v3/b/6908bbe4d0ea881f40d109b6?meta=false"
    private lateinit var progre: ProgressBar
    private lateinit var zoneReponce:  EditText
    private lateinit var verif: Button
    private  var noms : ArrayList<String>? = null
    private  var obtenue : ArrayList<String>? = null
    var trouver = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        progre = findViewById(R.id.progressBar)
        zoneReponce  = findViewById(R.id.champAlbum)
        verif = findViewById(R.id.boutonVerifier)
        this.obtenue = ArrayList<String>()
        verif.setOnClickListener {
            var reponce = zoneReponce.text.toString()
            getnom(reponce)
        }

        val queue = Volley.newRequestQueue(this)


        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                val lm: ListeMusic = Klaxon().parse<ListeMusic>(response) ?: ListeMusic()
                Toast.makeText(this, "le jeux est pret",LENGTH_LONG).show()
                ajouter(lm)
            },
            { Toast.makeText(this, "erreure de connection ", LENGTH_LONG).show() }
        )
        queue.add(stringRequest)

    }
    private fun ajouter(lm: ListeMusic){
        this.noms = ArrayList<String>()
        for (memo in lm.nom) // pour chaque memos de la liste
            this.noms?.add(memo.nom)
    }
    private fun getnom(reponce: String){

        var error = false
        var dejaTrouve = false
        for( nom in this.noms!!){
            for( obt in this.obtenue!!) {
                if (obt == reponce ){
                    dejaTrouve = true
                }
            }
            if (!dejaTrouve && nom == reponce){
                Toast.makeText(this, "Bonne Reponce", LENGTH_LONG).show()
                this.obtenue?.add(reponce)
                zoneReponce.setText("")
                this.trouver+=1
                updateProgres(this.trouver)
                break
            }
            else{
                error = true
            }
        }
        if (error){
            Toast.makeText(this, "Il n`y a pas d'accent le jeux est en anglais", LENGTH_LONG).show()
            zoneReponce.setText("")
        }
        else if (dejaTrouve){
            Toast.makeText(this, "l'usager a deja rentrer "+reponce+" donc il ny auras point progres", LENGTH_LONG).show()
            zoneReponce.setText("")
        }
    }
    private fun updateProgres(find : Int){
        if (find == 3){
            Toast.makeText(this, "Bravo", LENGTH_LONG).show()
        }
        progre.progress = find
    }
}














