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
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.BoltztradeSingleton

import com.boltztrade.app.R
import com.boltztrade.app.SharedPrefKeys
import com.boltztrade.app.apis.BoltztradeRetrofit
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.callbacks.StrategyCardTouchCallback
import com.boltztrade.app.model.StrategyModel
import com.boltztrade.app.model.Username
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StrategiesFragment : Fragment() {

    private val LOG_TAG = StrategiesFragment::class.java.canonicalName
    companion object {
        fun newInstance() = StrategiesFragment()
    }

    private lateinit var viewModel: StrategiesViewModel
    private lateinit var moveToStrategyActivityButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter : RecyclerView.Adapter<*>
    private lateinit var viewManager : RecyclerView.LayoutManager
    private var strategyList: MutableList<StrategyModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.strategies_fragment, container, false)
        moveToStrategyActivityButton = view.findViewById(R.id.button_move_to_new_strategy_activity)
        moveToStrategyActivityButton.setOnClickListener {
            activity?.startActivity(Intent(activity,StrategyActivity::class.java))
        }
        viewManager = LinearLayoutManager(activity)
        viewAdapter = StrategiesListAdapter(strategyList,object :RecyclerviewSelectedPositionCallback{
            override fun itemSelected(position: Int) {
                Log.d(LOG_TAG,"Strategy selected.. $position")
                showBacktestResult()
            }
        })
        recyclerView = (view.findViewById(R.id.strategies_recycler_view) as RecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

        val disp = BoltztradeRetrofit.getInstance().getUserStrategies("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", Username(
            BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeUser, "")!!)).
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

    fun showBacktestResult(){
        var dialog: AlertDialog? = null
        val dialogBuilder = AlertDialog.Builder(view?.context)
        val inflater = layoutInflater

        val dialogView=inflater.inflate(R.layout.dialog_backtest_result,null)
        val deployButton = dialogView.findViewById(R.id.deploy_button) as Button

        val grossWinningTradeTextView :TextView = dialogView.findViewById(R.id.gross_winning_trades)
        val winningTradeTextView :TextView = dialogView.findViewById(R.id.winning_trades)
        val largestWinningTradeTextView:TextView = dialogView.findViewById(R.id.largest_winning_trade)
        val averageWinningTradeTextView:TextView = dialogView.findViewById(R.id.average_winning_trade)
        val netProfitTextView :TextView = dialogView.findViewById(R.id.net_profit)
        val winPercentageTextView:TextView = dialogView.findViewById(R.id.win_percentage)

        val grossLossingTradeTextView :TextView = dialogView.findViewById(R.id.gross_lossing_trade)
        val LossingTradeTextView :TextView = dialogView.findViewById(R.id.lossing_trade)
        val largestLossingTradeTextView:TextView = dialogView.findViewById(R.id.largest_lossing_trade)
        val averageLossingTradeTextView:TextView = dialogView.findViewById(R.id.average_lossing_trade)
        val netLossTextView :TextView = dialogView.findViewById(R.id.net_loss)
        val lossPercentageTextView:TextView = dialogView.findViewById(R.id.loss_percentage)

        grossWinningTradeTextView.text = "Gross Winning Trades: ₹1532.56"
        winningTradeTextView.text = "Winning Trade: 12"
        largestWinningTradeTextView.text = "Largest Winning Trade: ₹153.45"
        averageWinningTradeTextView.text = "Average Winning Trade: ₹134.01"
        netProfitTextView.text = "Net Profit: ₹337.70"
        winPercentageTextView.text = "57%"

        grossLossingTradeTextView.text = "Gross Losing Trades: ₹1194.86"
        LossingTradeTextView.text = "Lossing Trade: 9"
        largestLossingTradeTextView.text = "Largest Losing Trade: ₹115.73"
        averageLossingTradeTextView.text = "Average Losing Trade: ₹51.29"
        netLossTextView.text = "Net Loss: ₹0"
        lossPercentageTextView.text = "43%"

        deployButton.setOnClickListener {

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
