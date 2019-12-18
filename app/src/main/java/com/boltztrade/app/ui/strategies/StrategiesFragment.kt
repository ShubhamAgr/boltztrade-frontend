package com.boltztrade.app.ui.strategies

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StrategiesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
