package com.example.annexe1
//pour faire un singleton on change le mot class ppar objet
object SinglrtonMemos {
    var liste:ArrayList<Memo> = ArrayList()

    fun ajouterMemo (memo:Memo)
    {
        liste.add(memo)
    }
}