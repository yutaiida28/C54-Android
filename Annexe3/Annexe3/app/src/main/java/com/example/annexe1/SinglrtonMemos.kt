package com.example.annexe1

import android.content.Context
import androidx.transition.Visibility.Mode
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.Objects

//pour faire un singleton on change le mot class ppar objet
object SinglrtonMemos {
    private var liste:ArrayList<Memo> = ArrayList()

    fun ajouterMemo (memo:Memo)
    {
        liste.add(memo)
    }

    fun getListe() :ArrayList<Memo>
    {
        return liste
    }

    fun serialiserListe( contexte: Context)
    {
        val fos = contexte.openFileOutput ( "serialiastion.ser", Context.MODE_PRIVATE)  // parceque on nest pas dans une activité on vas le metre sur contexte
        val oos = ObjectOutputStream(fos) // tempon special pour objets
        oos.use{oos.writeObject(liste)}
    }
    fun deSerialiserListe(contexte: Context): ArrayList<Memo>
    {
        if (liste.isEmpty())
        {
            val fis = contexte.openFileInput("serialisation.ser")
            val ois = ObjectInputStream(fis)
            ois.use { liste = ois.readObject() as ArrayList<Memo> }
        }
        return ArrayList(liste) // retourne un copy de notre liste donc la liste original est proteger
    }
}