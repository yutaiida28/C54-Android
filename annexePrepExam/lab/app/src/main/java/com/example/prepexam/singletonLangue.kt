package com.example.prepexam

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

object singletonLangue {
    private var liste: ArrayList<Langue> = ArrayList()

    fun lireTxt(context: Context): ArrayList<Langue> {
        // Clear list to avoid duplicates if called multiple times
        liste.clear()

        // Open file from raw resources folder
        val inputStream = context.resources.openRawResource(R.raw.langage)
        val reader = BufferedReader(InputStreamReader(inputStream))

        // Skip the header line
        reader.readLine()

        // Read each line of the CSV file
        reader.forEachLine { ligne ->
            if (ligne.trim().isNotEmpty()) {
                // Split by comma to get individual values
                val parties = ligne.trim().split(",")

                // Extract values (CSV format: name,2024,2019,2012,)
                val langue = parties[0]
                val en24 = parties[1].toInt()
                val en19 = parties[2].toInt()
                val en12 = parties[3].toInt()

                // Create Langue object with correct parameter order
                liste.add(Langue(langue, en12, en19, en24))
            }
        }

        reader.close()
        return liste
    }

    // Count languages that didn't exist in 2012 (value = 99)
    fun compteurNonExistant2012(): Int {
        return liste.count { it.en12 == 99 }
    }
}