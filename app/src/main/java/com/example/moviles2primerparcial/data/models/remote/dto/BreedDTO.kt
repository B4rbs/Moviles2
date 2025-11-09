package com.example.moviles2primerparcial.data.models.remote.dto

data class BreedDTO(
    val id: String,
    val name: String,
    val origin: String,
    val temperament: String?,
    val lifeSpan: String?,
    val description: String?,
    val imageUrl: String?
)

