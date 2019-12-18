package com.boltztrade.app.ui.strategies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import com.boltztrade.app.BoltztradeSingleton
import com.boltztrade.app.R
import com.boltztrade.app.SharedPrefKeys
import com.boltztrade.app.apis.BoltztradeRetrofit
import com.boltztrade.app.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.time.Instant
import java.util.*

class StrategyActivity : AppCompatActivity() {

    private lateinit var currentFragment: Fragment
    private lateinit var fragmentSwitcherButton:Button
    private val LOG_TAG = StrategyActivity::class.java.canonicalName



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
                        try {

                            val disp = BoltztradeRetrofit.getInstance().createStrategies("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
                                SharedPrefKeys.boltztradeToken,"")!!}",MyStrategy.createStrategy()).
                                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                                val disp2 = BoltztradeRetrofit.getInstance().addStrategyAuthor("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
                                    SharedPrefKeys.boltztradeToken,"")!!}", Strategies.AddStrategyAuthor(it._id?:"",BoltztradeSingleton.mSharedPreferences.getString(
                                    SharedPrefKeys.boltztradeUser,"")!!)).
                                    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                                    Log.d(LOG_TAG,it.toString())
                                    finish()
                                },{
                                    it.printStackTrace()
                                },{
                                    Log.i(LOG_TAG,"Get All Strategies Call Completed..")
                                })

                            },{
                                it.printStackTrace()
                            },{
                                Log.i(LOG_TAG,"Get All Strategies Call Completed..")
                            })


                        }catch (e:Exception){
                            e.printStackTrace()
                        }
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
