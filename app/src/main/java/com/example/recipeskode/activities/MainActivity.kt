package com.example.recipeskode.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeskode.R
import com.example.recipeskode.adapters.RecipeAdapter
import com.example.recipeskode.models.Recipe
import com.example.recipeskode.models.RecipeList
import com.example.recipeskode.network.GetRecipeService
import com.example.recipeskode.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var storedRecipes: List<Recipe>? = null
    var rv : RecipeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        recyclerView_main.layoutManager = LinearLayoutManager(this)
        fetchJSON()

        setupSorting()
    }

    fun fetchJSON() {
        val service = RetrofitClientInstance.retrofitInstance?.create(GetRecipeService::class.java)

        val call = service?.getRecipes()
        call?.enqueue(object : Callback<RecipeList> {

            override fun onResponse(call: Call<RecipeList>, response: Response<RecipeList>) {
                val body = response?.body()
                val recipes = body?.recipes

                runOnUiThread {
                    storedRecipes = recipes
                    rv = RecipeAdapter(applicationContext, storedRecipes!!)
                    recyclerView_main.adapter = rv
                    //recyclerView_main.adapter = RecipeAdapter(recipes)
                }
            }

            override fun onFailure(call: Call<RecipeList>, t: Throwable) {
                println("Failed to execute request")
            }
        })
    }

    fun setupSorting() {
        val sortingOptions = arrayOf("Name", "Last Updated")
        spinnerSort.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            sortingOptions
        )

        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> sortByName()
                    1 -> sortByDate()
                    else -> null
                }
                recyclerView_main.adapter?.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }

        }
    }

    private fun sortByName() {
        //storedRecipes = storedRecipes?.sortedBy { it.name }
        //if (storedRecipes)
        storedRecipes?.let{
            rv?.recipes = it.sortedBy { it.name }
        }
        //rv?.recipes  = storedRecipes!!.sortedBy { it.name }
        //rv?.recipes = storedRecipes!!
        recyclerView_main.adapter?.notifyDataSetChanged()
    }

    private fun sortByDate() {
        storedRecipes?.let{
            rv?.recipes = rv?.recipes!!.sortedByDescending { it.lastUpdated }
            rv?.recipes = it.sortedByDescending { it.lastUpdated }
        }
        recyclerView_main.adapter?.notifyDataSetChanged()
    }
}
