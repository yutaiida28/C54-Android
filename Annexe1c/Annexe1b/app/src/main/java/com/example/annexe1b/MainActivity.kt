package com.example.annexe1b

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.Scanner
import java.util.Vector


// Chere Yuta. Si je meurs je te legue tout. Ce document est utilisable legalement.
// Je te legue aussi mon chat.
class MainActivity : AppCompatActivity() {
    lateinit var NbLigne: TextView
    lateinit var NbCaract: TextView
    lateinit var NbDeC: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        NbLigne = findViewById(R.id.nbLigne)
        NbCaract = findViewById(R.id.nbCaract)
        NbDeC = findViewById(R.id.nbDeC)
        NbLigne.setText(lireText("lignes").toString())
        NbCaract.setText(lireText("caracteres").toString())
        NbDeC.setText(lireText("c").toString())
    }
    fun lirePlanetes(): Int {
        val vector = Vector<Planet>()

        openFileInput("planetes.txt").bufferedReader().use { reader ->
            reader.forEachLine { ligne ->
                if (ligne.trim().isNotEmpty()) {
                    val parties = ligne.trim().split("\\s+".toRegex())
                    if (parties.size >= 2) {
                        val nom = parties[0]
                        val satellites = parties[1].toInt()
                        vector.add(Planet(nom, satellites))
                    }
                }
            }
        }

        return vector.size
    }
    fun lirePlanetes2(): Int {
        val vector = Vector<Planet>()

        Scanner(openFileInput("planetes.txt")).use { scanner ->
            while (scanner.hasNextLine()) {
                val ligne = scanner.nextLine().trim()
                if (ligne.isNotEmpty()) {
                    val scannerLigne = Scanner(ligne)
                    val nom = scannerLigne.next()
                    val satellites = scannerLigne.nextInt()
                    vector.add(Planet(nom, satellites))
                    scannerLigne.close()
                }
            }
        }

        return vector.size
    }
    private fun lireText(input: String): Int
    {
        var conteur = 0
        try {
            val fis = openFileInput("text.txt")
            //val fis: FileInputStream =  openFileInput("text.txt") // version java
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)

            if (input == "lignes")
            {
                br.use {
                    conteur =  br.readLines().size
                }
//                br.use{
//                    for(line in br.lines())
//                        conteur ++
//                }
            }
            else if (input == "caracteres")
            {
                br.use {
                    for (ligne in br.readLines()) //pas recomender pour des huge file 2g +
                        conteur += ligne.length
                }
            }
            else if (input == "c")
            {
                br.use {
                    for (ligne in br.readLines())
                        for (i in ligne)
                            if (i == 'c' || i == 'C')
                                conteur += 1

                    //conteur = br.readLines().sumOf { ligne -> ligne.length} //plus que on met un lambda dans les braquette on ne met pas les parenthese
                }
            }
        }
        catch ( fnfe : FileNotFoundException)
        {
            fnfe.printStackTrace()
        }
        return conteur
    }

}