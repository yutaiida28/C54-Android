package com.example.annexe8b

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.BounceInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AnimationZoom : AppCompatActivity() {
    lateinit var titre : TextView
    lateinit var buttonA: Button
    lateinit var buttonQ: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_animation_zoom)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        titre = findViewById(R.id.titre)
        buttonA = findViewById(R.id.buttonA)
        buttonQ = findViewById(R.id.buttonQ)

        var scaleOnX= ObjectAnimator.ofFloat(titre,TextView.SCALE_X,14f)
        var scaleOnY= ObjectAnimator.ofFloat(titre,TextView.SCALE_Y,14f)
        var scaleOnAlpha= ObjectAnimator.ofFloat(titre,TextView.ALPHA, 1f)
        var set = AnimatorSet()
        set.setDuration(1200)
        set.interpolator = BounceInterpolator()
        set.playTogether(scaleOnX,scaleOnY,scaleOnAlpha)

        buttonA.setOnClickListener {
            set.start()
        }
        buttonQ.setOnClickListener {
            finish()
        }
    }
}