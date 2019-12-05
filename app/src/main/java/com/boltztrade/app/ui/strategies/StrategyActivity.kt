package com.boltztrade.app.ui.strategies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.boltztrade.app.R

class StrategyActivity : AppCompatActivity() {

    lateinit var currentFragment: Fragment
    lateinit var fragmentSwitcherButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_strategy)
        fragmentSwitcherButton = findViewById(R.id.strategy_fragment_switch_button)
        currentFragment = CreateStrategyPart1.newInstance("","")
        switchFragment(currentFragment,"createStrategyPart1")

        fragmentSwitcherButton.setOnClickListener {
            when(currentFragment){
                is CreateStrategyPart1->{
                    currentFragment = CreateStrategyPart2.newInstance("","")
                    switchFragment(currentFragment,"createStrategyPart2")
                }
                is CreateStrategyPart2->{
                    currentFragment = CreateStrategyPart3.newInstance("","")
                    switchFragment(currentFragment,"createStrategyPart3")
                    fragmentSwitcherButton.text = "Submit"

                }
                is CreateStrategyPart3->{
                    //call submit api...
                }
            }
        }

    }

    fun switchFragment(fragment: Fragment, fragmentName:String) {
        try {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.strategy_fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}
