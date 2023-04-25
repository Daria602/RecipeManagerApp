package com.example.recipeman.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.recipeman.R
import com.example.recipeman.models.RecipeModel
import com.example.recipeman.databinding.FragmentSecondBinding
import com.squareup.picasso.Picasso

class SecondFragment : Fragment() {
    private lateinit var binding: FragmentSecondBinding
    private lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondBinding.inflate(inflater, container, false)
        linearLayout = binding.ingredientListContainer
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {

            val ingredientListBundle = bundle.getStringArrayList("RECIPE_INGREDIENTS")!!.toList()
            for (ingredient in ingredientListBundle) {
                val tvIngredientItem : TextView =
                    layoutInflater.inflate(R.layout.item_listview, linearLayout, false) as TextView
                tvIngredientItem.text = ingredient
                linearLayout.addView(tvIngredientItem)
            }
            Picasso.get()
                .load(bundle.getString("RECIPE_IMAGE"))
                .into(binding.recipePicture)
            binding.recipeLabel.text = bundle.getString("RECIPE_NAME")
            binding.recipeCalories.text = bundle.getString("RECIPE_CALORIES")
//            binding.button.setOnClickListener{
//
//            }
        }
    }
}