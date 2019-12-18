package com.boltztrade.app

import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class BoltztradeWebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boltztrade_web_view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }
        val myWebView: WebView = this.findViewById(R.id.boltztrade_webview)
        val isHtmlText = intent.extras?.getBoolean("isHtmlText")
        if(isHtmlText==true){
            val data= intent.extras?.getString("htmlText")
            myWebView.settings.javaScriptEnabled = true
            myWebView.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
        }else if(isHtmlText==false){
            val url= intent.extras?.getString("url")
            myWebView.settings.javaScriptEnabled = true
            myWebView.loadUrl(url)
        }



    }
}
