package com.example.moviles2primerparcial.ui.breeddetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.moviles2primerparcial.R
import com.example.moviles2primerparcial.data.remote.ServiceLocator
import com.example.moviles2primerparcial.databinding.FragmentBreedDetailBinding
import kotlinx.coroutines.launch

/**
 * Shows details for a single breed:
 * - Receives the required fields as fragment arguments.
 * - Fetches an image for the breed (if available) using a lightweight API call.
 * - Configures a toolbar with a back navigation icon.
 */
class BreedDetailFragment : Fragment() {

    private var _binding: FragmentBreedDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_BREED_ID = "breedId"
        const val ARG_BREED_NAME = "breedName"
        const val ARG_DESCRIPTION = "description"
        const val ARG_TEMPERAMENT = "temperament"
        const val ARG_LIFE_SPAN = "lifeSpan"
        const val ARG_ORIGIN = "origin"
    }

    private val breedId: String by lazy { requireArguments().getString(ARG_BREED_ID).orEmpty() }
    private val breedName: String by lazy { requireArguments().getString(ARG_BREED_NAME).orEmpty() }
    private val description: String by lazy { requireArguments().getString(ARG_DESCRIPTION).orEmpty() }
    private val temperament: String by lazy { requireArguments().getString(ARG_TEMPERAMENT).orEmpty() }
    private val lifeSpan: String by lazy { requireArguments().getString(ARG_LIFE_SPAN).orEmpty() }
    private val origin: String by lazy { requireArguments().getString(ARG_ORIGIN).orEmpty() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    /**
     * Shows details for a single breed:
     * - Receives the required fields as fragment arguments.
     * - Fetches an image for the breed (if available) using a lightweight API call.
     * - Configures a toolbar with a back navigation icon.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the toolbar title and a manual "up" navigation since we are not using an ActionBar.
        binding.toolbar.title = breedName
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Bind basic text fields passed from the previous screen via arguments.
        binding.tvName.text = breedName
        binding.tvDescription.text = description
        binding.tvTemperament.text = getString(R.string.breed_temperament, temperament)
        binding.tvLifeSpan.text = getString(R.string.breed_lifespan, lifeSpan)
        binding.tvOrigin.text = getString(R.string.breed_origin, origin)

        // Accessibility: describe the image for screen readers.
        binding.ivPhoto.contentDescription = getString(R.string.cd_breed_photo, breedName)

        // Load one image for this breed.
        loadImage()
    }

    /**
     * Requests one image for this breed.
     * UI contract:
     * - While loading: show spinner, hide error
     * - On success: hide spinner and load image with crossfade + gray placeholder
     * - On empty/failed: hide spinner and show a short error message (and a Toast)
     */
    private fun loadImage() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val images = ServiceLocator.api.getImages(breedId, 1) // one image per breed
                val url = images.firstOrNull()?.url

                binding.progressBar.visibility = View.GONE

                if (url.isNullOrBlank()) {
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = getString(R.string.image_not_available)
                } else {
                    // Keep the gray loading "bar" as placeholder to make progress obvious on slow networks.
                    binding.ivPhoto.load(url) {
                        crossfade(true)
                        placeholder(android.R.drawable.progress_indeterminate_horizontal)
                        error(android.R.drawable.ic_menu_report_image)
                    }
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = getString(R.string.error_generic)
                Toast.makeText(
                    requireContext(),
                    "Error: ${e.message ?: "unknown"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

