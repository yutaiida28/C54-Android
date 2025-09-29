package com.example.prepexam

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    lateinit var nonUtiliser12: TextView
    lateinit var derniereOuverture: TextView

    // File name for serialization
    private val DATETIME_FILE = "derniere_utilisation.ser"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize TextViews - make sure these IDs match your layout file
        nonUtiliser12 = findViewById(R.id.tvNonUtilise)
        derniereOuverture = findViewById(R.id.tvDerniereOuverture)

        // Load language data from CSV file
        singletonLangue.lireTxt(this)

        // Count and display languages that didn't exist in 2012
        val count = singletonLangue.compteurNonExistant2012()
        nonUtiliser12.text = "Nombre de langages non existants en 2012: $count"

        // Load and display last usage time
        chargerDerniereUtilisation()
    }

    override fun onStop() {
        super.onStop()
        // Save current date/time when app goes to background
        sauvegarderDateHeure()
    }

    // Save current DateTime using serialization
    private fun sauvegarderDateHeure() {
        try {
            // Create ObjectOutputStream to serialize objects
            val fos = openFileOutput(DATETIME_FILE, MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)

            // Create current DateTime and write it to file
            val maintenant = LocalDateTime.now()
            oos.writeObject(maintenant)

            // Close streams
            oos.close()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Load previously saved DateTime from serialized file
    private fun chargerDerniereUtilisation() {
        try {
            // Try to open the serialized file
            val fis = openFileInput(DATETIME_FILE)
            val ois = ObjectInputStream(fis)

            // Read the LocalDateTime object
            val derniereDate = ois.readObject() as LocalDateTime

            // Format the date for display (adjust format as needed)
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'à' HH'h'mm")
            derniereOuverture.text = "Dernière utilisation: ${derniereDate.format(formatter)}"

            // Close streams
            ois.close()
            fis.close()
        } catch (e: FileNotFoundException) {
            // First time using the app - no previous file exists
            derniereOuverture.text = "Première utilisation de l'application"
        } catch (e: Exception) {
            e.printStackTrace()
            derniereOuverture.text = "Erreur lors de la lecture"
        }
    }
}