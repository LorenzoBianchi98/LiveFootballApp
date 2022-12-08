package com.example.livefootball.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.livefootball.R
import com.example.livefootball.userPrefeNewsId

class EventsAdapter(private var homeTeams: List<String>,
                    private var awayTeams: List<String>,
                    private var results: List<String>) : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemEventsHome: TextView
        val itemEventsAway: TextView
        val itemEventsResult: TextView

        init {
            itemEventsHome = itemView.findViewById(R.id.tv_events_home)
            itemEventsAway = itemView.findViewById(R.id.tv_events_away)
            itemEventsResult =  itemView.findViewById(R.id.tv_events_result)

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_events_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: EventsAdapter.ViewHolder, position: Int) {
        holder.itemEventsHome.text = homeTeams[position]
        holder.itemEventsAway.text = awayTeams[position]
        holder.itemEventsResult.text = results[position]

    }


    override fun getItemCount(): Int {
        return homeTeams.size
    }

}