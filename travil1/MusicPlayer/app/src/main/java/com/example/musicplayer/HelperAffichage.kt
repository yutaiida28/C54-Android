package com.example.musicplayer

import android.content.Context
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import com.bumptech.glide.Glide

//helper pour l'affichage
class HelperAffichage {

//fair le hasmaplist pour les insertion dans les listeview
    fun createMusicHashMapList(musicList: List<Music>): ArrayList<HashMap<String, String>> {
        val remplir = ArrayList<HashMap<String, String>>()
        for (music in musicList) {
            val temp = HashMap<String, String>()
            temp["title"] = music.title
            temp["sec"] = music.duration.toString()
            temp["image"] = music.image.toString()
            remplir.add(temp)
        }
        return remplir
    }

//simpleadapter modifier
    fun createMusicAdapter(
        context: Context,
        musicList: List<Music>,
        layout: Int,
        from: Array<String>,
        to: IntArray
    ): SimpleAdapter {
        val remplir = createMusicHashMapList(musicList)

        val adapter = object : SimpleAdapter(context, remplir, layout, from, to) {
            override fun setViewImage(v: ImageView, value: String) {
                Glide.with(v.context)
                    .load(value)
                    .into(v)
            }
        }

        // Configure le ViewBinder pour le titre défilant
        adapter.viewBinder = SimpleAdapter.ViewBinder { view, data, textRepresentation ->
            if (view.id == R.id.name && view is TextView) {
                view.text = textRepresentation
                view.isSelected = true
                true
            } else {
                false
            }
        }

        return adapter
    }

//   configure une listView avec la liste de musique complete
//   applique l'adapter et configure le click listener

    fun setupMusicListView(
        context: Context,
        listView: ListView,
        musicList: List<Music>,
        onItemClick: (position: Int, music: Music) -> Unit
    ) {
        val adapter = createMusicAdapter(
            context,
            musicList,
            R.layout.layout,
            arrayOf("title", "sec", "image"),
            intArrayOf(R.id.name, R.id.length, R.id.imageView2)
        )

        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
            onItemClick(position, musicList[position])
        }
    }
}