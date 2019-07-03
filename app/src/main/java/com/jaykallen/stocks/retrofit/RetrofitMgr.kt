package com.jaykallen.stocks.retrofit

import android.arch.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.jaykallen.stocks.models.PortfolioResponse
import com.jaykallen.stocks.models.ChartData
import com.jaykallen.stocks.models.Quote
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

/**
 * Created by Jay Kallen on 4/9/18.
 */

class RetrofitMgr {
    companion object {

        val BASE_URL = "https://api.iextrading.com/1.0/?"
        var stockList: MutableLiveData<List<Quote>> = MutableLiveData()
        var chartList: MutableLiveData<List<ChartData>> = MutableLiveData()
        var stocks : List<Quote> = ArrayList()!!
        var stock : Quote = Quote()
        var chart : List<ChartData> = ArrayList()

        fun getPortfolio(query: String) {
            val service = initiateRetrofit()
            val call = service.queryStockList(query, "Quote")
            Timber.d("Url: " + call.request().url())
            call.enqueue(object : retrofit2.Callback<PortfolioResponse> {
                override fun onResponse(call: Call<PortfolioResponse>, response: retrofit2.Response<PortfolioResponse>) {
                    stocks = response.body()!!.quotes
                    Timber.d("Successful Market Batch Query. Number= ${stocks.size}")
                    stockList.value = stocks
                }
                override fun onFailure(call: Call<PortfolioResponse>, t: Throwable) {
                    Timber.d("Failed Call: " + t)
                }
            })
        }

        fun getStock(query: String) {
            val service = initiateRetrofit()
            val call = service.queryStock(query)
            Timber.d("Url: " + call.request().url())
            call.enqueue(object : retrofit2.Callback<Quote> {
                override fun onResponse(call: Call<Quote>, response: retrofit2.Response<Quote>) {
                    Timber.d("Successful Stock Query. Response.body=${response.body()!!.symbol}")
                    stock = response.body()!!
                    Timber.d("Result: ${stock.symbol} latestPrice=${stock.latestPrice} close=${stock.close}")
                }
                override fun onFailure(call: Call<Quote>, t: Throwable) {
                    Timber.d("Failed Call: $t")
                }
            })
        }

        fun getChart(query: String) {
            val service = initiateRetrofit()
            val call = service.queryChart(query)
            Timber.d("Url: " + call.request().url())
            call.enqueue(object : retrofit2.Callback<List<ChartData>> {
                override fun onResponse(call: Call<List<ChartData>>, response: retrofit2.Response<List<ChartData>>) {
//                    Timber.d("Successful Stock Query. Response.body=${response.body()}")
                    chart = response.body()!!
                    if (chart.isNotEmpty()) {
                        Timber.d("Result: ${chart[0].date} price=${chart[0].close}")
                        chartList.value = chart
                    }
                }
                override fun onFailure(call: Call<List<ChartData>>, t: Throwable) {
                    Timber.d("Failed Call: $t")
                }
            })
        }

        private fun initiateRetrofit(): RetrofitApi {
            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(buildGsonConverter()).build()
            return retrofit.create(RetrofitApi::class.java)
        }

        private fun buildGsonConverter(): GsonConverterFactory {
            val gsonBuilder = GsonBuilder()
            gsonBuilder.setLenient()

            // Adding custom deserializers
            gsonBuilder.registerTypeAdapter(PortfolioResponse::class.java, PortfolioDeserializer())
            val myGson = gsonBuilder.create()

            return GsonConverterFactory.create(myGson)
        }

    }
}