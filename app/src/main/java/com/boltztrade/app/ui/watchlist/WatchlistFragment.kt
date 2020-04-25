package com.boltztrade.app.ui.watchlist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.BoltztradeSingleton

import com.boltztrade.app.R
import com.boltztrade.app.SharedPrefKeys
import com.boltztrade.app.apis.BoltztradeRetrofit
import com.boltztrade.app.apis.KiteRetrofit
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.model.*
import com.boltztrade.app.ui.strategies.InstrumentListAdapter
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.*

class WatchlistFragment : Fragment() {

    private val LOG_TAG = WatchlistFragment::class.java.canonicalName
    companion object {
        fun newInstance() = WatchlistFragment()
    }

    private lateinit var viewModel: WatchlistViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter : RecyclerView.Adapter<*>
    private lateinit var viewManager : RecyclerView.LayoutManager

    private lateinit var recyclerView2: RecyclerView
    private lateinit var viewAdapter2 : RecyclerView.Adapter<*>
    private lateinit var viewManager2 : RecyclerView.LayoutManager
    private lateinit var instrumentSearchView: SearchView
    private val instrumentSearchList :MutableList<Instrument> = mutableListOf()
    private val userWatchlist :MutableList<Instrument> = mutableListOf()
    private val instrumentList:MutableList<String> = mutableListOf()
    private val instrumentOHLCQuote:MutableMap<String,BrokerService.OHLCQuote> = mutableMapOf()
    private lateinit var selectedInstrument: Instrument
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.watchlist_fragment, container, false)
        viewManager = LinearLayoutManager(activity)
        viewAdapter = WatchListAdapter(userWatchlist,instrumentOHLCQuote,object :RecyclerviewSelectedPositionCallback{
            override fun itemSelected(position: Int) {
                Log.d(LOG_TAG,"watchlist item clicked $position")
            }
        })
        instrumentSearchView = view.findViewById(R.id.searchView)
        recyclerView = (view.findViewById(R.id.watchlist_recycler_view) as RecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }
        viewManager2 = LinearLayoutManager(activity)
        viewAdapter2 = InstrumentListAdapter(instrumentSearchList,object :
            RecyclerviewSelectedPositionCallback {
            override fun itemSelected(position: Int) {
                selectedInstrument = instrumentSearchList[position]
                addUserWatchlist(selectedInstrument.instrument_token!!)
                visibility(false)
                instrumentSearchView.setQuery("",false)
                instrumentSearchView.clearFocus()

            }

        })
        recyclerView2 = (view.findViewById(R.id.watchlist_instruments_recycler_view) as RecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager2
            adapter = viewAdapter2

        }
        instrumentSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
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

        instrumentSearchView.setOnCloseListener(object : SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                Log.d(LOG_TAG,"search view onclose")
                return true
            }

        })
        instrumentSearchView.setOnQueryTextFocusChangeListener(object :View.OnFocusChangeListener{
            override fun onFocusChange(p0: View?, b: Boolean) {
                if(b){
                    Log.d(LOG_TAG,"Focused")

                }else{
                    Log.d(LOG_TAG,"Not Focused")
                }
            }

        })

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                Log.d(LOG_TAG, "onMoveCalled")
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.d(LOG_TAG, "onSwipedCalled.... on direction ... $direction")
                val position = viewHolder.adapterPosition
                val item  = userWatchlist[viewHolder.adapterPosition]
                val disposible =   Observable.create(ObservableOnSubscribe<Boolean> {
                    removeUserWatchlist(userWatchlist[position].instrument_token!!)
                    userWatchlist.removeAt(position)
                    it.onNext(true)
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({

                    if(it) {
                        Log.d(LOG_TAG,"notification deleted...")
                        viewAdapter.notifyDataSetChanged()

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
        getUserWatchlist()
        visibility(false)
        return view
    }


    fun getList(s:String){
        val disposible = Observable.create(ObservableOnSubscribe<List<Instrument>> {
            val list = BoltztradeSingleton.mDatabase.instrumentDao().instrumentList(s)
            it.onNext(list)
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            instrumentSearchList.clear()
            instrumentSearchList.addAll(it)
            viewAdapter2.notifyDataSetChanged()
            Log.d("instrumentsearch list","$instrumentSearchList")
        },{
            Log.d(LOG_TAG,"something went wrong while adding notification...")
            it.printStackTrace()
        },{ Log.d(LOG_TAG,"onComplete")})
        }

    fun visibility(boolean: Boolean){
        if(boolean){
            recyclerView2.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }else{
            recyclerView2.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
    fun searchText(data:String){
        val disp = BoltztradeRetrofit.getInstance().getInstrumentList("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", InstrumentRegex(data)
        ).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
            if(it.size>0){
                instrumentSearchList.clear()
                instrumentSearchList.addAll(it)
                viewAdapter2.notifyDataSetChanged()
            }
        },{
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get instrument search Call")
        })
    }

    fun getUserWatchlist(){
        val disp = BoltztradeRetrofit.getInstance().getUserWatchList("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", Username(BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeUser,"")!!)
        ).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
            for( i in it) getInstrumentDetail(i)
            instrumentList.clear()
            instrumentList.addAll(it)
            handler.postDelayed(callQuoteApi(it),500)
        },{
            it.printStackTrace()

        },{
            Log.i(LOG_TAG,"Get instrument search Call")
        })
    }

    fun getInstrumentDetail(token:String){
        val disp = BoltztradeRetrofit.getInstance().getInstrumentToken("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", InstrumentFlow.GetInstrumentModel(token)
        ).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
            userWatchlist.addAll(it)
//            viewAdapter.notifyDataSetChanged()

        },{
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get instrument search Call")
        })
    }

    fun addUserWatchlist(token:String){
        val disp = BoltztradeRetrofit.getInstance().addToUserWatchList("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", InstrumentFlow.AddInstrumentModel(BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeUser,"")!!,token)
        ).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
            userWatchlist.clear()
            viewAdapter.notifyDataSetChanged()
            getUserWatchlist()
        },{
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get instrument search Call")
        })
    }


    fun removeUserWatchlist(token:String){
        val disp = BoltztradeRetrofit.getInstance().removeInstrumentFromWatchList("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", InstrumentFlow.AddInstrumentModel(BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeUser,"")!!,token)
        ).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
        },{
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get instrument search Call")
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        if(instrumentList.isNotEmpty()){
            handler.postDelayed(callQuoteApi(instrumentList),500)
        }
    }

    /**
     * handler.postDelayed(callTimerTask(),2000)
     * handler.removeCallbacksAndMessages(null)
     * */

    val handler = Handler()
    fun callQuoteApi(instruments:MutableList<String>):Runnable{
        return object: Runnable {
            override fun run() {
                try {
                    Log.d(LOG_TAG,"Quote api called")
                    val disp = BoltztradeRetrofit.getInstance().getOhlcData("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
                        SharedPrefKeys.boltztradeToken,"")!!}", BrokerService.KiteDataRequest(instruments)).
                        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                        Log.d("data",it.toString())
                        instrumentOHLCQuote.clear()
                        instrumentOHLCQuote.putAll(it)
                        viewAdapter.notifyDataSetChanged()
//                        for(i in it){
//                            instrumentOHLCQuote[i.instrumentToken.toString()] = i
//                        }
                    },{
                        it.printStackTrace()
                    },{
                        Log.i(LOG_TAG,"Get instrument search Call")
                    })
                }catch (e:Exception){
                    e.printStackTrace()
                }
                handler.postDelayed(this,60000)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WatchlistViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
