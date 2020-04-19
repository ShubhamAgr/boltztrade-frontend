package com.boltztrade.app.ui.strategies

import android.app.AlertDialog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.boltztrade.app.R
import com.boltztrade.app.callbacks.IndicatorListDialogCallback
import com.boltztrade.app.callbacks.ShowIndicatorPropertiesDialogCallback
import com.boltztrade.app.callbacks.StrategyCardTouchCallback
import com.boltztrade.app.model.Indicator
import com.boltztrade.app.model.Strategy
import com.boltztrade.app.ui.dialog.IndicatorListDialog
import com.boltztrade.app.ui.dialog.ShowIndicatorPropertiesDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CreateStrategyPart2_2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val LOG_TAG = CreateStrategyPart2_2::class.java.canonicalName
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter : RecyclerView.Adapter<*>
    private lateinit var viewManager : RecyclerView.LayoutManager

    private val strategylist:MutableList<Strategy> = mutableListOf()
    private val operatorList:MutableList<String> = mutableListOf("crossUp","crossDown","lessThan","greaterThan","equal")
    private val optionList:MutableMap<String,MutableList<String>> = mutableMapOf()
    private lateinit var createStrategyFAB : FloatingActionButton
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
       val view = inflater.inflate(R.layout.fragment_create_strategy_part3, container, false)
        createStrategyFAB = view.findViewById(R.id.createStrategyFab)

        createStrategyFAB.setOnClickListener {
            showDialog()
        }

        viewManager = LinearLayoutManager(activity)

        viewAdapter = CreateStrategyListAdapter(strategylist,object : StrategyCardTouchCallback {
            override fun onSelectFirstIndicator(position: Int) {
                Log.d(LOG_TAG,"selected first indicator $position")
                editIndicatorDialog(strategylist.get(position).firstIndicator.name,0,position)
            }

            override fun onSelectSecondIndicator(position: Int) {
                Log.d(LOG_TAG,"selected second indicator $position")
                val secondIndicator = strategylist.get(position).secondIndicator?.name
                if(secondIndicator!=null){
                    editIndicatorDialog(secondIndicator,1,position)
                }
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
        var indicatorListDialog: IndicatorListDialog? = null
        indicatorListDialog =  IndicatorListDialog(object : IndicatorListDialogCallback {
            override fun indicatorName(name: String) {
                showIndicatorPropertiesDialog(name)
                indicatorListDialog?.dismiss()
            }
        })

        indicatorListDialog.show(fragmentManager?.beginTransaction()!!,"dialog")
    }


    fun showComparisonOperatorDialog(position: Int){
        var dialog: AlertDialog? = null
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
        var dialog:ShowIndicatorPropertiesDialog? = null
        dialog = if(indicatorPosition ==0){  ShowIndicatorPropertiesDialog(indicator,strategylist[position].firstIndicator.properties,object :ShowIndicatorPropertiesDialogCallback{
            override fun getProperties(properties: MutableMap<Any?, Any?>) {
                if(indicatorPosition ==0){
                    strategylist[position].firstIndicator = Indicator(indicator, properties)
                }else{
                    strategylist[position].secondIndicator = Indicator(indicator, properties)
                }
                dialog?.dismiss()
            }
        })
        }else {
            val secondIndicator = strategylist[position].secondIndicator?.properties
            if (secondIndicator != null) {
                ShowIndicatorPropertiesDialog(indicator,secondIndicator ,
                    object : ShowIndicatorPropertiesDialogCallback {
                        override fun getProperties(properties: MutableMap<Any?, Any?>) {
                            if (indicatorPosition == 0) {
                                strategylist[position].firstIndicator =
                                    Indicator(indicator, properties)
                            } else {
                                strategylist[position].secondIndicator =
                                    Indicator(indicator, properties)
                            }
                            dialog?.dismiss()
                        }
                    })
            }else{
                null
            }
        }
        dialog?.show(fragmentManager?.beginTransaction()!!,"")
    }

    /**
     * <item>RSI</item>
    <item>Bollinger Band</item>
    <item>Alligator</item>
     * */
    private lateinit var indicator1: Indicator
    private lateinit var indicator2: Indicator
    fun showIndicatorPropertiesDialog(indicator:String){
        var dialog: ShowIndicatorPropertiesDialog? = null
        dialog = ShowIndicatorPropertiesDialog(indicator,null,object :
            ShowIndicatorPropertiesDialogCallback {
            override fun getProperties(properties: MutableMap<Any?, Any?>) {
                if(showDialogCounter==0){
                    showDialogCounter++
                    indicator1 = Indicator(indicator,properties)
                    dialog?.dismiss()
                    showDialog()
                }else{
                    indicator2 = Indicator(indicator,properties)
                    strategylist.add(Strategy(firstIndicator = indicator1,secondIndicator = indicator2,comparisonOperator = "crossUp",logicalOperator = "and"))
                    viewAdapter.notifyDataSetChanged()
                    showDialogCounter = 0
                    dialog?.dismiss()
                }
            }
        })

        dialog.show(fragmentManager?.beginTransaction()!!,"")
    }
    fun setPage(){
        try {
            MyStrategy.setMEntryCondition(strategylist)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateStrategyPart3.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateStrategyPart2_2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
