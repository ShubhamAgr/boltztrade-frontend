package com.boltztrade.app.ui.strategies


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import com.boltztrade.app.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CreateStrategyPart3 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var profitPercentEditText:EditText
    private lateinit var lossPercentEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_create_strategy_part3, container, false)
        profitPercentEditText = view.findViewById(R.id.target_profit_edittext)
        lossPercentEditText = view.findViewById(R.id.stop_loss_edittext)

        return view
    }


    fun setPage(){
        try {
            MyStrategy.setMTargetProfitPercent(profitPercentEditText?.text.toString().toFloat())
            MyStrategy.setMTargetLossPercent(lossPercentEditText.text.toString().toFloat())
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateStrategyPart3().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
