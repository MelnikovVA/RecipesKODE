package com.example.recipeskode.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeskode.R
import com.example.recipeskode.models.Recipe
import kotlinx.android.synthetic.main.recipe_item_list.view.*

class RecipeAdapter(val context: Context, val recipes: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recipe_item_list, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}