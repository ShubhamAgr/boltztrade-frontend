package com.boltztrade.app

import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

class BoltztradeWebViewActivity : AppCompatActivity() {
    private val LOG_TAG = BoltztradeWebViewActivity::class.java.canonicalName
    private lateinit var webViewProgressbar:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boltztrade_web_view)
        webViewProgressbar = findViewById(R.id.webview_progressbar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }
        val myWebView: WebView = this.findViewById(R.id.boltztrade_webview)
        val isHtmlText = intent.extras?.getBoolean("isHtmlText")
        if(isHtmlText==true){
            val data= intent.extras?.getString("htmlText")
            webViewProgressbar.visibility = View.GONE
            myWebView.settings.javaScriptEnabled = true
//            myWebView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
//            myWebView.settings.loadWithOverviewMode = true
//            myWebView.settings.useWideViewPort = true
            myWebView.settings.builtInZoomControls = true
            myWebView.settings.displayZoomControls = false

            myWebView.loadDataWithBaseURL("",
                "<style>img{display: inline;height: auto;max-width: 100%;}</style>$data", "text/html", "UTF-8", "");
        }else if(isHtmlText==false){
            val url= intent.extras?.getString("url")
            myWebView.settings.javaScriptEnabled = true
            myWebView.webViewClient = object :WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    Log.d(LOG_TAG,"page loading finished...")
                    webViewProgressbar.visibility = View.GONE
                }
            }
            myWebView.loadUrl(url)
        }



    }
}
