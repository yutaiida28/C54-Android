package com.example.annexe8b

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

class AnimationSplash : AppCompatActivity() {
    lateinit var buttonA: Button
    lateinit var buttonQ: Button
    lateinit var soleil: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_animation_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        soleil = findViewById(R.id.view)
        buttonA = findViewById(R.id.buttonA)
        buttonQ = findViewById(R.id.buttonQ)

        var scaleOnX = ObjectAnimator.ofFloat(soleil,View.SCALE_X,14f)
        var scaleOnY = ObjectAnimator.ofFloat(soleil,View.SCALE_Y,14f)

        var set = AnimatorSet()
        set.setDuration(1200)
        set.playTogether(scaleOnX,scaleOnY)

        buttonA.setOnClickListener {
            set.start()
        }
        buttonQ.setOnClickListener {
            finish()
        }
    }
}