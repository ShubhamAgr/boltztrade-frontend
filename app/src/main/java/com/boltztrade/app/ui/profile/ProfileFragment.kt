package com.boltztrade.app.ui.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import com.boltztrade.app.BoltztradeSingleton
import com.boltztrade.app.R
import com.boltztrade.app.SharedPrefKeys
import com.boltztrade.app.apis.ApiService
import com.boltztrade.app.apis.BoltztradeRetrofit
import com.boltztrade.app.model.BoltztradeUserDetail
import com.boltztrade.app.model.UpdateUserDetailsData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfileFragment : Fragment() {
    private val LOG_TAG = ProfileFragment::class.java.canonicalName


    private lateinit var tradingExpSpinner:Spinner
    private lateinit var ageExpSpinner: Spinner
    private lateinit var fullnameEditText: EditText
    private lateinit var studentRadioButton: RadioButton
    private lateinit var professionalRadioButton: RadioButton
    private lateinit var lookingForJobCheckBox: CheckBox
    private lateinit var lookingForClientCheckBox: CheckBox
    private lateinit var isRegisteredAdvisorCheckBox: CheckBox
    private lateinit var registedAdvisorEditText: EditText
    private lateinit var isRegisteredTraderCheckBox: CheckBox
    private lateinit var registeredTraderEditText: EditText
    private lateinit var isRegisteredAnalystCheckBox:CheckBox
    private lateinit var registeredAnalystEditText: EditText
    private lateinit var updateAndEditButton:Button

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private var currentUiStatus = false

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        try {
            ageExpSpinner = view?.findViewById(R.id.age_spinner)!!
            tradingExpSpinner = view?.findViewById(R.id.trading_exp_spinner)!!
            fullnameEditText = view.findViewById(R.id.user_full_name)
            studentRadioButton = view.findViewById(R.id.student_radio_button)
            professionalRadioButton = view.findViewById(R.id.professional_radio_button)
            lookingForJobCheckBox = view.findViewById(R.id.looking_for_job_checkbox)
            lookingForClientCheckBox = view.findViewById(R.id.looking_for_clients_checkbox)
            isRegisteredAdvisorCheckBox = view.findViewById(R.id.registered_advisor_checkbox)
            registedAdvisorEditText = view.findViewById(R.id.registered_advisor_text)
            isRegisteredAnalystCheckBox = view.findViewById(R.id.registered_analyst_checkbox)
            registeredAnalystEditText = view.findViewById(R.id.registered_analyst_text)
            isRegisteredTraderCheckBox = view.findViewById(R.id.registered_trader_checkbox)
            registeredTraderEditText = view.findViewById(R.id.registered_trader_text)
            updateAndEditButton = view.findViewById(R.id.update_profile)

            disableAndEnableComponents(currentUiStatus)

            ArrayAdapter.createFromResource(
                activity!!,
                R.array.age_range,
                R.layout.profile_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                ageExpSpinner.adapter = adapter
            }


            updateAndEditButton.setOnClickListener {
                if(!currentUiStatus){
                    currentUiStatus = ! currentUiStatus
                    updateAndEditButton.text = "Save"
                    disableAndEnableComponents(currentUiStatus)
                }else{
                    setuserDetail()
                    currentUiStatus = ! currentUiStatus
                    updateAndEditButton.text = "Edit"
                    disableAndEnableComponents(currentUiStatus)
                }
            }

            ArrayAdapter.createFromResource(
                activity!!,
                R.array.trading_exp,
                R.layout.profile_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                tradingExpSpinner.adapter = adapter
            }

//            ageExpSpinner.setOnItemClickListener { adapterView, view, i, l ->  Log.d(LOG_TAG,"age Exp position  $i")}
//
//            tradingExpSpinner.setOnItemClickListener { adapterView, view, i, l -> Log.d(LOG_TAG,"trading exp position  $i") }


            callGetProfileApi()
        }catch (e:Exception){
            e.printStackTrace()
        }


        return view
    }

    fun disableAndEnableComponents(value:Boolean){
        ageExpSpinner.isEnabled = value
        tradingExpSpinner.isEnabled = value
        fullnameEditText.isEnabled = value
        studentRadioButton.isEnabled = value
        professionalRadioButton.isEnabled = value
        lookingForJobCheckBox.isEnabled = value
        lookingForClientCheckBox.isEnabled = value
        isRegisteredAdvisorCheckBox.isEnabled = value
        registedAdvisorEditText.isEnabled = value
        isRegisteredAnalystCheckBox.isEnabled = value
        registeredAnalystEditText.isEnabled = value
        isRegisteredTraderCheckBox.isEnabled = value
        registeredTraderEditText.isEnabled = value
    }

    fun  setuserDetail(){
        val workingStatus = if(professionalRadioButton.isChecked)"professional" else "student"
        val boltztradeUserDetail = BoltztradeUserDetail(fullnameEditText.text.toString(),ageExpSpinner.selectedItem.toString(),0,tradingExpSpinner.selectedItem.toString(),
            workingStatus,lookingForJobCheckBox.isChecked,lookingForClientCheckBox.isChecked,isRegisteredTraderCheckBox.isChecked,isRegisteredAdvisorCheckBox.isChecked,
            isRegisteredAnalystCheckBox.isChecked,registeredTraderEditText.text.toString(),registedAdvisorEditText.text.toString(),registeredAnalystEditText.text.toString())

        updateGetProfileApi(boltztradeUserDetail)


    }

    fun callGetProfileApi(){
        val disp = BoltztradeRetrofit.getInstance().getUserDetail("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", ApiService.MUsername(BoltztradeSingleton.mSharedPreferences.getString(SharedPrefKeys.boltztradeUser,"")!!)).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
            setUI(boltztradeUserDetail = it)
        },{
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"get user detail request completed")
        })
    }

    fun setUI(boltztradeUserDetail: BoltztradeUserDetail){
        fullnameEditText.setText(boltztradeUserDetail.fullName)
        when(boltztradeUserDetail.DateOfBirth){

            "<20"->{ageExpSpinner.setSelection(0)}
            "20 - 25"->{ageExpSpinner.setSelection(1)}
            "25 - 30"->{ageExpSpinner.setSelection(2)}
            "30 - 40"->{ageExpSpinner.setSelection(3)}
            "40 - 50"->{ageExpSpinner.setSelection(4)}
            ">50"->{ageExpSpinner.setSelection(5)}
            else->{ageExpSpinner.setSelection(0)}
        }
        when(boltztradeUserDetail.tradingExperience){
            "<1 yr"->{tradingExpSpinner.setSelection(0)}
            "1 -2 yr"->{tradingExpSpinner.setSelection(1)}
            "2 - 5 yr"->{tradingExpSpinner.setSelection(2)}
            "5 - 10 yr"->{tradingExpSpinner.setSelection(3)}
            "10 - 20 yr"->{tradingExpSpinner.setSelection(4)}
            ">20 yr" ->{tradingExpSpinner.setSelection(5)}
            else->{tradingExpSpinner.setSelection(0)}
        }

        when(boltztradeUserDetail.workingStatus){
            "professional"->{professionalRadioButton.isChecked = true}
            "student"->{studentRadioButton.isChecked = true}
            else->{}
        }
        if(boltztradeUserDetail.lookingForTradingJob == true){ lookingForJobCheckBox.isChecked = true}

        if(boltztradeUserDetail.lookingForClient == true) {lookingForClientCheckBox.isChecked = true}

        if(boltztradeUserDetail.IsSebiRegisteredAdvisor == true) {
            isRegisteredAdvisorCheckBox.isChecked = true
            registedAdvisorEditText.setText(boltztradeUserDetail.sebiRegisteredAdvisorNumber)
        }

        if(boltztradeUserDetail.IsSebiRegisteredTrader == true) {
            isRegisteredTraderCheckBox.isChecked = true
            registeredTraderEditText.setText(boltztradeUserDetail.sebiTraderRegistrationNumber)
        }

        if(boltztradeUserDetail.IsSebiRegisteredAnalyst == true){
            isRegisteredAnalystCheckBox.isChecked = true
            registeredAnalystEditText.setText(boltztradeUserDetail.sebiRegisteredAnalystNumber)
        }

    }

    fun updateGetProfileApi(userDetail: BoltztradeUserDetail){
        val disp = BoltztradeRetrofit.getInstance().updateUserDetail("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", UpdateUserDetailsData( BoltztradeSingleton.mSharedPreferences.getString(SharedPrefKeys.boltztradeUser,"")!!,userDetail)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
        },{
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"update user detail request completed")
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

    }

}
