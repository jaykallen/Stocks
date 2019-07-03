package com.jaykallen.stocks.retrofit

import com.google.gson.*
import com.jaykallen.stocks.models.PortfolioResponse
import com.jaykallen.stocks.models.Quote
import java.lang.reflect.Type

/**
 * Created by Jay Kallen on 4/9/18.
 */

class PortfolioDeserializer: JsonDeserializer<PortfolioResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): PortfolioResponse {
        val portfolioResponse = PortfolioResponse()

        json?.let {
            val jsonObject = it.asJsonObject
            val symbolSet = jsonObject.entrySet()
            val quoteElements = ArrayList<JsonObject>()
            val quotes = ArrayList<Quote>()
            val gson = Gson()

            // this will give us a list of JSON elements that look like ""Quote": {}"
            symbolSet.mapTo(quoteElements) { it.value.asJsonObject }

            // this will take each quote JSON element, and only grab the JSON that resembles a Quote
            // object, and add it to our list of Quotes
            quoteElements.mapTo(quotes) { gson.fromJson(it.entrySet().first().value, Quote::class.java) }

            portfolioResponse.quotes = quotes
        }

        return portfolioResponse
    }
}