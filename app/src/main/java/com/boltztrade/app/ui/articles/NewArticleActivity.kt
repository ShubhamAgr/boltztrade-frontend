package com.boltztrade.app.ui.articles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.boltztrade.app.R
import org.wordpress.aztec.Aztec
import org.wordpress.aztec.AztecText
import org.wordpress.aztec.ITextFormat
import org.wordpress.aztec.source.SourceViewEditText
import org.wordpress.aztec.toolbar.AztecToolbar
import org.wordpress.aztec.toolbar.IAztecToolbarClickListener
import java.lang.Exception

class NewArticleActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_article)
        val visualEditor = findViewById<AztecText>(R.id.visual)
        val sourceEditor = findViewById<SourceViewEditText>(R.id.source)
        val toolbar = findViewById<AztecToolbar>(R.id.formatting_toolbar)

        val publishArticle = findViewById<Button>(R.id.publish_article)
        publishArticle.setOnClickListener {
            Log.d("publishArticle","Button clicked")
            try {

                val purehtml = sourceEditor?.getPureHtml(false)
                val ab = sourceEditor?.displayStyledHtml(purehtml!!)
                Log.d("HTML","pure html ${purehtml?.length} , ${visualEditor?.toHtml(true)} ${visualEditor?.text.toString()}")
            }catch (e:Exception){
                Log.d("Exception",e.localizedMessage)
                e.printStackTrace()
            }
        }
//        sourceEditor?.getPureHtml(true)
        Aztec.with(visualEditor, sourceEditor, toolbar,object :IAztecToolbarClickListener{
            override fun onToolbarCollapseButtonClicked() {

            }

            override fun onToolbarExpandButtonClicked() {

            }

            override fun onToolbarFormatButtonClicked(
                format: ITextFormat,
                isKeyboardShortcut: Boolean
            ) {

            }

            override fun onToolbarHeadingButtonClicked() {

            }

            override fun onToolbarHtmlButtonClicked() {

            }

            override fun onToolbarListButtonClicked() {

            }

            override fun onToolbarMediaButtonClicked(): Boolean {
                return true
            }

        })

    }
}
