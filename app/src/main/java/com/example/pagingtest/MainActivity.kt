package com.example.pagingtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.pagingtest.databinding.ActivityMainBinding
import com.example.pagingtest.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //Clean up
//    private val mainViewModel: MainViewModel by lazy {
//        ViewModelProvider(this).get(MainViewModel::class.java)
//    }

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val items = arrayOf("부산", "울산", "서울", "대전")
        val autoCompleteAdapter = ArrayAdapter(this, R.layout.item_auto_complete, items)
        binding.mainAutoCompleteTv.apply {
            setAdapter(autoCompleteAdapter)
            setOnFocusChangeListener { view, focus ->
                if (focus) {
                    showDropDown()
                }
            }
        }
    }
}