package com.example.marvel.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.databinding.ComicListItemBinding
import com.example.marvel.views.models.ComicUIModel

class ComicListAdapter(private var items: List<ComicUIModel>): RecyclerView.Adapter<ComicListAdapter.ComicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        return ComicViewHolder.from(parent.context)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ComicViewHolder(private val binding: ComicListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ComicUIModel) {
            binding.model = model
        }

        companion object {
            fun from(context: Context): ComicViewHolder {
                return ComicViewHolder(ComicListItemBinding.inflate(LayoutInflater.from(context)))
            }
        }
    }
}