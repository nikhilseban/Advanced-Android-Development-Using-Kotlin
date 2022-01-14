package com.nick.foodRecipes.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nick.foodRecipes.data.model.local.Recipe
import com.nick.foodRecipes.databinding.RecipesRowLayoutBinding

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    private var mRecipes = emptyList<Recipe>()

    class RecipeViewHolder(private val binding: RecipesRowLayoutBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(recipe:Recipe){
            binding.recipe = recipe
            binding.executePendingBindings()
        }

        companion object{
            fun getViewHolder(parentViewGroup: ViewGroup):RecipeViewHolder{
                val layoutInflater = LayoutInflater.from(parentViewGroup.context)
                val binding = RecipesRowLayoutBinding.inflate(layoutInflater,parentViewGroup,false)
                return RecipeViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesAdapter.RecipeViewHolder {
        return RecipeViewHolder.getViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(mRecipes[position])
    }

    override fun getItemCount(): Int {
        return mRecipes.count()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRecipes(recipes: List<Recipe>){
        mRecipes = recipes
        notifyDataSetChanged()
    }
}