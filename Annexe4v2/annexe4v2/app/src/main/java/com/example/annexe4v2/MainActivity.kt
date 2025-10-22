package com.example.annexe4v2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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

class MainActivity : AppCompatActivity() {
    lateinit var bouttonPersonaliser: Button;
    lateinit var salutation: TextView;

    lateinit var launcher: ActivityResultLauncher<Intent>;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bouttonPersonaliser = findViewById(R.id.infoUtilisaeurBtn);
        salutation = findViewById(R.id.salutation);

        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            CallBackUtilisateur()
        )
        bouttonPersonaliser.setOnClickListener {
            val intent = Intent(this, IdentificationActivity::class.java)
            launcher.launch(intent)
        }
    }
    ovveride fun onSave


    inner class CallBackUtilisateur : ActivityResultCallback<ActivityResult>{
        override fun onActivityResult(result: ActivityResult) {
            if (result.resultCode == Activity.RESULT_OK) {
                // Récupérer l'utilisateur
                val utilisateur = result.data?.getSerializableExtra("identifiant") as? Utilisateur

                // Mettre à jour la salutation
                utilisateur?.let {
                    salutation.text = "Bonjour ${it.prenom} ${it.nom} !"
                }
            }
        }
    }

}