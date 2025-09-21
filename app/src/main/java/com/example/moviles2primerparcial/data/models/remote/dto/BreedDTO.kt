package com.example.moviles2primerparcial.data.remote.dto

import com.squareup.moshi.Json

data class BreedDTO(
    val id: String? = null,
    val name: String? = null,
    val origin: String? = null,
    val temperament: String? = null,
    @Json(name = "life_span") val lifeSpan: String? = null,
    val description: String? = null
)

