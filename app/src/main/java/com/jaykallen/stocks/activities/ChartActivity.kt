package com.jaykallen.stocks.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.github.mikephil.charting.charts.LineChart
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.jaykallen.stocks.ChartMgr
import com.jaykallen.stocks.R
import com.jaykallen.stocks.models.ChartData
import com.jaykallen.stocks.models.Quote
import com.jaykallen.stocks.retrofit.RetrofitMgr
import kotlinx.android.synthetic.main.activity_chart.*
import kotlinx.android.synthetic.main.content_toolbar.*
import timber.log.Timber
import java.util.ArrayList

class ChartActivity : AppCompatActivity() {
    private var stock = Quote.instance
    private lateinit var mChart : LineChart
    private var chartDataList : List<ChartData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        Timber.d (" **** Chart Activity **** ")
        setupToolbar()
        showData()
        getChart(stock.symbol?:"")
        setupAd()
    }

    private fun showData() {
        latestPriceTextView.text = "%.2f".format(stock.latestPrice)
        val changeBoolean = stock.change.compareTo(0)
        if (changeBoolean < 0) {
            changeTextView.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
            changePercentTextView.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
        } else {
            changeTextView.setTextColor(ContextCompat.getColor(this, R.color.colorGreen))
            changePercentTextView.setTextColor(ContextCompat.getColor(this, R.color.colorGreen))
        }
        changeTextView.text = "%.2f".format(stock.change)
        changePercentTextView.text = "%.2f".format(stock.changePercent) + "%"
        nameTextView.text = stock.companyName
        exchangeTextView.text = stock.primaryExchange
        sectorTextView.text = stock.sector
        yearHighTextView.text = "%.2f".format(stock.week52High)
        yearLowTextView.text = "%.2f".format(stock.week52Low)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        toolbarTitleTextView.text = stock.symbol
        toolbarCancelImageButton.visibility = View.VISIBLE
        toolbarCancelTextView.visibility = View.VISIBLE
        toolbarAddImageView.visibility = View.INVISIBLE
        toolbarDoneTextView.visibility = View.INVISIBLE
    }

    private fun getChart(stock: String) {
        if (stock.isNotBlank()) {
            RetrofitMgr.chartList.observe(this, android.arch.lifecycle.Observer { chartList ->
                chartDataList = chartList ?: ArrayList()
                populateChart()
            })
            RetrofitMgr.getChart(stock)
        }
    }

    private fun populateChart() {
        mChart = chartLayout
        val chartMgr = ChartMgr()
        mChart.data = chartMgr.setupData(chartDataList)
        mChart = chartMgr.setupChart(this.applicationContext, mChart)
        mChart.invalidate()
    }


    fun onCancelClick(view: View) {
        finish()
    }

    private fun setupAd() {
        // App ID:  ca-app-pub-2153652996366584~8809949110
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adListener()
    }

    private fun adListener() {
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() { Timber.d("onAdLoaded") }
            override fun onAdFailedToLoad(errorCode: Int) { Timber.d("onAdFailedToLoad") }
            override fun onAdOpened() { Timber.d("onAdOpened") }
            override fun onAdLeftApplication() { Timber.d("onAdLeftApplication") }
            override fun onAdClosed() { Timber.d("onAdClosed") }
        }
    }
}
