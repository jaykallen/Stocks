package com.jaykallen.stocks.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jaykallen on 3/29/18.
 */

class Quote {

    companion object {
        var instance = Quote()
    }

    @SerializedName("symbol")
    @Expose
    val symbol: String? = null
    @SerializedName("companyName")
    @Expose
    val companyName: String? = null
    @SerializedName("primaryExchange")
    @Expose
    val primaryExchange: String? = null
    @SerializedName("sector")
    @Expose
    val sector: String? = null
    @SerializedName("calculationPrice")
    @Expose
    val calculationPrice: String? = null
    @SerializedName("open")
    @Expose
    val open: Double = 0.toDouble()
    @SerializedName("openTime")
    @Expose
    val openTime: Long = 0
    @SerializedName("close")
    @Expose
    val close: Double = 0.toDouble()
    @SerializedName("closeTime")
    @Expose
    val closeTime: Long = 0
    @SerializedName("high")
    @Expose
    val high: Double = 0.toDouble()
    @SerializedName("low")
    @Expose
    val low: Double = 0.toDouble()
    @SerializedName("latestPrice")
    @Expose
    val latestPrice: Double = 0.toDouble()
    @SerializedName("latestSource")
    @Expose
    val latestSource: String? = null
    @SerializedName("latestTime")
    @Expose
    val latestTime: String? = null
    @SerializedName("latestUpdate")
    @Expose
    val latestUpdate: Long = 0
    @SerializedName("latestVolume")
    @Expose
    val latestVolume: Long = 0
    @SerializedName("iexRealtimePrice")
    @Expose
    val iexRealtimePrice: Double = 0.toDouble()
    @SerializedName("iexRealtimeSize")
    @Expose
    val iexRealtimeSize: Long = 0
    @SerializedName("iexLastUpdated")
    @Expose
    val iexLastUpdated: Long = 0
    @SerializedName("delayedPrice")
    @Expose
    val delayedPrice: Double = 0.toDouble()
    @SerializedName("delayedPriceTime")
    @Expose
    val delayedPriceTime: Long = 0
    @SerializedName("previousClose")
    @Expose
    val previousClose: Double = 0.toDouble()
    @SerializedName("change")
    @Expose
    val change: Double = 0.toDouble()
    @SerializedName("changePercent")
    @Expose
    val changePercent: Double = 0.toDouble()
    @SerializedName("iexMarketPercent")
    @Expose
    val iexMarketPercent: Double = 0.toDouble()
    @SerializedName("iexVolume")
    @Expose
    val iexVolume: Long = 0
    @SerializedName("avgTotalVolume")
    @Expose
    val avgTotalVolume: Long = 0
    @SerializedName("iexBidPrice")
    @Expose
    val iexBidPrice: Double = 0.0
    @SerializedName("iexBidSize")
    @Expose
    val iexBidSize: Long = 0
    @SerializedName("iexAskPrice")
    @Expose
    val iexAskPrice: Double = 0.0
    @SerializedName("iexAskSize")
    @Expose
    val iexAskSize: Long = 0
    @SerializedName("marketCap")
    @Expose
    val marketCap: Long = 0
    @SerializedName("peRatio")
    @Expose
    val peRatio: Double = 0.toDouble()
    @SerializedName("week52High")
    @Expose
    val week52High: Double = 0.toDouble()
    @SerializedName("week52Low")
    @Expose
    val week52Low: Double = 0.toDouble()
    @SerializedName("ytdChange")
    @Expose
    val ytdChange: Double = 0.toDouble()

}