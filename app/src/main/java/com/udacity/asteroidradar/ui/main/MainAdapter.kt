package com.udacity.asteroidradar.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding

class MainAdapter(private val onClickListener: OnClickListener): ListAdapter<Asteroid, MainAdapter.ViewHolder>(AsteroidDiffCallBack) {

    class ViewHolder(private val binding: AsteroidListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Asteroid) {
            binding.apply {
                codeName.text = item.codename
                approachDate.text = item.closeApproachDate
                asteroid = item
            }
        }
    }

    companion object AsteroidDiffCallBack: DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AsteroidListItemBinding.inflate(LayoutInflater.from(parent.context))
        // match_parent width does not work in RecyclerView list items
        // https://stackoverflow.com/a/30692398
        binding.root.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
        holder.bind(item)
    }

    class OnClickListener(private val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}