package com.example.annexe1

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AjouterActivity : AppCompatActivity() {
    lateinit var boutonAjouterMemo :Button
    lateinit var boutondate :Button
    lateinit var champMemo :EditText
    lateinit var champDate :TextView
    // la date d'echence sera par defaut le lendemain
    var dateChoisie = LocalDate.now().plusDays(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ajouter)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        boutonAjouterMemo = findViewById(R.id.boutonAjouterMemo)
        boutondate = findViewById(R.id.boutondate)
        champMemo = findViewById(R.id.champMemo)
        champDate = findViewById(R.id.champDate)

        val ec = Ecouteur()
        boutonAjouterMemo.setOnClickListener(ec)
        boutondate.setOnClickListener(ec)

        //simpification interface  fonctionnelle + lambda + lambda come dernier param -> a revoir
//        boutonAjouterMemo.setOnClickListener{
//            var texteMemo = champMemo.text.toString()
//            //verifier si texteMemo est vide
//            if(!texteMemo.isEmpty())
//            {
//                val fos = openFileOutput("memo.text", MODE_APPEND) // mode append est le mode ou on rajoute du contenue sans formater le contenue existent
//                // parce que cest du texte on veux un flux de character, et non un flux de binaire
//                val osw = OutputStreamWriter(fos)
//                val bw = BufferedWriter(osw)
//
//                bw.write(texteMemo)
//                bw.newLine()
//
//                bw.close()
//                champMemo.text.clear()
//
//                finish()  //pour revenir au mainActivity
//
//                //pour aller voir le contenu du fichier memo.txt on vas sur device manager option(3point) puis open in device explorer puis data data le nom du package pui files il devrait y etre
//            }
//        }
    }
    inner class Ecouteur : OnClickListener, OnDateSetListener {
        override  fun onClick(v: View?) {
            if ( v == boutondate){
                val d = DatePickerDialog(this@AjouterActivity)
                d.setOnDateSetListener(this)
                // on veut l'afficher
                d.show()
            }
            else // boutonAjouter
            {   // créer un objet Memo
                SinglrtonMemos.ajouterMemo(Memo(champMemo.text.toString(),dateChoisie ))
                // mettre a jour le fichier de serialisation
                SinglrtonMemos.serialiserListe(this@AjouterActivity)
                champMemo.text.clear()
                champDate.text = ""
                finish()
                // ajouter le memo dans la liste du singleton
            }
        }
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            // aller chercher le message et le localDate
            dateChoisie = LocalDate.of(year,month+1, dayOfMonth) // le month
//            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//            val dateFormater = date.format(formatter)
            // afficher la date
            champDate.setText(dateChoisie.toString())
            // créer un objet Memo
        }

    }
}