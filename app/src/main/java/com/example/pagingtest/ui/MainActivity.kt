package com.example.pagingtest.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.pagingtest.R
import com.example.pagingtest.databinding.ActivityMainBinding
import com.example.pagingtest.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //Clean up >> fragment KTX 사용 여부 차이???
//    private val mainViewModel: MainViewModel by lazy {
//        ViewModelProvider(this).get(MainViewModel::class.java)
//    }
//   >>  private val mainViewModel by viewModels<MainViewModel>()

    private val mainViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            MainViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = this

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setSupportActionBar(binding.mainToolbar)

        binding.mainToolbar.apply {
            setupWithNavController(
                navController,
                appBarConfiguration
            )
            title = ""
        }

        // toolbar 관련 작업
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.mainSearchContainer.visibility =
                when (destination.id) {
                    R.id.contentFragment -> View.GONE
                    R.id.webFragment -> View.GONE
                    else -> View.VISIBLE
                }
        }

        mainViewModel.actionbarTitle.observe(this) {
            binding.mainToolbar.title = it
        }

        // SearchView 탐색 ( 이전 검색 리스트 호출 )
        updateAdapter(emptyList())
        // update 시
        mainViewModel.keywordList.observe(this) {
            if (!it.isNullOrEmpty()) {
                updateAdapter(it)
            }
        }
    }

    private fun updateAdapter(items: List<String>) {
        val autoCompleteAdapter = ArrayAdapter(this, R.layout.item_auto_complete, items)
        binding.mainAutoCompleteTv.apply {
            setAdapter(autoCompleteAdapter)
            setOnFocusChangeListener { _, focus ->
                if (focus) {
                    showDropDown()
                }
            }
        }
    }
}