package com.boltztrade.app.ui.articles

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boltztrade.app.BoltztradeSingleton
import com.boltztrade.app.BoltztradeWebViewActivity

import com.boltztrade.app.R
import com.boltztrade.app.SharedPrefKeys
import com.boltztrade.app.apis.BoltztradeRetrofit
import com.boltztrade.app.callbacks.RecyclerviewSelectedPositionCallback
import com.boltztrade.app.model.Articles
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ArticleFragment : Fragment() {

    private val LOG_TAG = ArticleFragment::class.java.canonicalName
    companion object {
        fun newInstance() = ArticleFragment()
    }

    private lateinit var viewModel: ArticleViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter : RecyclerView.Adapter<*>
    private lateinit var viewManager : RecyclerView.LayoutManager
    private val articleList:MutableList<Articles> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.article_fragment, container, false)
        viewManager = LinearLayoutManager(activity)
        viewAdapter = ArticleListAdapter(articleList,object :RecyclerviewSelectedPositionCallback{
            override fun itemSelected(position: Int) {
                Log.d(LOG_TAG,"article clicked $position")
                if(articleList[position].htmlContent != ""){
                   activity?.startActivity(Intent(activity!!,BoltztradeWebViewActivity::class.java).apply {
                       putExtra("isHtmlText",true)
                       putExtra("htmlText",articleList[position].htmlContent)
                   })
                }
            }

        })
        recyclerView = (view.findViewById(R.id.article_recycler_view) as RecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

        val disp = BoltztradeRetrofit.getInstance().getArticles("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}").
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
            articleList.clear()
            articleList.addAll(it)
            viewAdapter.notifyDataSetChanged()
        },{
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"News Api Call Completed..")
        })
        return  view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
