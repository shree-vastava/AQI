package com.acous.airqualityindex.ui

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.acous.airqualityindex.util.CityClickListener
import com.acous.airqualityindex.R
import com.acous.airqualityindex.data.CityAqi
import com.acous.airqualityindex.databinding.ActivityMainBinding
import com.acous.airqualityindex.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CityClickListener {

    var mainAdapter = MainAdapter(this)
    private val viewModel: MainViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy<ActivityMainBinding> {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            mainAdapter = mainAdapter
            mainViewModel = viewModel
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.mainAdapter = mainAdapter

        /**
         * Setting up the
         * observers to listen
         * and update the UI on screen
         * with updated data
         */
        setUpObserver()

    }

    /**
     * On any city clicked
     */
    override fun onCityClicked(cityAqi: CityAqi, tvCityName: TextView) {
        var intent = Intent(this, ChartActivity::class.java)
        intent.putExtra(getString(R.string.city_aqi), cityAqi)
        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this,
                android.util.Pair(tvCityName,tvCityName.transitionName)).toBundle())
    }


    /**
     * observer setup
     */
    private fun setUpObserver() {
        viewModel.citiesListAqi.observe(this, Observer {

            when (it.status) {
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    mainAdapter.updateList(ArrayList(it.data!!))
                    recyclerView.animate()
                }
                Status.ERROR -> {

                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        viewModel.closeWebSocket()
    }

    override fun onStart() {
        super.onStart()
        viewModel.subscribeToSocketEvents()
    }
}