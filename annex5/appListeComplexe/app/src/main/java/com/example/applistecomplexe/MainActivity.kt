package com.example.applistecomplexe

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var listMusic: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listMusic = findViewById(R.id.appListe)
        val adap = SimpleAdapter(this,remplirArrayListe(), R.layout.music_item,arrayOf("position","nom","date","image"),intArrayOf(R.id.textPos,R.id.textNom,R.id.textDate,R.id.imageView));
        listMusic.setAdapter(adap);
    //        val list=ArrayList<HashMap<String,Any>>()

        val ec = Ecouteur()
//        listMusic.setOnItemClickListener(ec) // methode java
        listMusic.onItemClickListener = ec // methode kotlin
    }

    inner class Ecouteur : OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            var choisie = listMusic.getItemAtPosition(position) as HashMap<String, Any>
//            var nomMusic =  choisie["nom"]  as String
//            Toast.makeText(this@MainActivity,nomMusic,Toast.LENGTH_LONG).show()

            val linearLayout = view as LinearLayout
            val textView = linearLayout.findViewById<TextView>(R.id.textNom)
            Toast.makeText(this@MainActivity,textView.text.toString(),Toast.LENGTH_LONG).show()
        }

    }
    fun remplirArrayListe () : ArrayList<HashMap<String,Any>>{
        var listeDonnes =ArrayList<HashMap<String,Any>>()
        var chanson = HashMap<String,Any>()

        chanson.put("position", 3) // chanson["position"]= 3 meme que enhaut
        chanson["nom"]= "Touch Me"
        chanson["date"]= "22/03/86"
        chanson.put ("image", R.drawable.touchme)

        listeDonnes.add(chanson)

        chanson = HashMap() //fair un nouveaux hachmap car il vas l'ecraser sinon
        chanson.put("position", 8)
        chanson["nom"]= "Nothings gonna stop me now"
        chanson["date"]= "22/03/86"
        chanson.put ("image", R.drawable.nothing)
        listeDonnes.add(chanson)

        chanson = HashMap() //fair un nouveaux hachmap car il vas l'ecraser sinon
        chanson.put("position", 31)
        chanson["nom"]= "santa maria"
        chanson["date"]= "22/03/1992"
        chanson.put ("image", R.drawable.santamaria)
        listeDonnes.add(chanson)

        chanson = HashMap() //fair un nouveaux hachmap car il vas l'ecraser sinon
        chanson.put("position", 108)
        chanson["nom"]= "Hot body"
        chanson["date"]= "22/03/86"
        chanson.put ("image", R.drawable.hotboy)
        listeDonnes.add(chanson)

        return listeDonnes
    }
}