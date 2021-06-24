package com.airat.happyplacesapp.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airat.happyplacesapp.adapters.BaseAdapterCallback
import com.airat.happyplacesapp.adapters.HappyPlaceAdapter
import com.airat.happyplacesapp.database.HappyPlacesDao
import com.airat.happyplacesapp.database.HappyPlacesDatabase
import com.airat.happyplacesapp.databinding.ActivityMainBinding
import com.airat.happyplacesapp.models.HappyPlaceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    companion object{
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        const val EXTRA_PLACE_DETAILS = "placeDetails"
    }

    private lateinit var binding: ActivityMainBinding
    private var happyPlaces = arrayListOf<HappyPlaceModel>()
    private lateinit var dao: HappyPlacesDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dao = HappyPlacesDatabase.getInstance(this).happyPlaceDao
        binding.fabAddHappyPlace.setOnClickListener {
            startActivityForResult(Intent(this@MainActivity, AddHappyPlaceActivity::class.java), ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }



        lifecycleScope.launch(Dispatchers.IO) {
            getHappyPlacesFromLocalDB()
        }
        lifecycleScope.launch(Dispatchers.Main) {
            setupHappyPlacesToRecycleView()

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                recreate()
            }
        }
    }

    private fun setupHappyPlacesToRecycleView() {
        binding.rvHappyPlacesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHappyPlacesList.setHasFixedSize(true)
        val placesAdapter = HappyPlaceAdapter()
        placesAdapter.run {
            setList(happyPlaces)
            attachCallback(object : BaseAdapterCallback<HappyPlaceModel>{
                override fun onItemClick(model: HappyPlaceModel, view: View) {
                    val intentDetail = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                    intentDetail.putExtra(EXTRA_PLACE_DETAILS, model)
                    startActivity(intentDetail)
                }

                override fun onLongClick(model: HappyPlaceModel, view: View): Boolean {
                    TODO("Not yet implemented")
                }
            })
        }
        if (happyPlaces.size != 0) {
            binding.rvHappyPlacesList.visibility = View.VISIBLE
            binding.tvNoRecordsAvailable.visibility = View.GONE
        } else {
            binding.rvHappyPlacesList.visibility = View.GONE
            binding.tvNoRecordsAvailable.visibility = View.VISIBLE
        }

        binding.rvHappyPlacesList.adapter = placesAdapter
    }

    private suspend fun getHappyPlacesFromLocalDB() {
        happyPlaces.addAll(dao.getAllHappyPlaces())
    }
}