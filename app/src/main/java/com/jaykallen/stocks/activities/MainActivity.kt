package com.jaykallen.stocks.activities

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.ads.MobileAds
import com.jaykallen.stocks.*
import com.jaykallen.stocks.models.Portfolio
import com.jaykallen.stocks.models.Quote
import com.jaykallen.stocks.retrofit.RetrofitMgr
import com.jaykallen.stocks.room.RoomInstance
import com.jaykallen.stocks.room.RoomMgr
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_toolbar.*
import kotlinx.android.synthetic.main.recycler_stock.view.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerAdapter : RecyclerAdapter
    private var portfolios: List<Portfolio> = ArrayList()
    private var stocks : List<Quote> = ArrayList()
    private var position = 0
    private var quoteDate = Date()
    private var sdf = SimpleDateFormat("M/d h:mm:ss a")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.d(" *** Main Activity *** ")
        MobileAds.initialize(this, "ca-app-pub-2153652996366584~8809949110")
        setupToolbar()
        setupRecycler()
        connectDatabase()
    }

    override fun onResume() {
        super.onResume()
        getPortfolios()
    }

    fun connectDatabase() {
        RoomInstance.getInstance(this)
    }

    fun recalcPosition() {
        if (position >= portfolios.size) position = 0
    }

    fun getPortfolios() {
        // Gets the arraylist of portfolios from room and then calls Update UI with the first portfolio
        val roomMgr = RoomMgr.getPortfolio(this)
        roomMgr.portfolioLive.observe(this, Observer { portfolioLive ->
            Timber.d("Got the values from Live Data")
            portfolios = portfolioLive ?: ArrayList()
            if (portfolios.size > 0) {
                recalcPosition()
                updateUI(portfolios[position])
                recyclerTextView.visibility = View.INVISIBLE
            } else {
                recyclerTextView.visibility = View.VISIBLE
            }
        })
        roomMgr.execute()
    }

    private fun updateUI(portfolio: Portfolio) {
        toolbarTitleTextView.text = portfolio.name
        if (portfolio.stockList != "") getData(portfolio.stockList) else {
            recyclerTextView.text = "This Portfolio is Empty"
        }
    }

    private fun getData(portfolioStockList: String) {
        // Takes the current portfolio and retrives the data from Retrofit
        RetrofitMgr.stockList.observe(this, android.arch.lifecycle.Observer { stockList ->
            stocks = stockList ?: ArrayList()
            refreshRecycler()
            quoteDate = Date()
            statusTextView.text = "Updated ${sdf.format(quoteDate)}"
        })
        RetrofitMgr.getPortfolio(portfolioStockList)
    }


    fun onAddClick(view: View) {
        startActivity(Intent(this, PortfolioActivity::class.java).putExtra("id", "add"))
    }

    fun onEditClick(view: View) {
        if (portfolios.isNotEmpty()) {
            startActivity(Intent(this, PortfolioActivity::class.java).putExtra("id", "${portfolios[position].id}"))
        }
    }

    fun onPrevClick(view: View) {
        Timber.d ("Prev Click")
        if (portfolios.isNotEmpty()) {
            position--
            if (position < 0) {
                position = portfolios.size - 1
            }
            updateUI(portfolios[position])
        }
    }

    fun onNextClick(view: View) {
        Timber.d ("Next Click")
        if (portfolios.isNotEmpty()) {
            position++
            if (position == portfolios.size) {
                position = 0
            }
            updateUI(portfolios[position])
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        toolbarTitleTextView.text = "No Portfolios"
        toolbarDoneTextView.visibility = View.INVISIBLE
        toolbarCancelImageButton.visibility = View.INVISIBLE
        toolbarCancelTextView.visibility = View.INVISIBLE
        toolbarAddImageView.visibility = View.VISIBLE
        toolbarEditImageView.visibility = View.VISIBLE
    }

    fun onCancelClick(view: View) {
        finish()
    }

    private fun refreshRecycler() {
        recyclerAdapter.itemList = stocks
        recyclerAdapter.notifyDataSetChanged()
    }

    private fun setupRecycler() {
        recyclerAdapter = RecyclerAdapter(stocks)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = recyclerAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    private inner class RecyclerAdapter(var itemList: List<Quote>): RecyclerView.Adapter<RecyclerHolder>() {
        override fun getItemCount(): Int {
            Timber.d("")
            return itemList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_stock, parent, false)
            return RecyclerHolder(view)
        }

        override fun onBindViewHolder(recyclerholder: RecyclerHolder, position: Int) {
            recyclerholder.symbolText.text = itemList[position].symbol
            recyclerholder.nameText.text = itemList[position].companyName
            recyclerholder.priceText.text = "%.2f".format(itemList[position].latestPrice)
            val changeBoolean = itemList[position].change.compareTo(0)
            if (changeBoolean < 0) {
                recyclerholder.differenceText.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorRed))
                recyclerholder.percentText.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorRed))
            } else {
                recyclerholder.differenceText.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorGreen))
                recyclerholder.percentText.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorGreen))
            }
            val change = "%.2f".format(itemList[position].change)
            recyclerholder.differenceText.text = if (itemList[position].change>0.0f) "+$change" else change
            val percent = "%.1f".format(itemList[position].changePercent.toFloat()*100f)
            recyclerholder.percentText.text = "$percent%"
        }
    }

    private inner class RecyclerHolder(holder: View) : RecyclerView.ViewHolder(holder), View.OnClickListener {
        val symbolText: TextView = itemView.symbolText
        val nameText: TextView = itemView.nameText
        val priceText: TextView = itemView.priceText
        val differenceText: TextView = itemView.differenceText
        val percentText: TextView = itemView.percentText
        init { holder.setOnClickListener(this) }
        override fun onClick(v: View) {
            Timber.d("RecyclerView selected ${stocks[adapterPosition].symbol}")
            Quote.instance = stocks[adapterPosition]
            startActivity(Intent(this@MainActivity, StockActivity::class.java))
        }
    }
}
