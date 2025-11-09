package com.example.moviles2primerparcial.data.models.remote


import com.example.moviles2primerparcial.data.models.remote.dto.BreedDTO
import com.example.moviles2primerparcial.data.models.remote.CatApi

/**
 * Repository converts network models into UI DTOs.
 */
class Repository(private val api: CatApi) {

    suspend fun getBreeds(): List<BreedDTO> {
        val nets = api.getBreeds()
        return nets.map { net ->
            BreedDTO(
                id = net.id,
                name = net.name,
                origin = net.origin,
                temperament = net.temperament,
                lifeSpan = net.lifeSpan,
                description = net.description,
                imageUrl = net.image?.url
            )
        }
    }

    /** NUEVO: trae URLs de imágenes para una raza (puede devolver vacío). */
    suspend fun getImages(breedId: String, limit: Int = 1): List<String> {
        return api.searchImages(breedId = breedId, limit = limit)
            .mapNotNull { it.url }
    }
}
