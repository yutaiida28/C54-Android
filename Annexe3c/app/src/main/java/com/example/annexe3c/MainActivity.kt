package com.example.annexe3c

import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainActivity : AppCompatActivity() {
    // optimisation avec getchildren
    lateinit var dent1: LinearLayout

    lateinit var dent2: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dent1 = findViewById(R.id.dent1)
        dent2 = findViewById(R.id.dent2)

        try {
            // récupérer objets sérialisés
            val fis = openFileInput("dents.ser")
            val ois = ObjectInputStream(fis)
            ois.use {
                var dentUn: Dent = ois.readObject() as Dent
                var dentDeux: Dent = ois.readObject() as Dent

                (dent1.getChildAt(0) as EditText).setText(dentUn.numDent.toString())
                (dent1.getChildAt(1) as CheckBox).setChecked(dentUn.traitementCanal)
                (dent1.getChildAt(2) as EditText).setText(dentUn.notes)

                (dent2.getChildAt(0) as EditText).setText(dentDeux.numDent.toString())
                (dent2.getChildAt(1) as CheckBox).setChecked(dentDeux.traitementCanal)
                (dent2.getChildAt(2) as EditText).setText(dentDeux.notes)
            }
        }
        catch (f: FileNotFoundException) {
            f.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()

        val noDent1 = (dent1.get(0) as EditText).text.toString().toInt()
        val trai1 = (dent1.get(1) as CheckBox).isChecked
        val notes1 = (dent1.get(2) as EditText).text.toString()

        var dentUn = Dent(noDent1, trai1, notes1)

        val noDent2 = (dent2.get(0) as EditText).text.toString().toInt()
        val trai2 = (dent2.get(1) as CheckBox).isChecked
        val notes2 = (dent2.get(2) as EditText).text.toString()

        var dentDeux = Dent(noDent2, trai2, notes2)

        //sérialisation des objets Dent
        val fos = openFileOutput("dents.ser", MODE_PRIVATE)
        val oos = ObjectOutputStream(fos)
        oos.use {
        }
    }
}