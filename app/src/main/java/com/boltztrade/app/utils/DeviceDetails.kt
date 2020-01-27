package com.boltztrade.app.utils

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import java.util.*

object DeviceDetails {
    fun getScreenResolution(context: Context): String {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        Log.d("resolution","{$width,$height}")
        return "{$width,$height}"
    }



    fun getAppVersion(context: Context):String{

        var manager = context.packageManager
        var info = manager.getPackageInfo(context.packageName, 0)
        Log.d("app version...",info.versionName)
        return info.versionName
    }


    fun getManufacturer():String?{
        Log.d("manufacturer", Build.MANUFACTURER)
        return Build.MANUFACTURER
    }


    fun getModel():String?{return Build.MODEL}//}

    fun getOs():String{return "android"}

    /**os version**/
    fun getOsVersion():String{
        val osVersion  = Build.VERSION.SDK_INT.toString()
        Log.d("osVersion",osVersion)
        return osVersion
    }

    fun getCountry(context: Context):String{
//        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        val countryCode = tm.simCountryIso
//        return context.resources.configuration.locale.country
        return "IN"
    }

    fun getTimeZone(context: Context):String{
        Log.d("timezone", TimeZone.getDefault().displayName)
        return TimeZone.getDefault().displayName
    }

    fun getAppRegistrationDate(){

    }

    fun getType(context: Context):String{
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels

        return if (width > 1200 || height > 1920){
            Log.d("device Type","tablet")
            DeviceType.TABLET
        }else{
            Log.d("device Type","phone")
            DeviceType.PHONE
        }
    }



    object DeviceType {
        val PHONE = "phone"
        val TABLET = "tablet"
    }
}