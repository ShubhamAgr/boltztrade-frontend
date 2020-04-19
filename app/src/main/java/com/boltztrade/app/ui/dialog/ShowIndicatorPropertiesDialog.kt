package com.boltztrade.app.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.boltztrade.app.R
import com.boltztrade.app.callbacks.ShowIndicatorPropertiesDialogCallback

class ShowIndicatorPropertiesDialog(val indicator: String,val givenProps:MutableMap<Any?,Any?>? = null,
                                    val showIndicatorPropertiesDialogCallback:ShowIndicatorPropertiesDialogCallback): DialogFragment(){
    private val LOG_TAG = ShowIndicatorPropertiesDialog::class.java.canonicalName
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val dialogView = when(indicator){
                "Close Price"->inflater.inflate(R.layout.single_period_param,null)
                "Simple Moving Average(SMA)"->inflater.inflate(R.layout.single_period_param,null)
                "Exponential Moving Average(EMA)"->inflater.inflate(R.layout.single_period_param,null)
                "Average Directional Moving Index(ADX)"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "Commodity Channel Index(CCI)"->inflater.inflate(R.layout.single_period_param,null)
                "Parabolic SAR"->inflater.inflate(R.layout.single_period_param,null)
                "Relative Strength Index(RSI)"->inflater.inflate(R.layout.single_period_param,null)
                "Stochastic RSI"->inflater.inflate(R.layout.single_period_param,null)
                "William R"->inflater.inflate(R.layout.single_period_param,null)
                "Stochastic Oscillator K"->inflater.inflate(R.layout.single_period_param,null)
                "Stochastic Oscillator D"->inflater.inflate(R.layout.single_period_param,null)
                "Minus Directional Indicator"->inflater.inflate(R.layout.single_period_param,null)
                "Plus Directional Indicator"->inflater.inflate(R.layout.single_period_param,null)
                "Bollinger Band Width"->inflater.inflate(R.layout.single_period_param,null)
                "Lower Bollinger Band"->inflater.inflate(R.layout.single_period_param,null)
                "Upper Bollinger Band"->inflater.inflate(R.layout.single_period_param,null)
                "Middle Bollinger Band"->inflater.inflate(R.layout.single_period_param,null)
                "Percent B"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "Ichimoku Tenkan Sen"->inflater.inflate(R.layout.single_period_param,null)
                "Ichimoku Kijun Sen"->inflater.inflate(R.layout.single_period_param,null)
                "Ichimoku Span A"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "Ichimoku Senkan Span B"->inflater.inflate(R.layout.single_period_param,null)
                "Ichimoky Chikou Span"->inflater.inflate(R.layout.single_period_param,null)
                "Keltner Channel Middle"->inflater.inflate(R.layout.single_period_param,null)
                "Keltner Channel Upper"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "Keltner Channel Lower"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "Co-Relation Coefficient"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "Covariance"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "Linear Regression"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "Standard Deviation"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "Variance"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "Standard Error"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "Positive Volume Index(PVI)"->inflater.inflate(R.layout.single_period_param,null)
                "Accumulation Distribution"->inflater.inflate(R.layout.single_period_param,null)
                "Chaikin Money Flow"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "Volume Weighted Average Price(VWAP)"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "Moving Volume Weighted Average Price(MVWAP)"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "Negative Volume Index(NVI)"->inflater.inflate(R.layout.indicator_macd_layout,null)
                "On Balance Volume Indicator"->inflater.inflate(R.layout.indicator_macd_layout,null)
                 else->inflater.inflate(R.layout.indicator_macd_layout,null)
            }
            builder.setView(dialogView)

            if(givenProps != null){
                setIndicatorDialogValues(indicator,dialogView,givenProps)
            }

            val doneButton = dialogView.findViewById(R.id.done) as Button
            doneButton.setOnClickListener {
               showIndicatorPropertiesDialogCallback.getProperties(setIndicatorProperties(indicator,dialogView))
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }



fun setIndicatorProperties(indicator:String,dialogView:View):MutableMap<Any?,Any?>{
    val propertiesMap :MutableMap<Any?,Any?> = mutableMapOf()
    when(indicator){
        "Close Price"->{}
        "Simple Moving Average(SMA)"->{}
        "Exponential Moving Average(EMA)"->{}
        "Average Directional Moving Index(ADX)"->{}
        "Commodity Channel Index(CCI)"->{}
        "Parabolic SAR"->{}
        "Relative Strength Index(RSI)"->{}
        "Stochastic RSI"->{}
        "William R"->{}
        "Stochastic Oscillator K"->{}
        "Stochastic Oscillator D"->{}
        "Minus Directional Indicator"->{}
        "Plus Directional Indicator"->{}
        "Bollinger Band Width"->{}
        "Lower Bollinger Band"->{}
        "Upper Bollinger Band"->{}
        "Middle Bollinger Band"->{}
        "Percent B"->{}
        "Ichimoku Tenkan Sen"->{}
        "Ichimoku Kijun Sen"->{}
        "Ichimoku Span A"->{}
        "Ichimoku Senkan Span B"->{}
        "Ichimoky Chikou Span"->{}
        "Keltner Channel Middle"->{}
        "Keltner Channel Upper"->{}
        "Keltner Channel Lower"->{}
        "Co-Relation Coefficient"->{}
        "Covariance"->{}
        "Linear Regression"->{}
        "Standard Deviation"->{}
        "Variance"->{}
        "Standard Error"->{}
        "Positive Volume Index(PVI)"->{}
        "Accumulation Distribution"->{}
        "Chaikin Money Flow"->{}
        "Volume Weighted Average Price(VWAP)"->{}
        "Moving Volume Weighted Average Price(MVWAP)"->{}
        "Negative Volume Index(NVI)"->{}
        "On Balance Volume Indicator"->{}
        "MACD" ->{
            val fastMatext =   dialogView.findViewById<EditText>(R.id.fast_ma_edittext).text.toString().toInt()
            val slowMatext = dialogView.findViewById<EditText>(R.id.slow_ma_edittext).text.toString().toInt()
            val signalPeriodText = dialogView.findViewById<EditText>(R.id.signal_period_edittext).text.toString().toInt()

            propertiesMap["fast_ma_period"] = fastMatext
            propertiesMap["slow_ma_period"] = slowMatext
            propertiesMap["signal_period"] = signalPeriodText
        }

        "Bollinger Band"->{
            try {

                val fieldSpinner = dialogView.findViewById<Spinner>(R.id.field_spinner)!!
                val maSpinner = dialogView.findViewById<Spinner>(R.id.ma_type_spinner)!!
                var fieldText = fieldSpinner?.selectedItem.toString()
                var ma  = maSpinner?.selectedItem.toString()
//                    fieldSpinner.setOnItemClickListener { adapterView, view, i, l ->
//                        Log.d(LOG_TAG,"age Exp position  $i")
//                        fieldText = fieldSpinner?.selectedItem.toString()
//
//                    }
//                    maSpinner.setOnItemClickListener { adapterView, view, i, l ->
//                        Log.d(LOG_TAG,"trading exp position  $i")
//                        ma  = maSpinner?.selectedItem.toString()
//                    }

                var period = dialogView.findViewById<EditText>(R.id.period_edittext).text.toString().toInt()
                var standardDeviation = dialogView.findViewById<EditText>(R.id.standard_deviation_edittext).text.toString().toInt()

                propertiesMap["period"] = period
                propertiesMap["standard_deviation"] = standardDeviation
                propertiesMap["field"] = fieldText.toLowerCase()
                propertiesMap["ma type"] = ma.toLowerCase()

            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        "RSI"->{
            val period = dialogView.findViewById<EditText>(R.id.period_edittext).text.toString().toInt()

            propertiesMap["period"] = period
        }

        "Alligator"->{
            val jawPeriodText = dialogView.findViewById<EditText>(R.id.jaw_period_edittext).text.toString().toInt()
            val jawoffsetText = dialogView.findViewById<EditText>(R.id.jaw_offset_edittext).text.toString().toInt()
            val teethPeriodText = dialogView.findViewById<EditText>(R.id.teeth_period_edittextt).text.toString().toInt()
            val teethPeriodOffsetText = dialogView.findViewById<EditText>(R.id.teeth_offset_edittext).text.toString().toInt()
            val lipsPeriodText = dialogView.findViewById<EditText>(R.id.lips_period_edittext).text.toString().toInt()
            val lipsPeriodOffsetText = dialogView.findViewById<EditText>(R.id.lips_offset_edittext).text.toString().toInt()

            propertiesMap["jaw_period"] = jawPeriodText
            propertiesMap["jaw_offset"] = jawoffsetText
            propertiesMap["teeth_period"] = teethPeriodText
            propertiesMap["teeth_offset"] = teethPeriodOffsetText
            propertiesMap["lips_period"] = lipsPeriodText
            propertiesMap["lips_offset"] = lipsPeriodOffsetText
        }

        else->{Log.d("","no indicator for set properties")}
    }

    return propertiesMap
}
    fun setIndicatorDialogValues(indicator:String, dialogView: View, propertiesMap:MutableMap<Any?,Any?>){
        when(indicator){
            "Close Price"->{}
            "Simple Moving Average(SMA)"->{}
            "Exponential Moving Average(EMA)"->{}
            "Average Directional Moving Index(ADX)"->{}
            "Commodity Channel Index(CCI)"->{}
            "Parabolic SAR"->{}
            "Relative Strength Index(RSI)"->{}
            "Stochastic RSI"->{}
            "William R"->{}
            "Stochastic Oscillator K"->{}
            "Stochastic Oscillator D"->{}
            "Minus Directional Indicator"->{}
            "Plus Directional Indicator"->{}
            "Bollinger Band Width"->{}
            "Lower Bollinger Band"->{}
            "Upper Bollinger Band"->{}
            "Middle Bollinger Band"->{}
            "Percent B"->{}
            "Ichimoku Tenkan Sen"->{}
            "Ichimoku Kijun Sen"->{}
            "Ichimoku Span A"->{}
            "Ichimoku Senkan Span B"->{}
            "Ichimoky Chikou Span"->{}
            "Keltner Channel Middle"->{}
            "Keltner Channel Upper"->{}
            "Keltner Channel Lower"->{}
            "Co-Relation Coefficient"->{}
            "Covariance"->{}
            "Linear Regression"->{}
            "Standard Deviation"->{}
            "Variance"->{}
            "Standard Error"->{}
            "Positive Volume Index(PVI)"->{}
            "Accumulation Distribution"->{}
            "Chaikin Money Flow"->{}
            "Volume Weighted Average Price(VWAP)"->{}
            "Moving Volume Weighted Average Price(MVWAP)"->{}
            "Negative Volume Index(NVI)"->{}
            "On Balance Volume Indicator"->{}
            "MACD" ->{
                dialogView.findViewById<EditText>(R.id.fast_ma_edittext).setText(propertiesMap["fast_ma_period"].toString())
                dialogView.findViewById<EditText>(R.id.slow_ma_edittext).setText(propertiesMap["slow_ma_period"].toString())
                dialogView.findViewById<EditText>(R.id.signal_period_edittext).setText(propertiesMap["signal_period"].toString())
            }

            "Bollinger Band"->{
                try {

                    val fieldSpinner = dialogView.findViewById<Spinner>(R.id.field_spinner)!!
                    val maSpinner = dialogView.findViewById<Spinner>(R.id.ma_type_spinner)!!
                    var fieldText = fieldSpinner?.selectedItem.toString()
                    var ma  = maSpinner?.selectedItem.toString()
                    var period = dialogView.findViewById<EditText>(R.id.period_edittext).text.toString().toInt()
                    var standardDeviation = dialogView.findViewById<EditText>(R.id.standard_deviation_edittext).text.toString().toInt()

                    propertiesMap["period"] = period
                    propertiesMap["standard_deviation"] = standardDeviation
                    propertiesMap["field"] = fieldText.toLowerCase()
                    propertiesMap["ma type"] = ma.toLowerCase()
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            "RSI"->{
                dialogView.findViewById<EditText>(R.id.period_edittext).setText(propertiesMap["period"].toString())
            }

            "Alligator"->{
                dialogView.findViewById<EditText>(R.id.jaw_period_edittext).setText(propertiesMap["jaw_period"].toString())
                dialogView.findViewById<EditText>(R.id.jaw_offset_edittext).setText(propertiesMap["jaw_offset"].toString())
                dialogView.findViewById<EditText>(R.id.teeth_period_edittextt).setText(propertiesMap["teeth_period"].toString())
                dialogView.findViewById<EditText>(R.id.teeth_offset_edittext).setText(propertiesMap["teeth_offset"].toString())
                dialogView.findViewById<EditText>(R.id.lips_period_edittext).setText(propertiesMap["lips_period"].toString())
                dialogView.findViewById<EditText>(R.id.lips_offset_edittext).setText(propertiesMap["lips_offset"].toString())

            }

            else->{
                Log.d("","no indicator for set properties")}
        }
    }

}