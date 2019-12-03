package com.boltztrade.app.ui.strategies

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.boltztrade.app.R

class StrategiesFragment : Fragment() {

    companion object {
        fun newInstance() = StrategiesFragment()
    }

    private lateinit var viewModel: StrategiesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.strategies_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StrategiesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
