package com.example.marvel.views.models

import java.io.Serializable

data class CharacterUIModel(
        val imageURL: String,
        val name: String,
        val description: String
) :Serializable
