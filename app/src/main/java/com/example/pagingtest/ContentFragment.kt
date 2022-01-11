package com.example.pagingtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pagingtest.databinding.FragmentContentBinding
import com.example.pagingtest.viewmodels.MainViewModel

class ContentFragment : Fragment() {

    private lateinit var binding: FragmentContentBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false)
        binding.lifecycleOwner = this

        binding.item = mainViewModel.content.value
        mainViewModel.updateActionBarTitle(mainViewModel.content.value!!.label)

        // Web Url 이동
        binding.contentItemMoveBtn.setOnClickListener {
            binding.root.findNavController()
                .navigate(ContentFragmentDirections.actionContentToWeb())
        }

        return binding.root
    }
}