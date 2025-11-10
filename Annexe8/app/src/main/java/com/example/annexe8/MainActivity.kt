package com.example.annexe8

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
//    lateinit var button : Button
    lateinit var element : View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        button = findViewById(R.id.button)
        element =  findViewById(R.id.floorZone)
        var anim = ObjectAnimator.ofFloat( element, View.TRANSLATION_Y, 0f )        // bouton va se déplacer de x-500px à x = 1070px
        anim.duration = 2000 // la duration 300 milisecond
//        anim.interpolator = BounceInterpolator()

        var anim2 = ObjectAnimator.ofFloat( element, View. ROTATION_X,360f)
        anim2.duration = 1000
        anim2. interpolator = BounceInterpolator()

        var animatorSet = AnimatorSet()
        animatorSet.playTogether(anim)

        element.setOnClickListener{
            animatorSet.start()
            animatorSet.ons
        }

    }
}