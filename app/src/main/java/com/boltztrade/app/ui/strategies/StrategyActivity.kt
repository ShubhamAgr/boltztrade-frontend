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
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.text.DateFormat
import android.app.Activity
import android.content.Intent
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe


class StrategyActivity : AppCompatActivity() {

    private lateinit var currentFragment: Fragment
    private lateinit var fragmentSwitcherButton:Button
    private val LOG_TAG = StrategyActivity::class.java.canonicalName
    val gson = Gson()
    private var isUpdateRequest = false
    private var strategyID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_strategy)
        fragmentSwitcherButton = findViewById(R.id.strategy_fragment_switch_button)
        val strategyString = intent.extras?.getString("strategy")
        if(strategyString != null){
            val strategy = gson.fromJson(strategyString,StrategyModel::class.java)
                Log.d("strategy available","update this strategy")
            isUpdateRequest = true
            strategyID = strategy._id?:""
            MyStrategy.setMPeriod(strategy.period?:"day")
            MyStrategy.setMAlgoName(strategy.algoName)
            MyStrategy.setMCandleInterval(strategy.candleInterval?:"")
            MyStrategy.setMEntryCondition(strategy.entry)
            MyStrategy.setMExitCondition(strategy.exit)
            MyStrategy.setMPosition(strategy.position?:"buy")
            MyStrategy.setMTargetLossPercent(strategy.stopLossPercent?:0.0f)
            MyStrategy.setMTargetProfitPercent(strategy.targetProfitPercent?:0.0f)
            MyStrategy.setMQuantity(strategy.quantity?:0.0)
            getInstrument(strategy.instrument?:"")
        }else{
            Log.d("strategy not available","continue with strategy")
            currentFragment = CreateStrategyPart1.newInstance(isUpdateRequest,"")
            switchFragment(currentFragment,"createStrategyPart1")

        }
        fragmentSwitcherButton.setOnClickListener {
            when(currentFragment){
                is CreateStrategyPart1->{
                    (currentFragment as CreateStrategyPart1).setPage()
                    currentFragment = CreateStrategyPart2.newInstance(isUpdateRequest,"")
                    switchFragment(currentFragment,"createStrategyPart2")
                }
                is CreateStrategyPart2->{
                    try {
                        (currentFragment as CreateStrategyPart2).setPage()
                        currentFragment = CreateStrategyPart2_2.newInstance(isUpdateRequest,"")
                        switchFragment(currentFragment,"createStrategyPart2_2")
                    }catch (e:Exception){
                     e.printStackTrace()
                    }


                }
                is CreateStrategyPart2_2->{
                    try {
                        (currentFragment as CreateStrategyPart2_2).setPage()
                        currentFragment = CreateStrategyPart3.newInstance(isUpdateRequest,"")
                        switchFragment(currentFragment,"createStrategyPart3")
                        if(isUpdateRequest){
                            fragmentSwitcherButton.text = "Update\tStrategy"
                        }else fragmentSwitcherButton.text = "Save\tStrategy"

                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                is CreateStrategyPart3->{
                    (currentFragment as CreateStrategyPart3).setPage()
                    //create Strategy And submit api
                    try {
                        try {

                            val df = DateFormat.getTimeInstance()
                            df.setTimeZone(TimeZone.getTimeZone("gmt"))
                            val gmtTime = df.format(Date())
                            if(isUpdateRequest){
                                updateStrategy()
                            }else{
                                saveStrategy()
                            }

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

    override fun onBackPressed() {
        val data = Intent()
        // add data to Intent
        Log.d(LOG_TAG,"on back pressed")
        setResult(Activity.RESULT_OK, data)
        finish()
    }


    fun saveStrategy(){
        val disp = BoltztradeRetrofit.getInstance().createStrategies("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}",MyStrategy.createStrategy()).
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({

            Log.d(LOG_TAG,it.toString())

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
    }
    fun updateStrategy(){
        val strategy = MyStrategy.createStrategy()
        strategy._id = strategyID
        val disp = BoltztradeRetrofit.getInstance().updateStrategies("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}",strategy).
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
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
    }
    fun getInstrument(s:String){
        val disposible = Observable.create(ObservableOnSubscribe<List<Instrument>> {
            val list = BoltztradeSingleton.mDatabase.instrumentDao().searchInstrumentFromToken(s)
            it.onNext(list)
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            if(it.isNotEmpty()){
                MyStrategy.setMSelectedInstrument(it[0])
            }
            currentFragment = CreateStrategyPart1.newInstance(isUpdateRequest,"")
            switchFragment(currentFragment,"createStrategyPart1")
        },{
            Log.d(LOG_TAG,"something went wrong while adding notification...")
            it.printStackTrace()
        },{ Log.d(LOG_TAG,"onComplete")})
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
