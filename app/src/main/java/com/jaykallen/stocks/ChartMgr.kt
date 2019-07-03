package com.jaykallen.stocks

import android.content.Context
import android.graphics.Color
import android.text.TextUtils.substring
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.jaykallen.stocks.models.ChartData
import timber.log.Timber
import java.util.*

/**
 * Created by Jay Kallen on 3/20/19.
 */

class ChartMgr {
    private var xLabels = ArrayList<String>()
    private var xMax = 0f
    private var yMin = 0f
    private var yMax = 0f

    fun setupData(chartDataList: List<ChartData>): LineData {
        val dataEntry = ArrayList<Entry>()
        var chart: ChartData
        yMin = chartDataList[0].close.toFloat()
        xMax = (chartDataList.size - 1).toFloat()
        for (i in 0 until chartDataList.size) {
            chart = chartDataList[i]
            xLabels.add(DateUtility.formatIso2West(chart.date))
            dataEntry.add(Entry(i.toFloat(), chart.close.toFloat()))
            yMin = if (chart.close.toFloat() < yMin) chart.close.toFloat() else yMin
            yMax = if (chart.close.toFloat() > yMax) chart.close.toFloat() else yMax
        }
        val lineDataSet = LineDataSet(dataEntry, "Y axis")
        lineDataSet.lineWidth = 1f                              // The width of the chart line
        lineDataSet.color = Color.WHITE                         // The color of the chart line
        lineDataSet.highLightColor = Color.GREEN                // The color of the cross hairs
        lineDataSet.setDrawValues(false)
        lineDataSet.circleRadius = 10f
        lineDataSet.setCircleColor(Color.YELLOW)
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER        // Function to smooth line for abrupt changes
        lineDataSet.cubicIntensity = 0.2f                       // Enables the cubic density : 1 = sharp curves
        lineDataSet.setDrawFilled(true)                         // Fills in the coloring below the smooth line
        lineDataSet.fillColor = Color.BLACK                     // Set the transparency
        lineDataSet.fillAlpha = 80
        lineDataSet.setDrawCircles(false)                       // Removes the circle from the graph
        //lineDataSet.setColor(ColorTemplate.COLORFUL_COLORS);  // Optional
        val iLineDataSetArrayList = ArrayList<ILineDataSet>()
        iLineDataSetArrayList.add(lineDataSet)
        val lineData = LineData(iLineDataSetArrayList)
        lineData.setValueTextSize(13f)
        lineData.setValueTextColor(Color.BLACK)
        return lineData
    }

    fun setupChart(context: Context, lineChart: LineChart): LineChart {

        val legend = lineChart.legend
        legend.isEnabled = false
        legend.form = Legend.LegendForm.LINE
        legend.textColor = Color.WHITE

        val xAxis = lineChart.xAxis
        xAxis.textColor = Color.WHITE
        xAxis.axisMinimum = 0f
        xAxis.axisMaximum = xMax
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(true)
        xAxis.granularity = 6f
        xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)     // This sets the date labels

        val yAxis = lineChart.axisLeft
        yAxis.textColor = Color.WHITE
        yAxis.axisMinimum = yMin
        yAxis.axisMaximum = yMax
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawGridLines(true)

        val yAxisRight = lineChart.axisRight
        yAxisRight.setDrawAxisLine(false)
        yAxisRight.setDrawGridLines(false)

        lineChart.setBackgroundColor(Color.BLACK)
        val marker = CustomMarkerView(context, R.layout.content_marker, xLabels)
        lineChart.marker = marker

        return lineChart
    }
}