package com.example.recipeman.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.recipeman.R
import com.example.recipeman.retrofit.RecipeHit
import com.squareup.picasso.Picasso

class CustomAdapter(
    private val recipeList: ArrayList<RecipeHit>,
    private val onItemClicked: (RecipeHit) -> Unit
): RecyclerView.Adapter<CustomAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
        return RecipeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentRecipe = recipeList[position]
        Picasso.get()
            .load(currentRecipe.recipe?.images?.REGULAR?.url)
            .into(holder.imageView)
        holder.nameTextView.text = currentRecipe.recipe?.label
        holder.layout.setOnClickListener {
            onItemClicked(currentRecipe)
        }
    }

    class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView
        var nameTextView: TextView
        var likeButton: LottieAnimationView
        var layout: ConstraintLayout

        init {
            imageView = view.findViewById(R.id.recipe_image_view)
            nameTextView = view.findViewById(R.id.name_text_view)
            layout = view.findViewById(R.id.container)
            likeButton = view.findViewById(R.id.animationView)

            likeButton.setOnClickListener {
                likeButton.playAnimation()
            }

        }

    }
}