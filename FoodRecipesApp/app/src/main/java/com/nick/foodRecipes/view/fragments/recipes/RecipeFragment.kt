package com.nick.foodRecipes.view.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nick.foodRecipes.R
import com.nick.foodRecipes.utils.NetworkResult
import com.nick.foodRecipes.utils.singleLiveEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipe.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeFragment : Fragment() {


    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        readDatabase()

    }

    private fun readDatabase() {
        lifecycleScope.launch {
            recipesViewModel.readRecipes.singleLiveEvent(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    Log.d("RecipesFragment", "readDatabase called!")
                    mAdapter.setRecipes(database[0].foodRecipe.recipes)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            })
        }
    }

    private fun requestApiData() {
        recipesViewModel.getRecipes(recipesViewModel.applyQueries())
        recipesViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setRecipes(it.recipes) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading ->{
                    showShimmerEffect()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        recyclerview.adapter = mAdapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        recyclerview.visibility = View.GONE
        shimmerRecyclerview.visibility = View.VISIBLE
        shimmerRecyclerview.startShimmer()
    }

    private fun hideShimmerEffect() {
        shimmerRecyclerview.visibility = View.GONE
        recyclerview.visibility = View.VISIBLE
        shimmerRecyclerview.stopShimmer()
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            recipesViewModel.readRecipes.observe(viewLifecycleOwner, {database->
                if (database.isNotEmpty()) {
                    mAdapter.setRecipes(database[0].foodRecipe.recipes)
                }
            })
        }
    }

}