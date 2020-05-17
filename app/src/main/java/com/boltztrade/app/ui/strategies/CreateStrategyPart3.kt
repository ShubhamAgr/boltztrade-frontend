package com.boltztrade.app.ui.strategies


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat

import com.boltztrade.app.R
import kotlinx.android.synthetic.main.fragment_create_strategy_part4.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CreateStrategyPart3 : Fragment() {
    private val LOG_TAG = CreateStrategyPart3::class.java.canonicalName?:"CreateStrategyPart3"
    // TODO: Rename and change types of parameters
    private var param1: Boolean? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getBoolean(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var profitPercentEditText:EditText
    private lateinit var lossPercentEditText: EditText
    private lateinit var backtestPeriodDayButton:Button
    private lateinit var backtestPeriod5DayButton:Button
    private lateinit var backtestPeriodMonthButton: Button
    private lateinit var backtestPeriod3MonthButton: Button
    private lateinit var backtestPeriod6MonthButton: Button
    private lateinit var backtestperiodYearButton:Button
    private lateinit var backtestPeriod5YearButton: Button

    private lateinit var tempBacktestPeriodButton: Button
    private var period:String = "day"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_create_strategy_part4, container, false)
        profitPercentEditText = view.findViewById(R.id.target_profit_edittext)
        lossPercentEditText = view.findViewById(R.id.stop_loss_edittext)
        backtestPeriodDayButton =  view.findViewById(R.id.one_day_period)
        backtestPeriod5DayButton = view.findViewById(R.id.five_day_period)
        backtestPeriodMonthButton = view.findViewById(R.id.one_month_period)
        backtestPeriod3MonthButton = view.findViewById(R.id.three_month_period)
        backtestPeriod6MonthButton = view.findViewById(R.id.six_month_period)
        backtestperiodYearButton = view.findViewById(R.id.one_year_period)
        backtestPeriod5YearButton = view.findViewById(R.id.five_year_period)
        tempBacktestPeriodButton = backtestPeriodDayButton

        backtestPeriodDayButton.setOnClickListener {
            tempBacktestPeriodButton.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
            backtestPeriodDayButton.setTextColor(ContextCompat.getColor(activity!!,R.color.deep_saffron))
            tempBacktestPeriodButton = backtestPeriodDayButton
            period = "day"
        }
        backtestPeriod5DayButton.setOnClickListener {
            tempBacktestPeriodButton.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
            backtestPeriod5DayButton.setTextColor(ContextCompat.getColor(activity!!,R.color.deep_saffron))
            tempBacktestPeriodButton = backtestPeriod5DayButton
            period = "5Day"
        }
        backtestPeriodMonthButton.setOnClickListener {
            tempBacktestPeriodButton.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
            backtestPeriodMonthButton.setTextColor(ContextCompat.getColor(activity!!,R.color.deep_saffron))
            tempBacktestPeriodButton = backtestPeriodMonthButton
            period = "Month"
        }
        backtestPeriod3MonthButton.setOnClickListener {
            tempBacktestPeriodButton.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
            backtestPeriod3MonthButton.setTextColor(ContextCompat.getColor(activity!!,R.color.deep_saffron))
            tempBacktestPeriodButton = backtestPeriod3MonthButton
            period = "3Month"
        }
        backtestPeriod6MonthButton.setOnClickListener {
            tempBacktestPeriodButton.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
            backtestPeriod6MonthButton.setTextColor(ContextCompat.getColor(activity!!,R.color.deep_saffron))
            tempBacktestPeriodButton = backtestPeriod6MonthButton
            period = "6Month"
        }

        backtestperiodYearButton.setOnClickListener {
            tempBacktestPeriodButton.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
            backtestperiodYearButton.setTextColor(ContextCompat.getColor(activity!!,R.color.deep_saffron))
            tempBacktestPeriodButton = backtestperiodYearButton
            period = "1Year"
        }

        backtestPeriod5YearButton.setOnClickListener {
            tempBacktestPeriodButton.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
            backtestPeriod5YearButton.setTextColor(ContextCompat.getColor(activity!!,R.color.deep_saffron))
            tempBacktestPeriodButton = backtestPeriod5YearButton
            period = "5Year"
        }
        if(param1 == true)initPage()
        return view
    }

    fun initPage(){
        try {
            val period = MyStrategy.getMPeriod()
            when(period){
                "day"->{
                    tempBacktestPeriodButton.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
                    backtestPeriodDayButton.setTextColor(ContextCompat.getColor(activity!!,R.color.deep_saffron))
                    tempBacktestPeriodButton = backtestPeriodDayButton
                }
                "5Day"->{
                    tempBacktestPeriodButton.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
                    backtestPeriod5DayButton.setTextColor(ContextCompat.getColor(activity!!,R.color.deep_saffron))
                    tempBacktestPeriodButton = backtestPeriod5DayButton
                }
                "Month"->{
                    tempBacktestPeriodButton.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
                    backtestPeriodMonthButton.setTextColor(ContextCompat.getColor(activity!!,R.color.deep_saffron))
                    tempBacktestPeriodButton = backtestPeriodMonthButton
                }
                "3Month"->{
                    tempBacktestPeriodButton.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
                    backtestPeriod3MonthButton.setTextColor(ContextCompat.getColor(activity!!,R.color.deep_saffron))
                    tempBacktestPeriodButton = backtestPeriod3MonthButton
                }
                "6Month"->{
                    tempBacktestPeriodButton.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
                    backtestPeriod6MonthButton.setTextColor(ContextCompat.getColor(activity!!,R.color.deep_saffron))
                    tempBacktestPeriodButton = backtestPeriod6MonthButton
                }
                "1Year"->{
                    tempBacktestPeriodButton.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
                    backtestperiodYearButton.setTextColor(ContextCompat.getColor(activity!!,R.color.deep_saffron))
                    tempBacktestPeriodButton = backtestperiodYearButton
                }
                "5Year"->{
                    tempBacktestPeriodButton.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
                    backtestPeriod5YearButton.setTextColor(ContextCompat.getColor(activity!!,R.color.deep_saffron))
                    tempBacktestPeriodButton = backtestPeriod5YearButton
                }
                else->{
                    Log.d(LOG_TAG,"unknonwn ")
                }
            }
            profitPercentEditText?.setText(MyStrategy.getMTargetProfitPercent().toString())
            lossPercentEditText?.setText(MyStrategy.getMTargetLossPercent().toString())
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun setPage(){
        try {
            MyStrategy.setMTargetProfitPercent(profitPercentEditText?.text.toString().toFloat())
            MyStrategy.setMTargetLossPercent(lossPercentEditText.text.toString().toFloat())
            MyStrategy.setMPeriod(period)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: Boolean, param2: String) =
            CreateStrategyPart3().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
