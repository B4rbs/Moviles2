package com.example.moviles2primerparcial.data.models.remote

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface que define los endpoints de TheCatAPI
 * y modelos de red tal como vienen del JSON.
 */
interface CatApi {
    @GET("breeds")
    suspend fun getBreeds(): List<BreedNet>

    // NUEVO: buscar imágenes por raza (devuelve lista de resultados con URL)
    @GET("images/search")
    suspend fun searchImages(
        @Query("breed_id") breedId: String,
        @Query("limit") limit: Int = 1
    ): List<ImageSearchNet>
}

/** Modelos de red (Net) -> se mapean luego a BreedDTO en Repository */
data class BreedNet(
    val id: String,
    val name: String,
    val origin: String,
    val temperament: String?,
    @Json(name = "life_span") val lifeSpan: String?,
    val description: String?,
    val image: ImageNet?
)

data class ImageNet(
    val url: String?
)

/** NUEVO: respuesta de /images/search */
data class ImageSearchNet(
    val url: String?
)
