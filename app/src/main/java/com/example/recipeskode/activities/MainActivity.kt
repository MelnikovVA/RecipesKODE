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

        fetchJSON()
        setupSearching()
        setupSorting()
    }

    private fun fetchJSON() {
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

        spinnerSearchItemsOptions.setSelection(0)
        spinnerSearchItemsOptions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
//CHANGE TO "UPDATE_SEARCH FUNCTION THAT WOULD TAKE currentSearchOption VALUE AND REFRESH THE LIST ACCORDING TO THE CURRENT TEXT TYPED
                    1 -> currentSearchOption = "name"
                    2 -> currentSearchOption = "description"
                    3 -> currentSearchOption = "instructions"
                    else -> null
                }
                recyclerView_main.adapter?.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        editTextSearchItems.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                filterItems(p0.toString(), currentSearchOption)
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
        when (spinnerPosition) {
            1 -> {
                rv?.recipes?.let {
                    //storedRecipes = it.sortedBy { it.name }
                    rv?.recipes = rv?.recipes!!.sortedBy { it.name }
                    //rv?.recipes = storedRecipes!!
                }
            }
            2 -> {
                rv?.recipes?.let {
                    //storedRecipes = it.sortedByDescending { it.lastUpdated }
                    rv?.recipes = rv?.recipes!!.sortedByDescending { it.lastUpdated }
                    //rv?.recipes = storedRecipes!!
                }
            }
            else -> null
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
                            if (recipe.description?.toLowerCase()!!.contains(text.toLowerCase())) {
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
