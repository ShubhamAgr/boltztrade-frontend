package com.boltztrade.app.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.boltztrade.app.R
import com.boltztrade.app.callbacks.ShowIndicatorPropertiesDialogCallback

class ShowIndicatorPropertiesDialog(val indicatorCount:Int,val indicator: String,val givenProps:MutableMap<Any?,Any?>? = null,
                                    val showIndicatorPropertiesDialogCallback:ShowIndicatorPropertiesDialogCallback): DialogFragment(){
    private val LOG_TAG = ShowIndicatorPropertiesDialog::class.java.canonicalName
    private var price = 0.0
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val dialogView = when(indicator){
                "Close Price"->inflater.inflate(R.layout.single_period_param,null)
                "Price Point"->inflater.inflate(R.layout.single_period_param,null).apply {
                    this.findViewById<TextView>(R.id.property_one_key).text = "Price Point"
                    this.findViewById<EditText>(R.id.property_one_value).setText(price.toString())
                }
                "Simple Moving Average(SMA)"->inflater.inflate(R.layout.single_period_param,null)
                "Exponential Moving Average(EMA)"->inflater.inflate(R.layout.single_period_param,null)
                "Average Directional Moving Index(ADX)"->inflater.inflate(R.layout.single_period_param,null)
                "Commodity Channel Index(CCI)"->inflater.inflate(R.layout.single_period_param,null)
                "Parabolic SAR"->inflater.inflate(R.layout.single_period_param,null)
                "Relative Strength Index(RSI)"->inflater.inflate(R.layout.single_period_param,null)
                "Stochastic RSI"->inflater.inflate(R.layout.single_period_param,null)
                "William R"->inflater.inflate(R.layout.single_period_param,null)
                "Stochastic Oscillator K"->inflater.inflate(R.layout.single_period_param,null)
                "Stochastic Oscillator D"->inflater.inflate(R.layout.two_properties_indicator_dialog,null).apply {
                    this.findViewById<TextView>(R.id.property_one_key).text = "Period"
                    this.findViewById<TextView>(R.id.property_two_key).text = "SMA period"
//                    this.findViewById<EditText>(R.id.property_one_value).setText(5.toString())
                }
                "Minus Directional Indicator"->inflater.inflate(R.layout.single_period_param,null)
                "Plus Directional Indicator"->inflater.inflate(R.layout.single_period_param,null)
                "Bollinger Band Width"->inflater.inflate(R.layout.single_period_param,null)
                "Lower Bollinger Band"->inflater.inflate(R.layout.single_period_param,null)
                "Upper Bollinger Band"->inflater.inflate(R.layout.single_period_param,null)
                "Middle Bollinger Band"->inflater.inflate(R.layout.single_period_param,null)
                "Percent B"->inflater.inflate(R.layout.two_properties_indicator_dialog,null).apply {
                    this.findViewById<TextView>(R.id.property_one_key).text = "period"
                    this.findViewById<TextView>(R.id.property_two_key).text = "K Value"
//                    this.findViewById<EditText>(R.id.property_one_value).setText(5.toString())
                }
                "Ichimoku Tenkan Sen"->inflater.inflate(R.layout.single_period_param,null)
                "Ichimoku Kijun Sen"->inflater.inflate(R.layout.single_period_param,null)
                "Ichimoku Span A"->inflater.inflate(R.layout.two_properties_indicator_dialog,null).apply {
                    this.findViewById<TextView>(R.id.property_one_key).text = "Tenkan Period"
                    this.findViewById<TextView>(R.id.property_two_key).text = "Kijun Period"
//                    this.findViewById<EditText>(R.id.property_one_value).setText(5.toString())
                }
                "Ichimoku Senkan Span B"->inflater.inflate(R.layout.single_period_param,null)
                "Ichimoky Chikou Span"->inflater.inflate(R.layout.single_period_param,null)
                "Keltner Channel Middle"->inflater.inflate(R.layout.single_period_param,null)
                "Keltner Channel Upper"->inflater.inflate(R.layout.two_properties_indicator_dialog,null).apply {
                    this.findViewById<TextView>(R.id.property_one_key).text = "period"
                    this.findViewById<TextView>(R.id.property_two_key).text = "ratio"
//                    this.findViewById<EditText>(R.id.property_one_value).setText(5.toString())
                }
                "Keltner Channel Lower"->inflater.inflate(R.layout.two_properties_indicator_dialog,null).apply {
                    this.findViewById<TextView>(R.id.property_one_key).text = "period"
                    this.findViewById<TextView>(R.id.property_two_key).text = "ratio"
//                    this.findViewById<EditText>(R.id.property_one_value).setText(5.toString())
                }
                "Co-Relation Coefficient"->inflater.inflate(R.layout.two_properties_indicator_dialog,null).apply {
                    this.findViewById<TextView>(R.id.property_one_key).text = "Volume Period"
                    this.findViewById<TextView>(R.id.property_two_key).text = "Coefficient Period"
//                    this.findViewById<EditText>(R.id.property_one_value).setText(5.toString())
                }
                "Covariance"->inflater.inflate(R.layout.two_properties_indicator_dialog,null).apply {
                    this.findViewById<TextView>(R.id.property_one_key).text = "Volume Period"
                    this.findViewById<TextView>(R.id.property_two_key).text = "Covariance Period"
//                    this.findViewById<EditText>(R.id.property_one_value).setText(5.toString())
                }
                "Linear Regression"->inflater.inflate(R.layout.single_period_param,null)
                "Standard Deviation"->inflater.inflate(R.layout.single_period_param,null)
                "Variance"->inflater.inflate(R.layout.single_period_param,null)
                "Standard Error"->inflater.inflate(R.layout.single_period_param,null)
                "Positive Volume Index(PVI)"->inflater.inflate(R.layout.single_period_param,null)
                "Accumulation Distribution"->inflater.inflate(R.layout.single_period_param,null)
                "Chaikin Money Flow"->inflater.inflate(R.layout.single_period_param,null)
                "Volume Weighted Average Price(VWAP)"->inflater.inflate(R.layout.single_period_param,null)
                "Moving Volume Weighted Average Price(MVWAP)"->inflater.inflate(R.layout.two_properties_indicator_dialog,null).apply {
                    this.findViewById<TextView>(R.id.property_one_key).text = "VWAP Period"
                    this.findViewById<TextView>(R.id.property_two_key).text = "MVWAP Period"
//                    this.findViewById<EditText>(R.id.property_one_value).setText(5.toString())
                }
                "Negative Volume Index(NVI)"->inflater.inflate(R.layout.single_period_param,null)
                "On Balance Volume Indicator"->inflater.inflate(R.layout.single_period_param,null)
                 else->inflater.inflate(R.layout.single_period_param,null)
            }


            builder.setView(dialogView)

            if(givenProps != null){
                setIndicatorDialogValues(indicator,dialogView,givenProps)
            }

            val doneButton = dialogView.findViewById(R.id.done) as Button
            doneButton.setOnClickListener {
                if(indicator == "Price Point"){
                    price = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toDouble()
                    Log.e("Price Point callback",price.toString())
                    showIndicatorPropertiesDialogCallback.getPrice(price)
                }else{
                    showIndicatorPropertiesDialogCallback.getProperties(setIndicatorProperties(indicator,dialogView))
                }

            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }



fun setIndicatorProperties(indicator:String,dialogView:View):MutableMap<Any?,Any?>{
    val propertiesMap :MutableMap<Any?,Any?> = mutableMapOf()
    when(indicator){
        "Close Price"->{
        }
        "Simple Moving Average(SMA)"->{

            propertiesMap["barCount"] =
                dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Exponential Moving Average(EMA)"->{
            propertiesMap["barCount"] =
                dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Average Directional Moving Index(ADX)"->{
            propertiesMap["barCount"] =
                dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Commodity Channel Index(CCI)"->{
            propertiesMap["barCount"] =
                dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Parabolic SAR"->{
            propertiesMap["barCount"] =
                dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Relative Strength Index(RSI)"->{
            propertiesMap["barCount"] =
                dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Stochastic RSI"->{
            propertiesMap["barCount"] =
                dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "William R"->{
            propertiesMap["barCount"] =
                dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Stochastic Oscillator K"->{
            propertiesMap["barCount"] =
                dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Stochastic Oscillator D"->{

            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
            propertiesMap["smaBarCount"] = dialogView.findViewById<EditText>(R.id.property_two_value).text.toString().toInt()
        }
        "Minus Directional Indicator"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Plus Directional Indicator"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Bollinger Band Width"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Lower Bollinger Band"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Upper Bollinger Band"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Middle Bollinger Band"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Percent B"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
            propertiesMap["kValue"] = dialogView.findViewById<EditText>(R.id.property_two_value).text.toString().toInt()
        }
        "Ichimoku Tenkan Sen"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Ichimoku Kijun Sen"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Ichimoku Span A"->{

            propertiesMap["tenkanBarCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
            propertiesMap["kijunBarCount"] = dialogView.findViewById<EditText>(R.id.property_two_value).text.toString().toInt()
        }
        "Ichimoku Senkan Span B"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Ichimoky Chikou Span"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Keltner Channel Middle"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Keltner Channel Upper"->{

            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
            propertiesMap["ratio"] = dialogView.findViewById<EditText>(R.id.property_two_value).text.toString().toInt()
        }
        "Keltner Channel Lower"->{

            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
            propertiesMap["ratio"] = dialogView.findViewById<EditText>(R.id.property_two_value).text.toString().toInt()
        }
        "Co-Relation Coefficient"->{

            propertiesMap["volumeBarCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
            propertiesMap["coefficientBarCount"] = dialogView.findViewById<EditText>(R.id.property_two_value).text.toString().toInt()
        }
        "Covariance"->{

            propertiesMap["volumeBarCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
            propertiesMap["covarianceBarCount"] = dialogView.findViewById<EditText>(R.id.property_two_value).text.toString().toInt()
        }
        "Linear Regression"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Standard Deviation"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Variance"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Standard Error"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Positive Volume Index(PVI)"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Accumulation Distribution"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Chaikin Money Flow"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Volume Weighted Average Price(VWAP)"->{
            propertiesMap["barCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
        }
        "Moving Volume Weighted Average Price(MVWAP)"->{

            propertiesMap["vapBarCount"] = dialogView.findViewById<EditText>(R.id.property_one_value).text.toString().toInt()
            propertiesMap["mVapBarCount"] = dialogView.findViewById<EditText>(R.id.property_two_value).text.toString().toInt()
        }
        "Negative Volume Index(NVI)"->{
            propertiesMap[""] = ""
        }
        "On Balance Volume Indicator"->{
            propertiesMap[""] = ""
        }

        else->{Log.d("","no indicator for set properties")}
    }

    return propertiesMap
}
    fun setIndicatorDialogValues(indicator:String, dialogView: View, propertiesMap:MutableMap<Any?,Any?>){
        when(indicator){
            "Close Price"->{}
            "Simple Moving Average(SMA)"->{
                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Exponential Moving Average(EMA)"->{
                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Average Directional Moving Index(ADX)"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Commodity Channel Index(CCI)"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Parabolic SAR"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Relative Strength Index(RSI)"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Stochastic RSI"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "William R"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Stochastic Oscillator K"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Stochastic Oscillator D"->{
                val value1 = propertiesMap["barCount"].toString()
                val value2 = propertiesMap["smaBarCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value1)
                dialogView.findViewById<EditText>(R.id.property_two_value).setText(value2)
            }
            "Minus Directional Indicator"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Plus Directional Indicator"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Bollinger Band Width"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Lower Bollinger Band"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Upper Bollinger Band"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Middle Bollinger Band"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Percent B"->{

                val value1 = propertiesMap["barCount"].toString()
                val value2 = propertiesMap["kValue"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value1)
                dialogView.findViewById<EditText>(R.id.property_two_value).setText(value2)
            }
            "Ichimoku Tenkan Sen"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Ichimoku Kijun Sen"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Ichimoku Span A"->{

                val value1 = propertiesMap["tenkanBarCount"].toString()
                val value2 = propertiesMap["kijunBarCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value1)
                dialogView.findViewById<EditText>(R.id.property_two_value).setText(value2)
            }
            "Ichimoku Senkan Span B"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Ichimoky Chikou Span"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Keltner Channel Middle"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Keltner Channel Upper"->{

                val value1 = propertiesMap["barCount"].toString()
                val value2 = propertiesMap["ratio"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value1)
                dialogView.findViewById<EditText>(R.id.property_two_value).setText(value2)
            }
            "Keltner Channel Lower"->{

                val value1 = propertiesMap["barCount"].toString()
                val value2 = propertiesMap["ratio"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value1)
                dialogView.findViewById<EditText>(R.id.property_two_value).setText(value2)
            }
            "Co-Relation Coefficient"->{

                val value1 = propertiesMap["volumeBarCount"].toString()
                val value2 = propertiesMap["coefficientBarCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value1)
                dialogView.findViewById<EditText>(R.id.property_two_value).setText(value2)
            }
            "Covariance"->{

                val value1 = propertiesMap["volumeBarCount"].toString()
                val value2 = propertiesMap["covarianceBarCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value1)
                dialogView.findViewById<EditText>(R.id.property_two_value).setText(value2)
            }
            "Linear Regression"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Standard Deviation"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Variance"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Standard Error"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Positive Volume Index(PVI)"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Accumulation Distribution"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Chaikin Money Flow"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Volume Weighted Average Price(VWAP)"->{

                val value = propertiesMap["barCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value)
            }
            "Moving Volume Weighted Average Price(MVWAP)"->{

                val value1 = propertiesMap["vapBarCount"].toString()
                val value2 = propertiesMap["mVapBarCount"].toString()
                dialogView.findViewById<EditText>(R.id.property_one_value).setText(value1)
                dialogView.findViewById<EditText>(R.id.property_two_value).setText(value2)
            }
            "Negative Volume Index(NVI)"->{Log.d("nvi","not applicable")}
            "On Balance Volume Indicator"->{Log.d("nvi","not applicable")}

            else->{
                Log.d("","no indicator for set properties")}
        }
    }

}