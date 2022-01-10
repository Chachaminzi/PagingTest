package com.example.pagingtest

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.pagingtest.databinding.FragmentContentListBinding

class ContentListFragment : Fragment() {
    private var _binding: FragmentContentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentListBinding.inflate(layoutInflater, container, false)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}