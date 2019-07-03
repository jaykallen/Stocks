package com.jaykallen.stocks.room

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.os.AsyncTask
import com.jaykallen.stocks.models.Portfolio
import timber.log.Timber

class RoomMgr {


    class getPortfolioById(val context: Context, val id: String): AsyncTask<String, String, String>() {
        var portfolioLive: MutableLiveData<Portfolio> = MutableLiveData()
        var portfolio = Portfolio()

        override fun doInBackground(vararg p0: String?): String {
            val roomMgr = RoomInstance.instance
            portfolio = roomMgr?.daoAccess()?.getById(id)?: Portfolio()
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Timber.d("portfolios retrieved = ${portfolio.name}")
            portfolioLive.value = portfolio
        }
    }

    class getPortfolio(var context: Context): AsyncTask<String, String, String>() {
        var portfolioLive: MutableLiveData<List<Portfolio>> = MutableLiveData()
        var portfolioList: List<Portfolio> = ArrayList()

        override fun doInBackground(vararg p0: String?): String {

            val roomMgr = RoomInstance.instance
            portfolioList = roomMgr?.daoAccess()?.getAll()?:ArrayList()
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Timber.d("portfolios retrieved = ${portfolioList.size}")
            portfolioLive.value = portfolioList
        }
    }

    class create(var context: Context, var portfolio: Portfolio): AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val databaseMgr = RoomInstance.instance
            databaseMgr?.daoAccess()?.create(portfolio)
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Timber.d("portfolio ${portfolio.name} added")
        }
    }

    class update (var portfolio: Portfolio): AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val databaseMgr = RoomInstance.instance
            databaseMgr?.daoAccess()?.update(portfolio)
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Timber.d("portfolio ${portfolio.name} updated")
        }
    }

    class delete(var context: Context, var portfolio: Portfolio): AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val databaseMgr = RoomInstance.instance
            databaseMgr?.daoAccess()?.delete(portfolio)
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Timber.d("portfolio ${portfolio.name} deleted")
        }
    }

}