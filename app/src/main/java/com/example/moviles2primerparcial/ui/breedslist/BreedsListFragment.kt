package com.example.moviles2primerparcial.ui.breedslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviles2primerparcial.R
import com.example.moviles2primerparcial.data.models.remote.ServiceLocator
import com.example.moviles2primerparcial.data.models.remote.dto.BreedDTO
import com.example.moviles2primerparcial.databinding.FragmentBreedsListBinding
import com.example.moviles2primerparcial.ui.common.SpacingItemDecoration
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BreedsListFragment : Fragment() {

    private var _binding: FragmentBreedsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BreedAdapter

    // Fuente completa y filtro actual
    private var allBreeds: List<BreedDTO> = emptyList()
    private var currentQuery: String = ""

    private var fetchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Título fijo en la toolbar
        binding.toolbar.title = getString(R.string.title_breeds)

        // RecyclerView
        adapter = BreedAdapter { breed ->
            // Navegación manual lista -> detalle
            val args = Bundle().apply {
                putString("breedId", breed.id)
                putString("breedName", breed.name)
                putString("origin", breed.origin)
                putString("temperament", breed.temperament ?: "")
                putString("lifeSpan", breed.lifeSpan ?: "")
                putString("description", breed.description ?: "")
            }
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    com.example.moviles2primerparcial.ui.breeddetail.BreedDetailFragment().apply { arguments = args }
                )
                .addToBackStack(null)
                .commit()
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        val spacing = (12 * resources.displayMetrics.density).toInt()
        binding.recyclerView.addItemDecoration(SpacingItemDecoration(spacing))

        // Buscador, filtra localmente por nombre
        configureSearch(binding.searchView)

        // Carga inicial desde la API (una sola vez)
        loadBreeds()
    }

    private fun configureSearch(searchView: SearchView) {
        searchView.queryHint = getString(R.string.search)
        // Mostrar teclado listo para escribir
        searchView.isIconified = false
        searchView.clearFocus() // sacá esto si querés foco inicial

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentQuery = newText.orEmpty()
                applyFilter()
                return true
            }
        })
    }

    private fun applyFilter() {
        val q = currentQuery.trim().lowercase()
        val filtered = if (q.isEmpty()) {
            allBreeds
        } else {
            allBreeds.filter { it.name.lowercase().contains(q) }
        }
        adapter.setData(filtered)
        binding.statusText.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        binding.statusText.text = if (q.isEmpty()) getString(R.string.loading) else getString(R.string.no_results)
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun setError(msg: String) {
        binding.statusText.visibility = View.VISIBLE
        binding.statusText.text = msg
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun loadBreeds() {
        if (allBreeds.isNotEmpty()) {
            applyFilter()
            return
        }
        fetchJob?.cancel()
        fetchJob = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                try {
                    setLoading(true)
                    binding.statusText.visibility = View.GONE
                    val breeds = ServiceLocator.api.getBreeds()
                    allBreeds = breeds
                    applyFilter()
                } catch (e: Exception) {
                    setError(e.message ?: "Error al cargar razas")
                } finally {
                    setLoading(false)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
