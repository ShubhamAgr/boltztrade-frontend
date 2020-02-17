package com.boltztrade.app.ui.strategies

import android.app.AlertDialog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.boltztrade.app.R
import com.boltztrade.app.callbacks.StrategyCardTouchCallback
import com.boltztrade.app.model.Indicator
import com.boltztrade.app.model.Strategy
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CreateStrategyPart2 : Fragment() {

    private val LOG_TAG = CreateStrategyPart2::class.java.canonicalName
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter : RecyclerView.Adapter<*>
    private lateinit var viewManager : RecyclerView.LayoutManager

    private val strategylist:MutableList<Strategy> = mutableListOf()
    private val operatorList:MutableList<String> = mutableListOf("<",">","<=",">=","==")
    private val optionList:MutableMap<String,MutableList<String>> = mutableMapOf()

    private lateinit var createStrategyFAB :FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_create_strategy_part2, container, false)
        createStrategyFAB = view.findViewById(R.id.createStrategyFab)

        createStrategyFAB.setOnClickListener {
            showDialog()
        }

        viewManager = LinearLayoutManager(activity)

        viewAdapter = CreateStrategyListAdapter(strategylist,object :StrategyCardTouchCallback{
            override fun onSelectFirstIndicator(position: Int) {
                Log.d(LOG_TAG,"selected first indicator $position")
                editIndicatorDialog(strategylist.get(position).firstIndicator.name,0,position)
            }

            override fun onSelectSecondIndicator(position: Int) {
                Log.d(LOG_TAG,"selected second indicator $position")
                editIndicatorDialog(strategylist.get(position).secondIndicator.name,1,position)
            }

            override fun onSelectComparisonOperator(position: Int) {
                Log.d(LOG_TAG,"selected  comparison operator $position")
                showComparisonOperatorDialog(position)
            }

            override fun onSelectLogicalOperator(position: Int) {
                Log.d(LOG_TAG,"selected logical operator $position")
                strategylist[position].logicalOperator = if(strategylist[position].logicalOperator=="and") "or" else "and"
                viewAdapter.notifyDataSetChanged()
            }

        })

        recyclerView = (view.findViewById(R.id.create_strategy_recycler_view) as RecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                Log.d(LOG_TAG, "onMoveCalled")
                return true
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.d(LOG_TAG, "onSwipedCalled.... on direction ... $direction")
                val position = viewHolder.adapterPosition
                val item  = strategylist[viewHolder.adapterPosition]
                val disposible =   Observable.create(ObservableOnSubscribe<Boolean> {
                    strategylist?.removeAt(position)
                    it.onNext(true)
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({

                    if(it) {
                        Log.d(LOG_TAG,"notification deleted...")
                        viewAdapter.notifyDataSetChanged()
                        if(strategylist?.isEmpty()){
//                            noNewNotification.visibility=View.VISIBLE
//                            notificationIndicatorButton.visibility = View.INVISIBLE
                        }else{
//                            noNewNotification.visibility= View.INVISIBLE
                        }

                    }
                },{
                    Log.d(LOG_TAG,"something went wrong while deleting the notification")
                    it.printStackTrace()
                },{
                    Log.d(LOG_TAG,"notification delete completed...")
                })

            }

        }

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerView)
        return view
    }


    var showDialogCounter = 0
    fun showDialog(){
        var dialog:AlertDialog? = null
        val dialogBuilder = AlertDialog.Builder(view?.context)
        dialogBuilder.setTitle("Choose Indicator")
            .setItems(R.array.indiactor_list
            ) { dialog, which ->
                Log.d("LOG", resources.getStringArray(R.array.indiactor_list)[which])

                dialog?.dismiss()
                showIndicatorPropertiesDialog(resources.getStringArray(R.array.indiactor_list)[which])
            }
        dialog = dialogBuilder.create()
        dialog.show()
    }


    fun showComparisonOperatorDialog(position: Int){
        var dialog:AlertDialog? = null
        val dialogBuilder = AlertDialog.Builder(view?.context)
        dialogBuilder.setTitle("Choose Indicator")
            .setItems(R.array.comparison_operator_list
            ) { dialog, which ->
                Log.d("LOG", operatorList[which])
                strategylist[position].comparisonOperator = operatorList[which]
                viewAdapter.notifyDataSetChanged()
                dialog?.dismiss()
            }
        dialog = dialogBuilder.create()
        dialog.show()
    }

    fun editIndicatorDialog(indicator:String,indicatorPosition:Int,position:Int){
        var dialog:AlertDialog? = null
        val dialogBuilder = AlertDialog.Builder(view?.context)
        val inflater = layoutInflater

        val dialogView = when(indicator){
            "MACD" ->inflater.inflate(R.layout.indicator_macd_layout,null)
            "Bollinger Band"->inflater.inflate(R.layout.indicator_bollinger,null)
            "RSI"->inflater.inflate(R.layout.indicator_rsi,null)
            "Alligator"->inflater.inflate(R.layout.indicator_alligator,null)
            else->inflater.inflate(R.layout.indicator_macd_layout,null)
        }

        if(indicatorPosition ==0){
            setIndicatorDialogValues(indicator,dialogView,strategylist[position].firstIndicator.properties)
        }else{
            setIndicatorDialogValues(indicator,dialogView,strategylist[position].secondIndicator.properties)
        }

        val doneButton = dialogView.findViewById(R.id.done) as Button

        doneButton.setOnClickListener {
            if(indicatorPosition ==0){
                val properties = setIndicatorProperties(indicator,dialogView)
                strategylist[position].firstIndicator = Indicator(indicator, properties)
            }else{
                val properties = setIndicatorProperties(indicator,dialogView)
                strategylist[position].secondIndicator = Indicator(indicator, properties)
            }
            dialog?.dismiss()
        }

        dialogBuilder.setView(dialogView)
        dialog = dialogBuilder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    /**
     * <item>RSI</item>
    <item>Bollinger Band</item>
    <item>Alligator</item>
     * */
    private lateinit var indicator1:Indicator
    private lateinit var indicator2:Indicator
    fun showIndicatorPropertiesDialog(indicator:String){
        var dialog:AlertDialog? = null
        val dialogBuilder = AlertDialog.Builder(view?.context)
        val inflater = layoutInflater

        val dialogView = when(indicator){
           "MACD" ->inflater.inflate(R.layout.indicator_macd_layout,null)
            "Bollinger Band"->inflater.inflate(R.layout.indicator_bollinger,null)
            "RSI"->inflater.inflate(R.layout.indicator_rsi,null)
            "Alligator"->inflater.inflate(R.layout.indicator_alligator,null)
            else->inflater.inflate(R.layout.indicator_macd_layout,null)
        }

        if(false){
            val fieldSpinner = dialogView.findViewById<Spinner>(R.id.field_spinner)!!
            val maSpinner = dialogView.findViewById<Spinner>(R.id.ma_type_spinner)!!

        }

        val doneButton = dialogView.findViewById(R.id.done) as Button

        doneButton.setOnClickListener {
            if(showDialogCounter==0){
                showDialogCounter++
                val properties = setIndicatorProperties(indicator,dialogView)
                indicator1 = Indicator(indicator,properties)
                showDialog()
                dialog?.dismiss()
            }else{
                val properties = setIndicatorProperties(indicator,dialogView)

                indicator2 = Indicator(indicator,properties)

                strategylist.add(Strategy(indicator1,indicator2,"<","and"))
                viewAdapter.notifyDataSetChanged()
                showDialogCounter = 0
                dialog?.dismiss()
            }
        }

        dialogBuilder.setView(dialogView)
        dialog = dialogBuilder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    fun setPage(){
        try {
            MyStrategy.setMEntryCondition(strategylist)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun setIndicatorDialogValues(indicator:String,dialogView:View,propertiesMap:MutableMap<Any?,Any?>){
        when(indicator){

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

            else->{Log.d(LOG_TAG,"no indicator for set properties")}
        }
    }

    fun setIndicatorProperties(indicator:String,dialogView:View):MutableMap<Any?,Any?>{
        val propertiesMap :MutableMap<Any?,Any?> = mutableMapOf()
        when(indicator){
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

            else->{Log.d(LOG_TAG,"no indicator for set properties")}
        }

        return propertiesMap
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateStrategyPart2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
