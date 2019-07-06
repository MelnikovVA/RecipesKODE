package com.example.recipeskode.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeskode.R
import com.example.recipeskode.models.Recipe
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recipe_item_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class RecipeAdapter(val context: Context, val recipes: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recipe_item_list, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder?.itemView?.textCardName?.text = recipe.name
        holder?.itemView?.textCardDescription?.text = recipe.description

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(recipe.lastUpdated.toLong() * 1000)
        holder?.itemView?.textCardLastUpdated.text = "Last Updated: ${sdf.format(date)}"

        val recipePreviewImage = holder?.itemView?.imageRecipe
        Picasso.get().load(recipe.images[0]).into(recipePreviewImage)


    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}