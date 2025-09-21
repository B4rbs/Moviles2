package com.example.moviles2primerparcial.ui.breedslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviles2primerparcial.R
import com.example.moviles2primerparcial.data.remote.ServiceLocator
import com.example.moviles2primerparcial.data.remote.dto.BreedDTO
import com.example.moviles2primerparcial.databinding.FragmentBreedsListBinding
import com.example.moviles2primerparcial.ui.breeddetail.BreedDetailFragment
import kotlinx.coroutines.launch

/**
 * Displays a scrollable list of cat breeds.
 * - Creates and wires a RecyclerView adapter.
 * - Fetches data from the remote API.
 * - Navigates to detail when an item is clicked.
 */
class BreedsListFragment : Fragment() {

    private var _binding: FragmentBreedsListBinding? = null
    private val binding get() = _binding!!

    // Adapter that renders each row and forwards clicks via a lambda.
    private lateinit var adapter: BreedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Sets up RecyclerView, toolbar actions, and triggers the first load.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) Recycler setup: layout + adapter + a simple divider between rows.
        adapter = BreedAdapter { breed -> openDetail(breed) }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        )

        // 2) Toolbar action: manual refresh.
        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_refresh) {
                loadBreeds()
                true
            } else false
        }

        // 3) Initial load.
        loadBreeds()
    }

    /**
     * Fetches the breeds from the API and updates UI state accordingly.
     * - Shows a progress indicator during the request.
     * - Displays an error or empty state when needed.
     */
    private fun loadBreeds() {
        binding.progressBar.visibility = View.VISIBLE
        binding.statusText.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val breeds: List<BreedDTO> = ServiceLocator.api.getBreeds()

                binding.progressBar.visibility = View.GONE
                if (breeds.isEmpty()) {
                    // Empty state message (reuses a generic string).
                    binding.statusText.visibility = View.VISIBLE
                    binding.statusText.text = getString(R.string.image_not_available)
                    adapter.setData(emptyList())
                } else {
                    // Push data to adapter (triggers list render).
                    adapter.setData(breeds)
                }
            } catch (e: Exception) {
                // Error state + toast with the error message for quick feedback.
                binding.progressBar.visibility = View.GONE
                binding.statusText.visibility = View.VISIBLE
                binding.statusText.text = getString(R.string.error_generic)
                Toast.makeText(
                    requireContext(),
                    "Error: ${e.message ?: "unknown"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Navigates to the detail fragment, passing all the required fields as arguments.
     * Arguments are bundled explicitly to keep the transition self-contained.
     */
    private fun openDetail(b: BreedDTO) {
        val args = Bundle().apply {
            putString(BreedDetailFragment.ARG_BREED_ID, b.id.orEmpty())
            putString(BreedDetailFragment.ARG_BREED_NAME, b.name.orEmpty())
            putString(BreedDetailFragment.ARG_DESCRIPTION, b.description.orEmpty())
            putString(BreedDetailFragment.ARG_TEMPERAMENT, b.temperament.orEmpty())
            putString(BreedDetailFragment.ARG_LIFE_SPAN, b.lifeSpan.orEmpty())
            putString(BreedDetailFragment.ARG_ORIGIN, b.origin.orEmpty())
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, BreedDetailFragment().apply { arguments = args })
            .addToBackStack(null) // Enables back navigation with toolbar or system back.
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid leaking the view binding after the view is destroyed.
    }
}