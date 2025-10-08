package com.example.atelier3observerpattern

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class ObservateurActivity : AppCompatActivity(), ObservateurChangement {

    lateinit var texte: TextView
    var leModele: Sujet? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View>(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        texte = findViewById<TextView>(R.id.texte)
    }

    override fun onStart() {
        super.onStart()
        leModele = Modele()
        (leModele as Modele).ajouterObservateur(this) // on ajouter l'observateur ( l'activité ) au modèle ( le sujet )
    }

    override fun onDestroy() {
        super.onDestroy()
        leModele!!.enleverObservateur(this)
    }
    override fun changement(valeur: Int) {
        texte.text = "nouvelle valeur : $valeur" // le modèle a changé son état !
    }


}