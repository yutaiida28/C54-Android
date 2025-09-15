package com.example.annexe2

import android.content.Intent
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    lateinit var bouton: Button;
    lateinit var volume: Button;
    lateinit var texteDuree: TextView
    lateinit var texteNom: TextView;

    lateinit var lanceur: ActivityResultLauncher<Intent>;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bouton = findViewById(R.id.bouton);
        volume = findViewById(R.id.volume);
        texteDuree = findViewById(R.id.texteDuree);
        texteNom = findViewById(R.id.texteNom);

        val ec = Ecouteur()
        bouton.setOnClickListener(ec)
        volume.setOnClickListener(ec)


        // création du lanceur de boomerang, objet sera appelé au retour du boomerang dans cette classe
        lanceur = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            CallBackMusic()
        )
    }

    // le retour du boomrang, on revien ici ape=res selection dela musique
    inner class CallBackMusic : ActivityResultCallback<ActivityResult> {
        override fun onActivityResult(result: ActivityResult) {


            val intentRetour: Intent =
                result.data!! // !! : certain que l'intent n'est pas nul car on fait une selection, si null quand même, va générer une exception, j'assume s il y a une erreur dans l'intent
            val uri =
                intentRetour.data!!  // ceetain que j'ai fait une sélection , sinon lance exception
            val resolver =
                contentResolver // objet permettant d'accéder aux données sur le téléphones ( méthodes CRUD ), présente les données sous forme de tables

            //nom du fichier
            val cursor =
                resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
            cursor!!.moveToFirst()
            texteNom.text = cursor!!.getString(0)


            try {
                // ouvrir un flux de données vers l'URI choisi
                val stream = resolver.openInputStream(uri) //c'est un stream d'octets

                texteDuree.text = "durée : ${tempsDeLecture(stream!!)}" //string concatener
            } catch (fnf: Exception) {
                fnf.printStackTrace()
            }

        }

    }

    inner class Ecouteur : View.OnClickListener {
        override fun onClick(v: View?) {
            if (v == bouton) {
                rechercherFichiers()
            }
            else if (v == volume){
                startActivity(Intent(this@MainActivity, SecondActivity::class.java))
            }
        }

    }

    fun rechercherFichiers() {
        // intent vers le téléphone
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("audio/*") // ou text/* // fichiers musciaux
        lanceur.launch(intent) // lance l'intent différemment qu'avec startActivity car on attend un résultat dans cette activité, affiche les fichiers musicaux
    }


    fun tempsDeLecture(chemin: InputStream): String {
        val fis:FileInputStream = chemin as FileInputStream
        val bis:BufferedInputStream = BufferedInputStream(fis)
        var temp : Long = 0
        bis.use { // fonction a hoaut niveaux...
            val debut = System.currentTimeMillis()
            while(bis.read() != -1){}
            val fin = System.currentTimeMillis()
            temp = (fin - debut)
        }
        return temp.toString()
    }
}
