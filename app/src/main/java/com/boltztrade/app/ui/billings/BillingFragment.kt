package com.boltztrade.app.ui.billings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.boltztrade.app.R

class BillingFragment : Fragment() {

    companion object {
        fun newInstance() = BillingFragment()
    }

    private lateinit var viewModel: BillingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.billing_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BillingViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
