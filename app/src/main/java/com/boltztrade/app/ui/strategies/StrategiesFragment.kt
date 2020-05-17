package com.boltztrade.app.ui.strategies

import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.boltztrade.app.BoltztradeSingleton

import com.boltztrade.app.R
import com.boltztrade.app.SharedPrefKeys
import com.boltztrade.app.apis.ApiService
import com.boltztrade.app.apis.BoltztradeRetrofit
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.callbacks.StrategyCardTouchCallback
import com.boltztrade.app.callbacks.StrategyOpsCallback
import com.boltztrade.app.model.Strategies
import com.boltztrade.app.model.StrategyModel
import com.boltztrade.app.model.Username
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal

class StrategiesFragment : Fragment() {


    private val LOG_TAG = StrategiesFragment::class.java.canonicalName

    private val STRATEGY_INTENT = 1
    companion object {
        fun newInstance() = StrategiesFragment()
    }

    private lateinit var viewModel: StrategiesViewModel
    private lateinit var moveToStrategyActivityButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter : RecyclerView.Adapter<*>
    private lateinit var viewManager : RecyclerView.LayoutManager
    private var strategyList: MutableList<StrategyModel> = mutableListOf()

    private lateinit var animationView: LottieAnimationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.strategies_fragment, container, false)
        animationView = view.findViewById(R.id.strategy_animation_view)
        moveToStrategyActivityButton = view.findViewById(R.id.button_move_to_new_strategy_activity)
        moveToStrategyActivityButton.setOnClickListener {
            startActivityForResult(Intent(activity,StrategyActivity::class.java),STRATEGY_INTENT)
        }
        viewManager = LinearLayoutManager(activity)
        viewAdapter = StrategiesListAdapter(strategyList,object :StrategyOpsCallback{
            override fun backtestResult(position: Int) {
                Log.d(LOG_TAG,"Strategy selected.. $position")
                val disp = BoltztradeRetrofit.getInstance().getBacktestResult("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
                    SharedPrefKeys.boltztradeToken,"")!!}", Strategies.StrategyOpsRequest(strategyList[position]._id?:"",BoltztradeSingleton.mSharedPreferences.getString(
                    SharedPrefKeys.boltztradeUser,"")!!)).
                    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Log.d(LOG_TAG,it.toString())
                    showBacktestResult(it)
//                    else Toast.makeText(activity,"no backtest found",Toast.LENGTH_LONG).show()
                },{
                    it.printStackTrace()
                },{
                    Log.i(LOG_TAG,"Get All Strategies Call Completed..")
                })
            }

            override fun backtest(position: Int) {
                val disp = BoltztradeRetrofit.getInstance().backtest("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
                    SharedPrefKeys.boltztradeToken,"")!!}", Strategies.BacktestOpsRequest(strategyList[position]._id?:"",BoltztradeSingleton.mSharedPreferences.getString(
                    SharedPrefKeys.boltztradeUser,"")!!,"1554091231000","1586271031000")).
                    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Toast.makeText(activity,it,Toast.LENGTH_LONG).show()
                    Log.d(LOG_TAG,it.toString())
                },{
                    Toast.makeText(activity,"Something Went wrong, Please try again",Toast.LENGTH_LONG).show()
                    it.printStackTrace()
                },{
                    Log.i(LOG_TAG,"backtest request completed")

                })
            }

            override fun deploy(position: Int) {
                val disp = BoltztradeRetrofit.getInstance().deploy("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
                    SharedPrefKeys.boltztradeToken,"")!!}", Strategies.StrategyOpsRequest(strategyList[position]._id?:"",BoltztradeSingleton.mSharedPreferences.getString(
                    SharedPrefKeys.boltztradeUser,"")!!)).
                    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Toast.makeText(activity,it,Toast.LENGTH_LONG).show()
                    Log.d(LOG_TAG,it.toString())
                },{
                    it.printStackTrace()
                    Toast.makeText(activity,"Something Went wrong, Please try again",Toast.LENGTH_LONG).show()
                },{
                    Log.i(LOG_TAG,"Dep Completed..")
                })
            }

            override fun delete(position: Int) {
                // delete api call
            }

            override fun edit(position: Int) {
                getStrategyFromStrategyId(strategyId = strategyList[position]._id?:"test")
            }

            override fun enter(position: Int) {
                //don't need to implement this for strategy Fragment
            }

            override fun exit(position: Int) {
                //don't need to implement this for strategy Fragment
            }

            override fun snooze(position: Int) {
                //don't need to implement this for strategy Fragment
            }

        })
        recyclerView = (view.findViewById(R.id.strategies_recycler_view) as RecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

        getUserStrategies()


        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                Log.d(LOG_TAG, "onMoveCalled")
                return true
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.d(LOG_TAG, "onSwipedCalled.... on direction ... $direction")
                val position = viewHolder.adapterPosition
                val item  = strategyList[viewHolder.adapterPosition]
                val disposible =   Observable.create(ObservableOnSubscribe<Boolean> {
                    deleteStrategy(strategyList[position]._id?:"random")
                    strategyList.removeAt(position)
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

        return view
    }


    private fun getStrategyFromStrategyId(strategyId:String){
        Log.d("strategy ID",strategyId)
        val disp = BoltztradeRetrofit.getInstance().getStrategyFromId("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", ApiService.StrategyIdBody(strategyId)
        )?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe({
            Log.d(LOG_TAG, "strategy response$it")
            val gson = Gson()
            val strategy = gson.toJson(it)
            val intent = Intent(activity,StrategyActivity::class.java)
            intent.putExtra("strategy",strategy)
            startActivityForResult(intent,STRATEGY_INTENT)
//            strategyList.add(it)
//            viewAdapter.notifyDataSetChanged()
        },{
            Log.e("error","while calling get strategy with id")
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get All Strategies Call Completed..")
        })
    }

    fun getUserStrategies(){
        Log.d(LOG_TAG,"Fetching User strategies")
        val disp = BoltztradeRetrofit.getInstance().getUserStrategies("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", Username(
            BoltztradeSingleton.mSharedPreferences.getString(
                SharedPrefKeys.boltztradeUser, "")!!)).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            if(it.isEmpty()){
                animationView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }else{
                animationView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                Log.d(LOG_TAG,it.toString())
                strategyList.clear()
                strategyList.addAll(it)
                viewAdapter.notifyDataSetChanged()
            }

        },{
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get All Strategies Call Completed..")
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("onActivityforResult","$requestCode,,$resultCode")
        when(requestCode){
            STRATEGY_INTENT->{
                Log.d(LOG_TAG,"fetch user strategies again")
                getUserStrategies()
            }
        }
    }

    fun deleteStrategy(strategyId:String){
        val disp = BoltztradeRetrofit.getInstance().deleteStrategy("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", Strategies.StrategyOpsRequest(strategyId,BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeUser,"")!!)).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
        },{
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get All Strategies Call Completed..")
        })
    }

    fun showBacktestResult(backtestModel: Strategies.BacktestModel){
        var dialog: AlertDialog? = null
        val dialogBuilder = AlertDialog.Builder(view?.context)
        val inflater = layoutInflater

        val dialogView=inflater.inflate(R.layout.dialog_backtest_result,null)
        val editButton = dialogView.findViewById(R.id.deploy_button) as Button

        val profitPercent = (backtestModel.averageProfitableTrade?.toBigDecimal()?.multiply(BigDecimal(100)))?.toInt()
        val lossPercent = 100.minus(profitPercent?:0)

        val winningTradeTextView :TextView = dialogView.findViewById(R.id.winning_trades)
        val netProfitTextView :TextView = dialogView.findViewById(R.id.net_profit)
        val winPercentageTextView:TextView = dialogView.findViewById(R.id.win_percentage)

        val lossingTradeTextView :TextView = dialogView.findViewById(R.id.lossing_trade)
        val netLossTextView :TextView = dialogView.findViewById(R.id.net_loss)
        val lossPercentageTextView:TextView = dialogView.findViewById(R.id.loss_percentage)

        val expectedShorFallTextView:TextView = dialogView.findViewById(R.id.expected_short_fall)
        val maximumDrawDownTextView:TextView  = dialogView.findViewById(R.id.maximum_draw_down)
        val rewardRiskRatioTextView:TextView = dialogView.findViewById(R.id.reward_risk_ratio)
        val strategyBuyvsHoldTextView:TextView = dialogView.findViewById(R.id.strategy_vs_buy_hold)

        winPercentageTextView.setText("$profitPercent %")
        lossPercentageTextView.setText("$lossPercent %")
        netProfitTextView.setText("Net Profit: ₹ ${backtestModel.grossProfit}")
        netLossTextView.setText("Net Loss: ₹ ${backtestModel.grossLoss}")
        winningTradeTextView.setText("Num. Of Winning Trades: ${backtestModel.numOfWinningTrade}")
        lossingTradeTextView.setText("Num. Of Lossing Trades: ${backtestModel.numOfLosingTrade}")
        val expectedShortfall = backtestModel.expectedShortFall?.split(".")?: mutableListOf("${backtestModel.expectedShortFall}")
        val maximumDrawDown = backtestModel.maximumDrawdown?.split(".")?: mutableListOf("${backtestModel.maximumDrawdown}")
        val rewardRiskRatio = backtestModel.rewardRiskRatio?.split(".")?: mutableListOf("${backtestModel.rewardRiskRatio}")
        val buyVsHold = backtestModel.buyvsHold?.split(".")?: mutableListOf("${backtestModel.buyvsHold}")
        if(expectedShortfall.size > 1){
            expectedShorFallTextView.setText("Expected Short Fall: ${expectedShortfall[0]}.${expectedShortfall[1].substring(0,4)}")
        }else{
            expectedShorFallTextView.setText("Expected Short Fall: ${expectedShortfall[0]}")
        }

        if(maximumDrawDown.size > 1){
            maximumDrawDownTextView.setText("Maximum Drawdown: ${maximumDrawDown[0]}.${maximumDrawDown[1].substring(0,4)}")
        }else{
            maximumDrawDownTextView.setText("Maximum Drawdown: ${maximumDrawDown[0]}")
        }
        if(rewardRiskRatio.size > 1){
            rewardRiskRatioTextView.setText("Reward Risk Ratio: ${rewardRiskRatio[0]}.${rewardRiskRatio[1].substring(0,4)}")
        }else{
            rewardRiskRatioTextView.setText("Reward Risk Ratio: ${rewardRiskRatio[0]}")
        }
        if(buyVsHold.size > 1){
            strategyBuyvsHoldTextView.setText("Strategy vs Buy_and_hold: ${buyVsHold[0]}.${buyVsHold[1].substring(0,4)}")
        }else{
            strategyBuyvsHoldTextView.setText("Strategy vs Buy_and_hold: ${buyVsHold[0]}")
        }


       editButton.setOnClickListener {
            dialog?.dismiss()
            getStrategyFromStrategyId(strategyId = backtestModel.strategyID?:"test")
        }

        dialogBuilder.setView(dialogView)
        dialog = dialogBuilder.create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StrategiesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
