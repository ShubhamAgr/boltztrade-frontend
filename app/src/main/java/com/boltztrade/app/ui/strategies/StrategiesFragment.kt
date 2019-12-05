package com.boltztrade.app.ui.strategies

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.boltztrade.app.R

class StrategiesFragment : Fragment() {

    companion object {
        fun newInstance() = StrategiesFragment()
    }

    private lateinit var viewModel: StrategiesViewModel
    private lateinit var moveToStrategyActivityButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.strategies_fragment, container, false)
        moveToStrategyActivityButton = view.findViewById(R.id.button_move_to_new_strategy_activity)
        moveToStrategyActivityButton.setOnClickListener {
            activity?.startActivity(Intent(activity,StrategyActivity::class.java))
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StrategiesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
