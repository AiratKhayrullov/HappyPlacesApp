package com.airat.happyplacesapp.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airat.happyplacesapp.adapters.BaseAdapterCallback
import com.airat.happyplacesapp.adapters.HappyPlaceAdapter
import com.airat.happyplacesapp.database.HappyPlacesDao
import com.airat.happyplacesapp.database.HappyPlacesDatabase
import com.airat.happyplacesapp.databinding.ActivityMainBinding
import com.airat.happyplacesapp.models.HappyPlaceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.kitek.rvswipetodelete.SwipeToDeleteCallback
import pl.kitek.rvswipetodelete.SwipeToEditCallback
import java.lang.Exception

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
            })
        }
        val editSwipeHandler = object : SwipeToEditCallback(this@MainActivity){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.rvHappyPlacesList.adapter as HappyPlaceAdapter
                adapter.notifyEditItem(this@MainActivity, viewHolder.adapterPosition, ADD_PLACE_ACTIVITY_REQUEST_CODE)
            }
        }

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this@MainActivity){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.rvHappyPlacesList.adapter as HappyPlaceAdapter
                try {
                    val item = adapter.getItem(viewHolder.adapterPosition)
                    adapter.deleteItem(viewHolder.adapterPosition)
                    lifecycleScope.launch(Dispatchers.IO) {
                        dao.deleteHappyPlace(item)
                    }
                    Toast.makeText(this@MainActivity, "Item is deleted", Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    Toast.makeText(this@MainActivity, "Smth went wrong...Item isn't deleted", Toast.LENGTH_SHORT).show()
                }

            }

        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(binding.rvHappyPlacesList)

        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding.rvHappyPlacesList)

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