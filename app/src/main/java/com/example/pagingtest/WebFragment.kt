package com.example.pagingtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.pagingtest.databinding.FragmentWebBinding
import com.example.pagingtest.models.Content
import com.example.pagingtest.viewmodels.MainViewModel

class WebFragment : Fragment() {

    private var _binding: FragmentWebBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val content = mainViewModel.content.value

        content?.let {
            mainViewModel.updateActionBarTitle(it.title)
            initWebView(it)
        }
    }

    private fun initWebView(content: Content) {
        binding.urlWebView.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(content.url)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}