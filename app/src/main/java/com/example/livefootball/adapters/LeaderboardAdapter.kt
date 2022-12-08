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

class LeaderboardAdapter(private var teams: List<String>,
                         private var points: List<String>) : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemPosition: TextView
        val itemTeam: TextView
        val itemPoints: TextView


        init {
            itemPosition = itemView.findViewById(R.id.tv_lb_pos)
            itemTeam = itemView.findViewById(R.id.tv_lb_team)
            itemPoints =  itemView.findViewById(R.id.tv_lb_points)

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_leaderboard, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: LeaderboardAdapter.ViewHolder, position: Int) {
        holder.itemPosition.text = (position+1).toString()
        holder.itemTeam.text = teams[position]
        holder.itemPoints.text = points[position]
    }

    override fun getItemCount(): Int {
        return teams.size
    }

}