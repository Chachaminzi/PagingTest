package com.example.pagingtest.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.example.pagingtest.R
import com.example.pagingtest.databinding.FragmentContentListBinding
import com.example.pagingtest.repository.KakaoRepository
import com.example.pagingtest.repository.KeywordRepository
import com.example.pagingtest.viewmodels.ContentListViewModel
import dagger.hilt.android.AndroidEntryPoint
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
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            contentViewModel.updateSpinnerSelected(position)
        }

        override fun onNothingSelected(view: AdapterView<*>?) {
        }
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

        // RecyclerView
        val contentAdapter = ContentAdapter(
            ContentClickListener {
                binding.root.findNavController().navigate(
                    ContentListFragmentDirections.actionContentListToContent(it)
                )
            }
        )

        val filterAdapter = FilterAdapter(
            FilterClickListener {
                val typeArray = resources.getStringArray(R.array.list_type_array)
                val builder = AlertDialog.Builder(requireContext())
                var selectedId = contentViewModel.filterType.value

                hideKeyboard()

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

        val concatAdapter = ConcatAdapter(filterAdapter, contentAdapter)
        binding.contentListRv.adapter = concatAdapter
        contentViewModel.isSubmit.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                hideKeyboard()
                contentViewModel.loadList()
            }
        }

        // Edittext 포커스 지우기
        binding.contentListRv.setOnTouchListener { v, event ->
            binding.mainSearchEt.clearFocus()
            false
        }

        // Filter Type
        contentViewModel.filterType.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                contentViewModel.loadList()
            }
        }

        // keyword
        binding.mainSearchEt.apply {
            setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    binding.mainSearchKeywordList.visibility = View.VISIBLE
                } else {
                    binding.mainSearchKeywordList.visibility = View.INVISIBLE
                    hideKeyboard()
                }
            }
            setOnEditorActionListener { v, actionId, event ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        contentViewModel.updateSpinnerSelected(null)
                        true
                    }
                    else -> false
                }
            }
        }

        val keywordClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            contentViewModel.updateSubmitQuery(position)
            hideKeyboard()
        }
        contentViewModel.keywordList.observe(viewLifecycleOwner) { keywords ->
            if (!keywords.isNullOrEmpty()) {
                updateKeywordAdapter(keywords, keywordClickListener)
            }
        }

        // paging data update
        contentViewModel.itemPagingData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                contentAdapter.submitData(it)
            }
            filterAdapter.submitPostType(contentViewModel.selectedPostType)
        }
    }

    private fun updateKeywordAdapter(
        items: List<String>,
        clickListener: AdapterView.OnItemClickListener
    ) {
        val listAdapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, items)
        binding.mainSearchKeywordList.apply {
            adapter = listAdapter
            onItemClickListener = clickListener
        }
    }

    private fun hideKeyboard() {
        binding.mainSearchEt.clearFocus()
        // keyboard 내리기
        (requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            binding.mainSearchEt.windowToken,
            0
        )
    }
}