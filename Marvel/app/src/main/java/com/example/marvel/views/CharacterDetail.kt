package com.example.marvel.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.marvel.R
import com.example.marvel.databinding.CharacterListItemBinding
import com.example.marvel.databinding.FragmentCharacterDetailBinding
import com.example.marvel.databinding.FragmentCharacterListBinding


class CharacterDetail: Fragment() {

    var binding: FragmentCharacterDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_character_detail, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments?.let { CharacterDetailArgs.fromBundle(it) }
        binding?.model = args?.characterModel
    }
}