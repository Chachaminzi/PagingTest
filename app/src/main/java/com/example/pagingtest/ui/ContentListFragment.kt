package com.example.pagingtest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.pagingtest.ContentAdapter
import com.example.pagingtest.R
import com.example.pagingtest.api.Network
import com.example.pagingtest.databinding.FragmentContentListBinding
<<<<<<< HEAD:app/src/main/java/com/example/pagingtest/ui/ContentListFragment.kt
=======
import com.example.pagingtest.db.SearchDatabase
import com.example.pagingtest.db.getDatabase
import com.example.pagingtest.models.Content
>>>>>>> origin/main:app/src/main/java/com/example/pagingtest/ContentListFragment.kt
import com.example.pagingtest.repository.KakaoRepository
import com.example.pagingtest.viewmodels.ContentListViewModel
import com.example.pagingtest.viewmodels.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ContentListFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentContentListBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    private val contentViewModel by viewModels<ContentListViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ContentListViewModel::class.java)) {
                    return ContentListViewModel(
                        KakaoRepository(
                            getDatabase(requireContext()),
                            Network.retrofit
                        )
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentListBinding.inflate(layoutInflater, container, false)

        // RecyclerView Setting
        val adapter = ContentAdapter(ContentAdapter.ContentClickListener {
            mainViewModel.updateContent(it)
            binding.root.findNavController()
                .navigate(ContentListFragmentDirections.actionContentListToContent())
        })
        binding.contentListRv.adapter = adapter

        // Submit button
        var searchJob: Job? = null
        mainViewModel.isSubmit.observe(viewLifecycleOwner) {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                mainViewModel.submitQuery.value?.let { query ->
                    loadList(contentViewModel.spinnerSelected.value ?: 0, adapter)
                }
            }
        }

        // Spinner
        contentViewModel.spinnerSelected.observe(viewLifecycleOwner) { spinner ->
            lifecycleScope.launch {
                loadList(spinner, adapter)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dialog Show
        binding.contentListFilterBtn.setOnClickListener { view ->
            val typeArray = resources.getStringArray(R.array.list_type_array)
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Select")
                .setSingleChoiceItems(typeArray, -1) { dialog, i ->

                }
                .setPositiveButton("ok") { dialog, id ->

                }
                .setNegativeButton("cancel") { dialog, id ->

                }
            builder.show()
        }

        // Spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_array,
            R.layout.item_auto_complete
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.contentListFilterSpinner.adapter = adapter
        }

        binding.contentListFilterSpinner.onItemSelectedListener = this
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        contentViewModel.updateSpinnerSelected(p2)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    private suspend fun loadList(spinner: Int, adapter: ContentAdapter) {
        when (spinner) {
            0 -> {
//                mainViewModel.submitQuery.value?.let { query ->
//                    contentViewModel.searchContent(query).collectLatest {
//                        adapter.submitData(it)
//                    }
//                }
            }
            1 -> {
                mainViewModel.submitQuery.value?.let { query ->
                    contentViewModel.searchBlog(query).collectLatest {
                        adapter.submitData(it)
                    }
                }
            }
            2 -> {
                mainViewModel.submitQuery.value?.let { query ->
                    contentViewModel.searchCafe(query).collectLatest {
                        adapter.submitData(it)
                    }
                }
            }
        }
    }
}