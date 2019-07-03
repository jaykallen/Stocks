package com.jaykallen.stocks

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.android.synthetic.main.content_marker.view.*

// Created by Jay Kallen 3/22/2019
// https://github.com/PhilJay/MPAndroidChart/wiki/MarkerView

class CustomMarkerView : MarkerView {
    var mLabels: ArrayList<String>

    constructor(context: Context, layoutResource: Int, xLabels: ArrayList<String>) : super(context, layoutResource) {
        mLabels = xLabels
    }

    override fun refreshContent(entry: Entry, highlight: Highlight) {
        val position = highlight.x.toInt()
        tvContent.text = if (position < mLabels.size) {
            "${mLabels[position]} ${entry.y}"
        } else {
            "${entry.y}"
        }
        super.refreshContent(entry, highlight)
    }

    fun getXOffset(xpos: Float): Int {
        // this will center the marker-view horizontally
        return -(width / 2)
    }

    fun getYOffset(ypos: Float): Int {
        // this will cause the marker-view to be above the selected value
        return -height
    }

}