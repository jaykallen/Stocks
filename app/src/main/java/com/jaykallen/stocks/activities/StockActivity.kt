package com.jaykallen.stocks.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.mikephil.charting.charts.LineChart
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.jaykallen.stocks.ChartMgr
import com.jaykallen.stocks.R
import com.jaykallen.stocks.models.ChartData
import com.jaykallen.stocks.models.Quote
import com.jaykallen.stocks.retrofit.RetrofitMgr
import kotlinx.android.synthetic.main.activity_stock.*
import kotlinx.android.synthetic.main.content_toolbar.*
import timber.log.Timber
import java.util.*

class StockActivity : AppCompatActivity() {
    private var stock = Quote.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)
        Timber.d (" **** Stock Activity **** ")
        Timber.d ("Stock = ${stock.companyName} @ price = ${stock.latestPrice}")
        setupToolbar()
        showData()
        setupAd()
    }

    fun onChartClick(view: View) {
        startActivity(Intent(this, ChartActivity::class.java))
        finish()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        toolbarTitleTextView.text = stock.symbol
        toolbarCancelImageButton.visibility = View.VISIBLE
        toolbarCancelTextView.visibility = View.VISIBLE
        toolbarAddImageView.visibility = View.INVISIBLE
        toolbarDoneTextView.visibility = View.INVISIBLE
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
        openTextView.text = "%.2f".format(stock.open)
        closeTextView.text = "%.2f".format(stock.close)
        highTextView.text = "%.2f".format(stock.high)
        lowTextView.text = "%.2f".format(stock.low)
        yearHighTextView.text = "%.2f".format(stock.week52High)
        yearLowTextView.text = "%.2f".format(stock.week52Low)
        peTextView.text = "%.2f".format(stock.peRatio)
        marketCapTextView.text = getOrdinal(stock.marketCap)
        latestVolumeTextView.text =  getOrdinal(stock.latestVolume)
        avgTotalVolumeTextView.text =  getOrdinal(stock.avgTotalVolume)
    }

    private fun getOrdinal(amount: Long): String {
        if (amount > 1000000000) {
            return "%.2f B".format(amount.toDouble() / 1000000000.00)
        }
        if (amount > 1000000) {
            return "%.2f M".format(amount.toDouble() / 1000000.00)
        }
        return amount.toString()
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
