package com.boltztrade.app.ui.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.BoltztradeSingleton
import com.boltztrade.app.MainActivity
import com.boltztrade.app.R
import com.boltztrade.app.SharedPrefKeys
import com.boltztrade.app.apis.ApiService
import com.boltztrade.app.apis.BoltztradeRetrofit
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.callbacks.StrategyOpsCallback
import com.boltztrade.app.model.DeployModel
import com.boltztrade.app.model.StrategyModel
import com.boltztrade.app.model.Username
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*





class HomeFragment : Fragment() {
    private val LOG_TAG = HomeFragment::class.java.canonicalName
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter : RecyclerView.Adapter<*>
    private lateinit var viewManager : RecyclerView.LayoutManager
    private var strategyList: MutableList<StrategyModel> = mutableListOf()

    private lateinit var strategyCreatedValue:TextView
    private lateinit var strategyBacktestValue:TextView
    private lateinit var strategyInDeploymentValue:TextView
    private lateinit var entryConditionMetValue:TextView
    private lateinit var todaysProfitValue:TextView
    private lateinit var totalProfitEarnedValue:TextView
    private lateinit var noStrategiesDeployedView:ConstraintLayout
    private lateinit var cloudImage:ImageView
    private val deployedStrategyList:MutableMap<String,DeployModel> = mutableMapOf()
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

        noStrategiesDeployedView = view.findViewById(R.id.no_strategy_in_deployment)
        noStrategiesDeployedView.setOnClickListener {(activity as MainActivity).switchToStrategies() }
        cloudImage = view.findViewById(R.id.cloud_image)
        strategyCreatedValue = view.findViewById(R.id.strategy_created_value)
        strategyBacktestValue = view.findViewById(R.id.strategy_backtest_value)
        strategyInDeploymentValue = view.findViewById(R.id.strategy_in_deployment_value)
        entryConditionMetValue = view.findViewById(R.id.entry_condition_met_value)
        todaysProfitValue = view.findViewById(R.id.todays_profit_value)
        totalProfitEarnedValue = view.findViewById(R.id.total_profit_earned_value)

        viewManager = LinearLayoutManager(activity)
        viewAdapter = HomeStrategyListAdapter(deployedStrategyList,object :StrategyOpsCallback{
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

            override fun edit(position: Int) {
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
        getStrategyList()
        getTotalStrategyCreatedCount()
        getTotalStrategyBacktestCount()
        getTotalEntryConditionMetCount()
        getTodaysProfit()
        getTotalProfit()

        return view
    }



    private fun getStrategyList(){
        val disp = BoltztradeRetrofit.getInstance().getUserStrategies("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", Username(
            BoltztradeSingleton.mSharedPreferences.getString(
                SharedPrefKeys.boltztradeUser, "")!!)
        ).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
            strategyCreatedValue.text = it.size.toString()
            deployedStrategyList.clear()
            strategyList.clear()
            strategyList.addAll(it)
            getDeployedStrategy()
//            viewAdapter.notifyDataSetChanged()
        },{
            Log.e("error","while calling get strategy list")
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get All Strategies Call Completed..")
        })
    }

    private fun getTotalStrategyCreatedCount(){
        val disp = BoltztradeRetrofit.getInstance().getTotalStrategyCreatedCount("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", ApiService.MUsername(
            BoltztradeSingleton.mSharedPreferences.getString(
                SharedPrefKeys.boltztradeUser, "")!!)
        ).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
        },{
            Log.e("error","while calling get strategy created count")
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get total strategy created count Completed..")
        })
    }

    private fun getTotalStrategyBacktestCount(){
        val disp = BoltztradeRetrofit.getInstance().getTotalStrategyBacktestCount("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", ApiService.MUsername(
            BoltztradeSingleton.mSharedPreferences.getString(
                SharedPrefKeys.boltztradeUser, "")!!)
        ).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            strategyBacktestValue.text = it.totalBacktestDone.toString()
            Log.d(LOG_TAG,it.toString())
        },{
            Log.e("error","while calling get strategy backtest count")
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get total backtest count Completed..")
        })
    }

    private fun getDeployedStrategy(){
        val disp = BoltztradeRetrofit.getInstance().getDeployedStrategies("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", ApiService.RequestWithTimeStamp(
            BoltztradeSingleton.mSharedPreferences.getString(
                SharedPrefKeys.boltztradeUser, "")!!,getTradingDayTimeStampInUtc())
        ).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            if(it.isEmpty()){
                cloudImage.visibility = View.VISIBLE
                noStrategiesDeployedView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }else{

                cloudImage.visibility = View.GONE
                noStrategiesDeployedView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                strategyInDeploymentValue.text = it.size.toString()
                for(deployment in it){
                    for(strategy in strategyList){
                        if(strategy._id == deployment.strategyId){
                            deployedStrategyList[strategy.algoName] = deployment
                            viewAdapter.notifyDataSetChanged()
                        }
                    }
                }

                Log.e("strategyInDeployment","${deployedStrategyList.size}")
            }
            Log.d(LOG_TAG,it.toString())
        },{
            Log.e("error","while calling get deployed strategy")
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get Deployed Strategy Completed..")
        })
    }

    private fun getTotalEntryConditionMetCount(){
        val disp = BoltztradeRetrofit.getInstance().getTotalEntryConditionMetCount("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", Username(
            BoltztradeSingleton.mSharedPreferences.getString(
                SharedPrefKeys.boltztradeUser, "")!!)
        ).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            entryConditionMetValue.text = it.entryConditionMet.toString()
            Log.d(LOG_TAG,it.toString())
        },{
            Log.e("error","while Total entry condtion met")
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get total entry condition met Completed..")
        })
    }

    private fun getTodaysProfit(){
        val disp = BoltztradeRetrofit.getInstance().getTodaysProfit("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", ApiService.RequestWithTimeStamp(
            BoltztradeSingleton.mSharedPreferences.getString(
                SharedPrefKeys.boltztradeUser, "")!!,getTradingDayTimeStampInUtc())
        ).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            todaysProfitValue.setText("₹ ${it.totalProfit}")

            Log.d(LOG_TAG,it.toString())
        },{
            Log.e("error","while calling get todays profit")
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get todays profit Completed..")
        })
    }

    private fun getTotalProfit(){
        val disp = BoltztradeRetrofit.getInstance().getTotalProfitEarned("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", Username(
            BoltztradeSingleton.mSharedPreferences.getString(
                SharedPrefKeys.boltztradeUser, "")!!)
        ).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({

            if(it.totalProfit?.toString().contains(".")){
                val profit = it.totalProfit.toString().split(".")
                val secondPart = if(profit[1].length>0) profit[1].substring(0,2) else "00"
                totalProfitEarnedValue.setText("₹ ${profit[0]}.$secondPart")
            }else{
                totalProfitEarnedValue.setText("₹ ${it.totalProfit}")
            }
            Log.d(LOG_TAG,it.toString())
        },{
            Log.e("error","while calling get total profit")
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get total profit Completed..")
        })
    }

    private fun getTradingDayTimeStampInUtc():Long{
        val calendar = Calendar.getInstance()
        val today = calendar.getTime()
        val todayString =  SimpleDateFormat("yyyy-MM-dd").format(today)
        Log.d("Today Date",todayString)
        val offset = TimeZone.getDefault().rawOffset + TimeZone.getDefault().dstSavings
        val tradingDay = today.time - offset
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("Log","${Instant.now().toEpochMilli()}")
        }
        Log.d("Trading day" ,"${tradingDay}")

        return 1582546795272
    }
}