package com.jaykallen.stocks.activities

import android.app.Activity
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.jaykallen.stocks.models.Portfolio
import com.jaykallen.stocks.R
import com.jaykallen.stocks.room.RoomMgr
import kotlinx.android.synthetic.main.activity_portfolio.*
import timber.log.Timber

class PortfolioActivity : AppCompatActivity() {
    private var portfolio = Portfolio()
    private var id = "add"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_portfolio)
        getData()
    }

    fun getData() {
        id = intent.getStringExtra("id")?:"add"
        Timber.d("Opening id=$id")
        if (id != "add") {
            val roomMgr = RoomMgr.getPortfolioById(this, id)
            roomMgr.portfolioLive.observe(this, Observer { portfolioLive ->
                Timber.d("Got the value from Live Data")
                portfolio = portfolioLive ?: Portfolio()
                setupExistingDoc()
            })
            roomMgr.execute()
        } else {
            deleteButton.visibility = View.INVISIBLE
        }
    }

    fun onDeleteClick(view: View) {
        val roomMgr = RoomMgr.delete(this, portfolio)
        roomMgr.execute()
        setResult(Activity.RESULT_OK)
        finish()
    }

    fun setupExistingDoc() {
        titleTextView.setText(portfolio.name)
        stockTextView.setText(portfolio.stockList)
    }

    fun onCancelClick(view: View) {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    fun updateModel() {
        portfolio.name = titleTextView.text.toString()
        portfolio.stockList = stockTextView.text.toString()
    }

    fun onDoneClick(view: View) {
        updateModel()
        val roomMgr = if (id == "add") {
            RoomMgr.create(this, portfolio)
        } else {
            RoomMgr.update(portfolio)
        }
        roomMgr.execute()
        setResult(Activity.RESULT_OK)
        finish()
    }
}
