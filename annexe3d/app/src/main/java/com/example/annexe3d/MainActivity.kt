package com.example.annexe3d

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.i("cycle", "onCreate");
        println("onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.i("cycle", "onStart");
    }

    override fun onResume() {
        super.onResume()
        Log.i("cycle", "onResume");
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("cycle", "onRestart");
    }

    override fun onPause() {
        super.onPause()
        Log.i("cycle", "onPause");
    }

    override fun onStop() {
        super.onStop()
        Log.i("cycle", "onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("cycle", "onDestroy");
    }
}