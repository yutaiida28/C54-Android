package com.example.atelier3observerpattern

interface Sujet {
   // pourrait metre un boolean pour chan...?
   // plusque c'est un interface il faut que je limplement quelque part don ici le modele
   fun ajouterObservateur(o:ObservateurChangement)
   fun enleverObservateur(o:ObservateurChangement)
   fun avertirObservateurs()
}
