package com.example.pagingtest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.map
import com.example.pagingtest.api.Network
import com.example.pagingtest.databinding.FragmentContentListBinding
import com.example.pagingtest.models.Content
import com.example.pagingtest.repository.KakaoRepository
import com.example.pagingtest.viewmodels.ContentListViewModel
import com.example.pagingtest.viewmodels.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ContentListFragment : Fragment() {

    private var _binding: FragmentContentListBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    private val contentViewModel by viewModels<ContentListViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ContentListViewModel::class.java)) {
                    return ContentListViewModel(KakaoRepository(Network.retrofit)) as T
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
                    contentViewModel.searchCafe(query).collectLatest {
                        val lists = it.map { cafe ->
                            Content(
                                cafe.thumbnail,
                                "cafe",
                                cafe.cafeName,
                                cafe.title,
                                cafe.contents,
                                cafe.datetime,
                                cafe.url
                            )
                        }
                        adapter.submitData(lists)
                        Log.d("ContentListFragment", it.toString())
                    }
                }
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
    }
}