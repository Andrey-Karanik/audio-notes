package com.andreykaranik.audionotes

import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnDragListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator

class AudioNoteItemAdapter(private val audioNotes: List<AudioNote>) : RecyclerView.Adapter<AudioNoteItemAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val audioProgressIndicator: LinearProgressIndicator

        init {

            textView = view.findViewById(R.id.nameTextView)
            audioProgressIndicator = view.findViewById(R.id.audioProgressIndicator)

            view.setOnTouchListener(object : OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                    if (event?.action == MotionEvent.ACTION_DOWN) {
                        Log.println(Log.ERROR, "a", "DOWN")
                    }

                    if (event?.action == MotionEvent.ACTION_UP) {
                        Log.println(Log.ERROR, "a", "UP")
                    }


                    event?.let { audioProgressIndicator.progress = (event.x / view.width * 100).toInt() }

                    Log.println(Log.ERROR, "a", (event?.x?.div(view.width)).toString())

                    return true
                }

            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.audio_note_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = audioNotes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = audioNotes[position].name
    }
}