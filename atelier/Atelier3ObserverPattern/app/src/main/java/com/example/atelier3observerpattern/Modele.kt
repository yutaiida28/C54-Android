package com.example.atelier3observerpattern

import android.os.CountDownTimer
import android.os.Handler

class Modele : Sujet {
    private var valeur = 0
    private var obs: ObservateurChangement? = null // c'est un observateur, il pourrait en avoir plusieurs

    // constructeur unique
    init {
        genererChangementValeur()
    }


    // méthode générant un changement de la valeur du sujet,
    // on utilise un aitre thread pour ne pas bloquer l'affichage de l'activité et
    // faire en sorte ainsi que l'observateur du sujet soit null
    // le changement surviendra dans 5000 millisecondes
    fun genererChangementValeur() {
        val handler = Handler()
        handler.postDelayed(object : Thread() {
            var compteur: Int = 0
            override fun run() {
                println(currentThread().name)
                compteur++
                setValeur(compteur) // changement de valeur
                handler.postDelayed(this, 2000)
            }
        }, 5000) // la méthode run s'exécutera après un délai de 5000 ms
//      val timer = MonTimer()
//        timer.start()
//
      }

//    inner class MonTimer : CountDownTimer(15000, 5000)
//    {
//        var compteur = 0;
//        override fun onTick(millisUntilFinished: Long) {
//                        setValeur(compteur++)
//        }
//
//        override fun onFinish() {
//
//        }
//
//    }

    fun getValeur(): Int {
        return valeur
    }


    fun setValeur(valeur: Int) {
        this.valeur = valeur
        //changement de l'état du sujet, avertir les observateurs
        avertirObservateurs()
    }


    // méthodes de l'interface Sujet
    override fun ajouterObservateur(obs: ObservateurChangement) {
        this.obs = obs
    }

    override fun enleverObservateur(obs: ObservateurChangement) {

        this.obs = null // il y en a un seul
    }

    override fun avertirObservateurs() {
        obs!!.changement(valeur) // important
    }
}