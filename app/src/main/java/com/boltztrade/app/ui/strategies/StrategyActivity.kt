package com.boltztrade.app.ui.strategies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import com.boltztrade.app.R
import com.boltztrade.app.model.Instrument
import com.boltztrade.app.model.Strategy
import com.boltztrade.app.model.StrategyModel
import java.time.Instant
import java.util.*

class StrategyActivity : AppCompatActivity() {

    private lateinit var currentFragment: Fragment
    private lateinit var fragmentSwitcherButton:Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_strategy)
        fragmentSwitcherButton = findViewById(R.id.strategy_fragment_switch_button)
        currentFragment = CreateStrategyPart1.newInstance("","")
        switchFragment(currentFragment,"createStrategyPart1")

        fragmentSwitcherButton.setOnClickListener {
            when(currentFragment){
                is CreateStrategyPart1->{
                    (currentFragment as CreateStrategyPart1).setPage()
                    currentFragment = CreateStrategyPart2.newInstance("","")
                    switchFragment(currentFragment,"createStrategyPart2")
                }
                is CreateStrategyPart2->{
                    (currentFragment as CreateStrategyPart2).setPage()
                    currentFragment = CreateStrategyPart3.newInstance("","")
                    switchFragment(currentFragment,"createStrategyPart3")
                    fragmentSwitcherButton.text = "Save & Backtest"

                }
                is CreateStrategyPart3->{
                    (currentFragment as CreateStrategyPart3).setPage()
                    //create Strategy And submit api
                    try {
                       Log.d("create strategy","${MyStrategy.createStrategy().toString()}")
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }
            }
        }

    }

    fun switchFragment(fragment: Fragment, fragmentName:String) {
        try {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.strategy_fragment_container, fragment)
            transaction.commit()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}
