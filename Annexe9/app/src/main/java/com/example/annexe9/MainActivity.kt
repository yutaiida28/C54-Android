package com.example.annexe9

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    lateinit var page : ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        page = findViewById(R.id.pager)
        page.orientation = ViewPager2.ORIENTATION_VERTICAL
        page.setPageTransformer(MonPageTransformer())
        val adapter = myAdapter(this)
        page.adapter = adapter
    }
    inner class MonPageTransformer : ViewPager2.PageTransformer{
        override fun transformPage(page: View, position: Float) {
            page.setAlpha(0.9f - abs(position.toFloat()) * 0.5f) // Fades out and in
        }
    }
    inner class myAdapter (fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            // determiner quel fragment afficher en fonction de la position
            when( position){
                0 -> return BlankFragment1()
                1 -> return BlankFragment2()
                else -> return BlankFragment3()
            }
        }
    }
}