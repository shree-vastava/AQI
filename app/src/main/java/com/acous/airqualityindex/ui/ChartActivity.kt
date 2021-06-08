package com.acous.airqualityindex.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.acous.airqualityindex.R
import com.acous.airqualityindex.data.CityAqi
import com.acous.airqualityindex.util.Status
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_chart.*

@AndroidEntryPoint
class ChartActivity : AppCompatActivity() {

    var aqiEntries = ArrayList<Entry>()
    private val viewModel: ChartViewModel by viewModels()
    lateinit var cityAqi: CityAqi
    var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        cityAqi = intent.getParcelableExtra(getString(R.string.city_aqi))!!

        /**
         * setting up the chart with the properties
         * to show on UI correctly
         */
        setUpChart()


        /**
         * Setting up the line properties
         * to display the data and entries
         */
        setUpLineData()


        /**
         * Setting up obserevrs to
         * update the UI upon changes
         * in the data
         */
        setUpObservers()


        /**
         * control of spinner to
         * update the timer and delay on the
         * value selection from user
         */
        spinner_interval.setSelection(3)
        spinner_interval.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var selected = spinner_interval.selectedItem.toString().toLong()
               if (selected>0) {
                   countDownTimer?.let {
                       countDownTimer?.cancel()
                       countDownTimer = null
                   }

                   setUpTimer(selected*1000)
                   viewModel.setDelay(selected)
                   showToast(getString(R.string.next_interval))
               }
            }

        }
    }

    /**
     * Observers setup
     */
    private fun setUpObservers() {

        viewModel.aqiList.observe(this, Observer {

            when(it.status) {

                Status.ERROR -> {
                    showToast(it.message)
                    countDownTimer?.onFinish()
                }

                Status.SUCCESS -> {
                    countDownTimer?.onFinish()
                    var entry = Entry(aqiEntries.size.toFloat(), it.data!!)
                    updateData(entry)
                }
            }
        })
    }

    private fun showToast(message: String?) {
        Snackbar.make(tv_time_remaining,message!!, 1500).show()
    }


    /**
     * Chart setup
     */
    fun setUpChart(){

        chart_aqi.xAxis.labelRotationAngle = 45f;
        chart_aqi.axisRight.setDrawGridLines(false);
        chart_aqi.axisLeft.setDrawGridLines(false)
        chart_aqi.xAxis.setDrawGridLines(false);
        chart_aqi.axisRight.isEnabled = false
        chart_aqi.xAxis.textSize = 8f
        chart_aqi.legend.isEnabled = false
        var desc = Description()
        desc.text = """${getString(R.string.chart_desc)} ${cityAqi.city}"""
        chart_aqi.description = desc
        chart_aqi.setTouchEnabled(true)
        chart_aqi.xAxis.position = (XAxis.XAxisPosition.BOTTOM)
        chart_aqi.setDrawGridBackground(false)
    }

    /**
     * setting yop the line data
     */
    fun setUpLineData(){
        tv_city_name.text = cityAqi?.city
        aqiEntries.add(Entry(0f,cityAqi?.aqi!!))
        var lineDataSet = LineDataSet(aqiEntries,"")
        lineDataSet.lineWidth = 2f
        lineDataSet.color = getColor(R.color.color_graph_line)
        lineDataSet.setCircleColor(getColor(R.color.color_graph_circle))
        lineDataSet.circleRadius = 3.5f
        lineDataSet.circleHoleColor = getColor(R.color.white)
        var lineData = LineData(lineDataSet)
        chart_aqi.data = lineData
    }

    fun updateData(entry: Entry){
        chart_aqi.data.addEntry(entry, chart_aqi.lineData.dataSetCount-1)
        chart_aqi.data.notifyDataChanged()
        chart_aqi.notifyDataSetChanged();
        chart_aqi.setVisibleXRangeMaximum(10.0f);
        chart_aqi.moveViewToX(chart_aqi.data.entryCount.toFloat());

    }

    override fun onStop() {
        super.onStop()
        viewModel.closeWebSocket()
    }


    override fun onStart() {
        super.onStart()
        viewModel.subscribeToSocketEvents(cityAqi.city)
    }

    private fun setUpTimer(maxSeconds:Long=20000L) {
        countDownTimer = object: CountDownTimer(maxSeconds,1000){
            override fun onFinish() {
                start()
            }

            override fun onTick(p0: Long) {
                tv_timer_count.text = (p0/1000).toString()
            }

        }

        countDownTimer?.start()
    }

}