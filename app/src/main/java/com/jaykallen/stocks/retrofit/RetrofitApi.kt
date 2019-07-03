package com.jaykallen.stocks.retrofit

import com.jaykallen.stocks.models.PortfolioResponse
import com.jaykallen.stocks.models.ChartData
import com.jaykallen.stocks.models.Quote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by jaykallen on 4/6/18.
 */

interface RetrofitApi {
    // This is an individual call for a single stock quote
    @GET("stock/{stock}/quote")
    abstract fun queryStock(@Path("stock") stock: String): Call<Quote>

    // This is a batch call for a whole bunch of symbols like GE, AAPL, FB, etc.
    @GET("stock/market/batch")
    abstract fun queryStockList(@Query("symbols") stocks: String, @Query("types") types: String): Call<PortfolioResponse>

    // This is a call for chart data for a single stock
    @GET("stock/{stock}/chart/1y")
    abstract fun queryChart(@Path("stock") stocks: String): Call<List<ChartData>>

}