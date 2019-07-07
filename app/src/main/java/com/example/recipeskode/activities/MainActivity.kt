package com.example.recipeskode.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
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
    var rv: RecipeAdapter? = null
    var currentSearchOption: String = "name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        populateData()
        setupSearching()
        setupSorting()
    }

    private fun populateData() {
        val service = RetrofitClientInstance.retrofitInstance?.create(GetRecipeService::class.java)
        val call = service?.getRecipes()
        call?.enqueue(object : Callback<RecipeList> {

            override fun onResponse(call: Call<RecipeList>, response: Response<RecipeList>) {
                val body = response?.body()
                val recipes = body?.recipes

                runOnUiThread {
                    storedRecipes = recipes
                    rv = RecipeAdapter(applicationContext, storedRecipes!!)
                    recyclerView_main.layoutManager = LinearLayoutManager(this@MainActivity)
                    recyclerView_main.adapter = rv
                }
            }

            override fun onFailure(call: Call<RecipeList>, t: Throwable) {
                println("Failed to execute request")
            }
        })
    }

    private fun setupSearching() {
        val searchOptions = arrayOf("Search by:", "Name", "Description", "Instructions")
        spinnerSearchItemsOptions.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            searchOptions
        )

        //Filter by name by default
        spinnerSearchItemsOptions.setSelection(0)
        spinnerSearchItemsOptions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    1 -> currentSearchOption = "name"
                    2 -> currentSearchOption = "description"
                    3 -> currentSearchOption = "instructions"
                    else -> null
                }
                // Changing the filtering option should result in a new displayed list
                filterItems(editTextSearchItems.text.toString(), currentSearchOption)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        editTextSearchItems.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                filterItems(p0.toString(), currentSearchOption)
                // When erasing the text (thus getting more items satisfying the condition), sorting should still work
                sortItems(spinnerSortItemsOptions.selectedItemPosition)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun setupSorting() {
        val sortingOptions = arrayOf("Sort by:", "Name", "Last Updated")
        spinnerSortItemsOptions.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            sortingOptions
        )

        spinnerSortItemsOptions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sortItems(position)
                recyclerView_main.adapter?.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun sortItems(spinnerPosition: Int) {
        rv?.recipes?.let {
            when (spinnerPosition){
                1 -> rv?.recipes = rv?.recipes!!.sortedBy { it.name }
                2 -> rv?.recipes = rv?.recipes!!.sortedByDescending { it.lastUpdated }
                else -> null
            }
        }
    }

    private fun filterItems(text: String, searchBy: String) {
        var filteredItems: MutableList<Recipe> = mutableListOf<Recipe>()

        storedRecipes?.let {
            for (recipe in it) {
                when (searchBy) {
                    "name" ->
                        if (recipe.name.toLowerCase().contains(text.toLowerCase())) {
                            filteredItems.add(recipe)
                        }
                    "description" ->
                        recipe.description?.let {
                            if (recipe.description?.toLowerCase()!!.contains(text.toLowerCase()) ||
                                text == ""
                            ) {
                                filteredItems.add(recipe)
                            }
                        }
                    "instructions" ->
                        if (recipe.instructions.toLowerCase().contains(text.toLowerCase())) {
                            filteredItems.add(recipe)
                        }
                }
            }
        }
        rv?.recipes = filteredItems
        recyclerView_main.adapter?.notifyDataSetChanged()
    }
}
