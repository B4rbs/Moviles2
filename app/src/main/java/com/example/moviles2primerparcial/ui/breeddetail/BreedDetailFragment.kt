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
import com.example.moviles2primerparcial.data.models.remote.ServiceLocator
import com.example.moviles2primerparcial.databinding.FragmentBreedDetailBinding
import kotlinx.coroutines.launch

/**
 * Displays detailed information for a single cat breed.
 * It receives arguments from the list screen and optionally fetches an image for the breed.
 */
class BreedDetailFragment : Fragment() {

    // ViewBinding holder; cleared in onDestroyView to avoid memory leaks
    private var _binding: FragmentBreedDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the view using ViewBinding
        _binding = FragmentBreedDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // --- Read arguments coming from the list screen (primitive values) ---
        val breedId = requireArguments().getString("breedId").orEmpty()
        val breedName = requireArguments().getString("breedName").orEmpty()
        val description = requireArguments().getString("description").orEmpty()
        val temperament = requireArguments().getString("temperament").orEmpty()
        val lifeSpan = requireArguments().getString("lifeSpan").orEmpty()
        val origin = requireArguments().getString("origin").orEmpty()

        // --- Configure top toolbar (title + back navigation) ---
        binding.toolbar.title = breedName

        // Try to use a back arrow drawable if it exists in /res/drawable
        // This lets the toolbar behave like an "Up" button without a full ActionBar setup
        val backIconId = resources.getIdentifier("ic_arrow_back_24", "drawable", requireContext().packageName)
        if (backIconId != 0) {
            binding.toolbar.setNavigationIcon(backIconId)
        }

        // Wire the navigation click to the system back dispatcher
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // --- Bind textual content to the UI ---
        // Provide fallbacks for empty data so the UI always shows something meaningful
        binding.tvName.text = breedName
        binding.tvOrigin.text = if (origin.isNotBlank()) origin else getString(R.string.unknown)
        binding.tvTemperament.text = if (temperament.isNotBlank()) temperament else getString(R.string.unknown)
        binding.tvLifeSpan.text = if (lifeSpan.isNotBlank()) lifeSpan else getString(R.string.unknown)
        binding.tvDescription.text = if (description.isNotBlank()) description else getString(R.string.no_description)

        // --- Accessibility: describe the image content for screen readers ---
        binding.ivPhoto.contentDescription = "Foto de $breedName"

        // --- Temporary placeholder while the image is being requested/loaded ---
        // This avoids showing an empty ImageView and pairs nicely with Coil's crossfade.
        binding.ivPhoto.setImageResource(android.R.color.darker_gray)

        // --- Load a breed image if we have a valid breedId ---
        // Option A: Repository returns a List<String> (plain URLs), we take the first one.
        if (breedId.isNotBlank()) {
            // Use the Fragment's lifecycleScope so the job cancels automatically with the view lifecycle
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    // Repository → ServiceLocator.api.getImages returns List<String> (URLs)
                    val urls: List<String> = ServiceLocator.api.getImages(breedId, limit = 1)
                    val url: String? = urls.firstOrNull()

                    // Load into ImageView with Coil if a URL is available
                    if (!url.isNullOrBlank()) {
                        binding.ivPhoto.load(url) {
                            // Smooth transition when the image arrives
                            crossfade(true)
                            // Fallback drawable while loading or if it needs to rebind
                            placeholder(android.R.color.darker_gray)
                        }
                    }
                    // If no URL is returned, we silently keep the placeholder.
                } catch (_: Exception) {
                    // Network or parsing error: keep the placeholder.
                    // You could log or show a Toast here if desired for debug.
                }
            }
        }
    }

    override fun onDestroyView() {
        // Clear the binding reference to prevent leaks when the view is destroyed
        _binding = null
        super.onDestroyView()
    }
}

