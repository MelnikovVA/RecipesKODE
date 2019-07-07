package com.example.recipeskode.activities

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.recipeskode.R
import com.example.recipeskode.adapters.ViewPagerAdapter
import com.example.recipeskode.models.Recipe
import kotlinx.android.synthetic.main.recipe_details.*

class DetailsActivity : AppCompatActivity() {
    private lateinit var clickedRecipe: Recipe
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_details)

        val i: Intent = intent
        clickedRecipe = i.getParcelableExtra("clickedRecipe")

        renderData()
        setupViewPagerIndicator()
    }

    private fun renderData() {
        textViewName.text = clickedRecipe.name
        ratingBar2.rating = clickedRecipe.difficulty.toFloat()
        webViewDescriptionText.loadData(clickedRecipe.instructions, "text/html; charset=utf-8", "UTF-8")
        webViewInstructionsText.loadData(clickedRecipe.description, "text/html; charset=utf-8", "UTF-8")

        var adapter = ViewPagerAdapter(this, clickedRecipe.images)
        viewPagerImageScroll.adapter = adapter
    }

    private fun setupViewPagerIndicator() {
        val imagesCount = clickedRecipe.images.size
        val dotsCount = minOf(imagesCount, 6)
        val viewPagerDots = mutableListOf<ImageView>()

        if (imagesCount != 1) { // If there is only one image, we don't need the indicator at all
            for (i in 0 until dotsCount) {
                viewPagerDots.add(ImageView(this))
                viewPagerDots[i].setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.dot_view_pager_inactive
                    )
                )
                linearLayoutViewPagerIndicator.addView(viewPagerDots[i])
            }

            viewPagerDots[0].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_view_pager_active))
            viewPagerImageScroll.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    for (i in 0 until viewPagerDots.size) {
                        viewPagerDots[i].setImageDrawable(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.dot_view_pager_inactive
                            )
                        )
                    }
                    when {
                        // In case there are more than 6 images, the active dot will only be changed after a certain number of images has been scrolled
                        imagesCount <= 6 -> {
                            viewPagerDots[position].setImageDrawable(
                                ContextCompat.getDrawable(
                                    applicationContext,
                                    R.drawable.dot_view_pager_active
                                )
                            )
                        }
                        else -> {
                            if (position == imagesCount - 1) {
                                viewPagerDots[dotsCount - 1].setImageDrawable(
                                    ContextCompat.getDrawable(
                                        applicationContext,
                                        R.drawable.dot_view_pager_active
                                    )
                                )
                            } else {
                                viewPagerDots[position / ((imagesCount / dotsCount) + 2)].setImageDrawable(
                                    ContextCompat.getDrawable(
                                        applicationContext,
                                        R.drawable.dot_view_pager_active
                                    )
                                )
                            }
                        }
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })
        }
    }
}