package com.example.pagingtest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.PagingData
import com.example.pagingtest.R
import com.example.pagingtest.databinding.FragmentContentListBinding
import com.example.pagingtest.models.ItemModel
import com.example.pagingtest.repository.KakaoRepository
import com.example.pagingtest.repository.KeywordRepository
import com.example.pagingtest.viewmodels.ContentListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ContentListFragment : Fragment() {

    private lateinit var binding: FragmentContentListBinding

    @Inject
    lateinit var kakaoRepository: KakaoRepository

    @Inject
    lateinit var keywordRepository: KeywordRepository

    private val contentViewModel by viewModels<ContentListViewModel>()

    private val spinnerAdapter = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            adapterView: AdapterView<*>?,
            view: View?,
            position: Int,
            getItemId: Long
        ) {
            contentViewModel.updateSpinnerSelected(position)
        }

        override fun onNothingSelected(view: AdapterView<*>?) {
        }
    }

    private val recyclerAdapter by lazy {
        // TODO(ConcatAdapter로 변경)
        ContentAdapter(
            ContentClickListener {
                binding.root.findNavController().navigate(
                    ContentListFragmentDirections.actionContentListToContent(it)
                )
            },
            FilterClickListener {
                val typeArray = resources.getStringArray(R.array.list_type_array)
                val builder = AlertDialog.Builder(requireContext())
                var selectedId = contentViewModel.filterType.value
                builder.setTitle("Select")
                    .setSingleChoiceItems(typeArray, selectedId!!) { dialog, i ->
                        selectedId = i
                    }
                    .setPositiveButton("ok") { dialog, id ->
                        contentViewModel.updateFilter(selectedId!!)
                    }
                    .setNegativeButton("cancel") { dialog, id ->

                    }
                builder.show()
            },
            spinnerAdapter
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_content_list, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = contentViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 이동
        lifecycleScope.launch {
            recyclerAdapter.submitData(
                PagingData.from(
                    listOf(ItemModel.HeaderItem("Header"))
                )
            )
        }
        binding.contentListRv.adapter = recyclerAdapter
        contentViewModel.isSubmit.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                contentViewModel.loadList()
            }
        }

        // Filter Type
        contentViewModel.filterType.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                contentViewModel.loadList()
            }
        }

//        updateAdapter(emptyList())
        contentViewModel.keywordList.observe(viewLifecycleOwner) { keywords ->
            if (!keywords.isNullOrEmpty()) {
                updateAdapter(keywords)
            }
        }

        // paging data update
        contentViewModel.itemPagingData.observe(viewLifecycleOwner) {
            recyclerAdapter.submitHeaderAndList(contentViewModel.selectedPostType, it)
        }
    }

    private fun updateAdapter(items: List<String>) {
        val autoCompleteAdapter =
            MyAutoArrayAdapter(requireContext(), R.layout.item_drop_down, items)
        binding.mainAutoCompleteTv.apply {
            setAdapter(autoCompleteAdapter)
            setOnFocusChangeListener { _, focus ->
                showDropDown()
            }
            setOnClickListener {
                showDropDown()
            }
        }
    }
}