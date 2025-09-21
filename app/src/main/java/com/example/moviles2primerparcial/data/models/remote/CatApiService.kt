package com.example.moviles2primerparcial.data.remote

import com.example.moviles2primerparcial.data.remote.dto.BreedDTO
import com.example.moviles2primerparcial.data.remote.dto.CatImageDTO
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for TheCatAPI endpoints used.
 * Minimal info: we only need the breeds list and 1 image per breed.
 */
interface CatApiService {

    // Returns the entire list of cat breeds (id, name, origin, etc.).
    // Example: GET https://api.thecatapi.com/v1/breeds
    @GET("breeds")
    suspend fun getBreeds(): List<BreedDTO>

    // Returns up to [limit] images for a given breed id.
    // Example: GET https://api.thecatapi.com/v1/images/search?breed_ids=abys&limit=1
    @GET("images/search")
    suspend fun getImages(
        @Query("breed_ids") breedId: String,
        @Query("limit") limit: Int = 1
    ): List<CatImageDTO>
}
