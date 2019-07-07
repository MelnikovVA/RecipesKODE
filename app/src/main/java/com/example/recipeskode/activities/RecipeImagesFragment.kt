package com.example.recipeskode.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipeskode.R

//1
class RecipeImagesFragment : Fragment() {

    //2
    companion object {

        fun newInstance(): RecipeImagesFragment {
            return RecipeImagesFragment()
        }
    }

    //3
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.recipe_images_fragment, container, false)
    }

}