package com.kaliciak.turtlebattle.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kaliciak.turtlebattle.databinding.FoundGameListItemBinding

class FoundGamesRecViewAdapter:
RecyclerView.Adapter<FoundGamesRecViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FoundGameListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = 5

    inner class ViewHolder(val binding: FoundGameListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val gameTitle: TextView = binding.gameTitle

        fun bind(position: Int) {
            gameTitle.text = "TEST $position"
        }
    }

}