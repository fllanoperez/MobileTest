package com.example.marvel.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.databinding.CharacterListItemBinding
import com.example.marvel.views.models.CharacterUIModel

class CharacterListAdapter(private var items: List<CharacterUIModel>, private val callback: ChararacterListInterface): RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder.from(parent.context, callback)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newData: List<CharacterUIModel>, itemsInserted: Int){
        items = newData
        notifyItemRangeInserted(newData.size - itemsInserted, itemsInserted)
    }

    class CharacterViewHolder(private val binding: CharacterListItemBinding, private val callback: ChararacterListInterface) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CharacterUIModel) {
            binding.model = model
            binding.root.setOnClickListener {
                callback.onCharacterClick(model)
            }
        }

        companion object {
            fun from(context: Context, callback: ChararacterListInterface): CharacterViewHolder {
                return CharacterViewHolder(CharacterListItemBinding.inflate(LayoutInflater.from(context)), callback)
            }
        }
    }

    interface ChararacterListInterface {
        fun onCharacterClick(character: CharacterUIModel)
    }
}