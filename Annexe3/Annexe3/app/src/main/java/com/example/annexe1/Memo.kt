package com.example.annexe1

import org.xml.sax.Locator
import java.io.Serializable
import java.time.LocalDate

data class Memo (var message:String, var echance: LocalDate ) :Serializable {

}