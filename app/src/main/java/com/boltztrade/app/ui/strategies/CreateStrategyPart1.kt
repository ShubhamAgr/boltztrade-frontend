package com.boltztrade.app.ui.strategies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.BoltztradeSingleton


import com.boltztrade.app.R
import com.boltztrade.app.SharedPrefKeys
import com.boltztrade.app.apis.BoltztradeRetrofit
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.model.Instrument
import com.boltztrade.app.model.InstrumentRegex
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CreateStrategyPart1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var instrumentSearchView: SearchView
    private lateinit var strategyNameEditText: EditText
    private lateinit var strategyQuantityEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter : RecyclerView.Adapter<*>
    private lateinit var viewManager : RecyclerView.LayoutManager

    private lateinit var buyPositionButton:Button
    private lateinit var sellPositionButton: Button
    private lateinit var selectedInstrument: Instrument
    private var strategyPosition = "Buy"
    private val instrumentSearchList :MutableList<Instrument> = mutableListOf()
    private val LOG_TAG = CreateStrategyPart1::class.java.canonicalName
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
        val view =  inflater.inflate(R.layout.fragment_create_strategy_part1, container, false)

        instrumentSearchView = view.findViewById(R.id.searchView)
        strategyNameEditText = view.findViewById(R.id.strategyName)
        strategyQuantityEditText = view.findViewById(R.id.quantity)
        viewManager = LinearLayoutManager(activity)


        buyPositionButton = view.findViewById(R.id.buy_position)
        sellPositionButton = view.findViewById(R.id.sell_position)

        buyPositionButton.setOnClickListener {
            buyPositionButton.setBackgroundResource(R.drawable.buy_button_selected)
            buyPositionButton.setTextColor(ContextCompat.getColor(activity?.applicationContext!!,R.color.deep_saffron))
            sellPositionButton.setBackgroundResource(R.drawable.sell_button_unselected)
            sellPositionButton.setTextColor(ContextCompat.getColor(activity?.applicationContext!!,R.color.white))
            strategyPosition = "Buy"
        }

        sellPositionButton.setOnClickListener {
            buyPositionButton.setBackgroundResource(R.drawable.buy_button_unselected)
            sellPositionButton.setBackgroundResource(R.drawable.sell_button_selected)
            buyPositionButton.setTextColor(ContextCompat.getColor(activity?.applicationContext!!,R.color.white))
            sellPositionButton.setTextColor(ContextCompat.getColor(activity?.applicationContext!!,R.color.deep_saffron))
            strategyPosition = "Sell"
        }
        viewAdapter = InstrumentListAdapter(instrumentSearchList,object :RecyclerviewSelectedPositionCallback{
            override fun itemSelected(position: Int) {
                selectedInstrument = instrumentSearchList[position]
                visibility(false)
                instrumentSearchView.queryHint = selectedInstrument.name
                instrumentSearchView.setQuery("",false)
                instrumentSearchView.clearFocus()
            }
        })

        recyclerView = (view.findViewById(R.id.instruments_recycler_view) as RecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        visibility(false)

        instrumentSearchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(LOG_TAG,"Query Submitted $query")
               if(query?.length!! != 0){
                   getList(query?:"")
               }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(LOG_TAG,"Text change $newText")
                if(newText?.length!! == 0){
                    visibility(false)
                }else{
                    visibility(true)
                    getList(newText)

                }
                return true
            }

        })

        instrumentSearchView.setOnCloseListener {
            Log.d(LOG_TAG,"search view onclose")
            true
        }

        instrumentSearchView.setOnQueryTextFocusChangeListener { p0, b ->
            if(b){
                Log.d(LOG_TAG,"Focused")

            }else{
                Log.d(LOG_TAG,"Not Focused")
            }
        }
        return view
    }

    fun setPage(){
        try{
            MyStrategy.setMSelectedInstrument(selectedInstrument)
            MyStrategy.setMAlgoName(strategyNameEditText.text.toString())
            MyStrategy.setMQuantity(strategyQuantityEditText.text.toString().toDouble())
            MyStrategy.setMPosition(strategyPosition)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun visibility(boolean: Boolean){
        if(boolean){
            recyclerView.visibility = View.VISIBLE
            strategyNameEditText.visibility = View.GONE
            strategyQuantityEditText.visibility = View.GONE
            buyPositionButton.visibility = View.GONE
            sellPositionButton.visibility = View.GONE
        }else{
            recyclerView.visibility = View.GONE
            strategyNameEditText.visibility = View.VISIBLE
            strategyQuantityEditText.visibility = View.VISIBLE
            buyPositionButton.visibility = View.VISIBLE
            sellPositionButton.visibility = View.VISIBLE
        }
    }

    fun getList(s:String){
        val disposible = Observable.create(ObservableOnSubscribe<List<Instrument>> {
            val list = BoltztradeSingleton.mDatabase.instrumentDao().instrumentList(s)
            it.onNext(list)
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            instrumentSearchList.clear()
            instrumentSearchList.addAll(it)
            viewAdapter.notifyDataSetChanged()
            Log.d("instrumentsearch list","$instrumentSearchList")
        },{
            Log.d(LOG_TAG,"something went wrong while adding notification...")
            it.printStackTrace()
        },{ Log.d(LOG_TAG,"onComplete")})
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateStrategyPart1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
