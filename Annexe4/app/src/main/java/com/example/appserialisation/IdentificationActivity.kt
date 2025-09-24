package com.example.appserialisation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class IdentificationActivity : AppCompatActivity() {
    lateinit var confirmer: Button;
    lateinit var nom: EditText;
    lateinit var prenom: EditText;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_identification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        confirmer = findViewById(R.id.button2)  // Assure-toi que l'ID correspond
        nom = findViewById(R.id.nom)
        prenom = findViewById(R.id.prenom)

        confirmer.setOnClickListener {
            // Créer l'objet Utilisateur
            val utilisateur = Utilisateur(
                prenom.text.toString(),
                nom.text.toString()
            )

            // Créer l'intent de retour
            val intentRetour = Intent()  // Pas de destination !
            intentRetour.putExtra("utilisateur", utilisateur)

            // Définir le résultat et terminer
            setResult(Activity.RESULT_OK, intentRetour)
            finish()  // Retour à MainActivity
        }
    }
}