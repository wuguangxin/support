package com.wuguangxin.simple.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wuguangxin.simple.R
import com.wuguangxin.simple.databinding.FragmentTestEmptyBinding

class TestEmptyFragment : Fragment() {
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DataBindingUtil.inflate<FragmentTestEmptyBinding>(LayoutInflater.from(context), R.layout.fragment_test_empty, container, false)!!
        binding.content.text = title
        return binding.root
    }

    companion object {
        const val ARG_TITLE = "title"
        @JvmStatic
        fun newInstance(title: String) =
            TestEmptyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                }
            }
    }
}