package com.boltztrade.app.ui.watchlist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.BoltztradeSingleton

import com.boltztrade.app.R
import com.boltztrade.app.SharedPrefKeys
import com.boltztrade.app.apis.BoltztradeRetrofit
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.model.Instrument
import com.boltztrade.app.model.InstrumentFlow
import com.boltztrade.app.model.InstrumentRegex
import com.boltztrade.app.model.Username
import com.boltztrade.app.ui.strategies.InstrumentListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
    private lateinit var selectedInstrument: Instrument
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.watchlist_fragment, container, false)
        viewManager = LinearLayoutManager(activity)
        viewAdapter = WatchListAdapter(userWatchlist,object :RecyclerviewSelectedPositionCallback{
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
        visibility(false)

        instrumentSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(LOG_TAG,"Query Submitted $query")
                searchText(query?:"none")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(LOG_TAG,"Text change $newText")
                if(newText?.length!! >= 4){
                    searchText(newText)
                }else{
                    visibility(false)

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

        getUserWatchlist()
        return view
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
                visibility(true)
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
            viewAdapter.notifyDataSetChanged()

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
            getUserWatchlist()
        },{
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get instrument search Call")
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WatchlistViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
