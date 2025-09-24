package com.example.appserialisation

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainActivity : AppCompatActivity() {
    lateinit var bouttonPersonaliser: Button;
    lateinit var salutation: TextView;

    lateinit var launcher: ActivityResultLauncher<Intent>;
    var utilisateur: Utilisateur? = null

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
    //save instant state inactive car dans le manifeste on annule le reciclage du cicle de vie
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
//            utilisateur = savedInstanceState?.getSerializable("utilisateur", Utilisateur::class.java)
//        else
//            utilisateur = savedInstanceState?.getSerializable("utilisateur") as Utilisateur?
        // ?: elvis --> ce que on veut si le membre de gauche est null
//        salutation.text = "Bonjour  ${utilisateur?.prenom?:" " } ${utilisateur?.nom?:" " }"
        utilisateur = deserialiserUtilisateur()
        if (utilisateur != null ){
            salutation.text = "Bonjour " + utilisateur!!.prenom+" " + utilisateur!!.nom
        }
    }
    fun deserialiserUtilisateur() : Utilisateur?{
        try {
            // récupérer objets sérialisés
            val fis = openFileInput("user.ser")
            val ois = ObjectInputStream(fis)
            ois.use {
                utilisateur = ois.readObject() as Utilisateur
            }
        }
        catch (exception : FileNotFoundException) {
            Toast.makeText(this, "pas de fichier de serialisation", Toast.LENGTH_SHORT).show()
        }
        return utilisateur
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("utilisateur",utilisateur)
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        // fair des val pour chaque objet si pas deja recuperer
        // si il est deja une data class fair un var

        val fos = openFileOutput("user.ser", MODE_PRIVATE) // MODE_PRIVATE replace data in file when written
        val oos = ObjectOutputStream(fos)
        oos.use { //ici c'est un lambda de haute fonction donc pas de () dailleur on se serre de use pour ne pas avoir close a la fin  voir si on est capable de rajouter des donner ex il ya un liste puis attribuer des resultat a la fin
            oos.writeObject(utilisateur)
        }

    }

    inner class CallBackUtilisateur : ActivityResultCallback<ActivityResult>{
        override fun onActivityResult(result: ActivityResult) {
            if (result.resultCode == Activity.RESULT_OK) {
                // Récupérer l'utilisateur
                utilisateur = result.data?.getSerializableExtra("utilisateur") as? Utilisateur

                // Mettre à jour la salutation
                utilisateur?.let {
                    salutation.text = "Bonjour ${it.prenom} ${it.nom} !"
                }
            }
        }
    }

}