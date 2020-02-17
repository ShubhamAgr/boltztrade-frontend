package com.boltztrade.app.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.BoltztradeSingleton
import com.boltztrade.app.R
import com.boltztrade.app.SharedPrefKeys
import com.boltztrade.app.apis.BoltztradeRetrofit
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.callbacks.StrategyOpsCallback
import com.boltztrade.app.model.StrategyModel
import com.boltztrade.app.model.Username
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeFragment : Fragment() {
    private val LOG_TAG = HomeFragment::class.java.canonicalName
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter : RecyclerView.Adapter<*>
    private lateinit var viewManager : RecyclerView.LayoutManager
    private var strategyList: MutableList<StrategyModel> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.text.observe(this, Observer {
//            textView.text = it
        })
        viewManager = LinearLayoutManager(activity)
        viewAdapter = HomeStrategyListAdapter(strategyList,object :StrategyOpsCallback{
            override fun backtestResult(position: Int) {
                //don't need to implement this for strategy Fragment
            }

            override fun backtest(position: Int) {
                //don't need to implement this for strategy Fragment
            }

            override fun deploy(position: Int) {
                //don't need to implement this for strategy Fragment
            }

            override fun delete(position: Int) {
                //don't need to implement this for strategy Fragment
            }

            override fun enter(position: Int) {
                //implement user entered to strategy
            }

            override fun exit(position: Int) {
                //implement user exited to strategy
            }

            override fun snooze(position: Int) {
                // implement user snoozed the strategy...
            }

        })
        recyclerView = (view.findViewById(R.id.home_strategies_recycler_view) as RecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }
        val disp = BoltztradeRetrofit.getInstance().getUserStrategies("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", Username(
            BoltztradeSingleton.mSharedPreferences.getString(
                SharedPrefKeys.boltztradeUser, "")!!)
        ).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
            strategyList.clear()
            strategyList.addAll(it)
            viewAdapter.notifyDataSetChanged()
        },{
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get All Strategies Call Completed..")
        })
        return view
    }
}