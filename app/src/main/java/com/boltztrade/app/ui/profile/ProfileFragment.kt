package com.boltztrade.app.ui.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.boltztrade.app.BoltztradeSingleton

import com.boltztrade.app.R
import com.boltztrade.app.SharedPrefKeys
import com.boltztrade.app.apis.BoltztradeRetrofit
import com.boltztrade.app.model.BoltztradeUserDetail
import com.boltztrade.app.model.Strategies
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfileFragment : Fragment() {
    private val LOG_TAG = ProfileFragment::class.java.canonicalName


    private lateinit var tradingExpSpinner:Spinner
    private lateinit var ageExpSpinner: Spinner

    private var boltztradeUserDetail = BoltztradeUserDetail()

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        try {
            ageExpSpinner = view?.findViewById(R.id.age_spinner)!!
            tradingExpSpinner = view?.findViewById(R.id.trading_exp_spinner)!!
            ArrayAdapter.createFromResource(
                activity!!,
                R.array.age_range,
                R.layout.profile_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                ageExpSpinner.adapter = adapter
            }


            ArrayAdapter.createFromResource(
                activity!!,
                R.array.trading_exp,
                R.layout.profile_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                tradingExpSpinner.adapter = adapter
            }

            ageExpSpinner.setOnItemClickListener { adapterView, view, i, l ->  Log.d(LOG_TAG,"age Exp position  $i")}

            tradingExpSpinner.setOnItemClickListener { adapterView, view, i, l -> Log.d(LOG_TAG,"trading exp position  $i") }

        }catch (e:Exception){
            e.printStackTrace()
        }


        return view
    }

    fun callGetProfileApi(){
        val disp = BoltztradeRetrofit.getInstance().getUserDetail("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}",
            BoltztradeSingleton.mSharedPreferences.getString(SharedPrefKeys.boltztradeUser,"")!!).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
        },{
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"backtest request completed")
        })
    }

    fun updateGetProfileApi(userDetail: BoltztradeUserDetail){
        val disp = BoltztradeRetrofit.getInstance().updateUserDetail("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", userDetail).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
        },{
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"backtest request completed")
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
