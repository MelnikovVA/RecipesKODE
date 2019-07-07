package com.example.recipeskode.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.recipeskode.R
import com.example.recipeskode.adapters.ViewPagerAdapter
import com.example.recipeskode.models.Recipe
import kotlinx.android.synthetic.main.recipe_details.*

class DetailsActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_details)

        val i: Intent = intent
        val clickedRecipe: Recipe = i.getParcelableExtra("clickedRecipe")

        textViewName.text =  clickedRecipe.name
        textViewDescriptionText.text =  clickedRecipe.description
        textViewDifficulty.text = "Difficulty: ${clickedRecipe.difficulty.toString()}"
        textViewInstructionsText.text =  clickedRecipe.instructions

        var adapter = ViewPagerAdapter(this, clickedRecipe.images)
        viewPagerImageScroll.adapter = adapter
    }
}