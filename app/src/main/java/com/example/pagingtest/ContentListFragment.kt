package com.example.pagingtest

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.pagingtest.databinding.FragmentContentListBinding
import com.example.pagingtest.models.Content
import com.example.pagingtest.viewmodels.MainViewModel

class ContentListFragment : Fragment() {
    private lateinit var binding: FragmentContentListBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_content_list, container, false
        )
        binding.lifecycleOwner = this

        // RecyclerView Setting
        val adapter = ContentAdapter(ContentAdapter.ContentClickListener {
            mainViewModel.updateContent(it)
            binding.root.findNavController()
                .navigate(ContentListFragmentDirections.actionContentListToContent())
        })
        adapter.data = listOf(
            Content(
                "https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
                "blog",
                "인플루엔셜",
                "미움받을 용기",
                "인간은 변할 수 있고, 누구나 행복해 질 수 있다. 단 그러기 위해서는 ‘용기’가 필요하다고 말한 철학자가 있다. 바로 프로이트, 융과 함께 ‘심리학의 3대 거장’으로 일컬어지고 있는 알프레드 아들러다. 『미움받을 용기』는 아들러 심리학에 관한 일본의 1인자 철학자 기시미 이치로와 베스트셀러 작가인 고가 후미타케의 저서로, 아들러의 심리학을 ‘대화체’로 쉽고 맛깔나게 정리하고 있다. 아들러 심리학을 공부한 철학자와 세상에 부정적이고 열등감 많은",
                "2014-11-17T00:00:00.000+09:00",
                "https://search.daum.net/search?w=bookpage&bookId=1467038&q=%EB%AF%B8%EC%9B%80%EB%B0%9B%EC%9D%84+%EC%9A%A9%EA%B8%B0"
            )
        )

        binding.contentListRv.adapter = adapter

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