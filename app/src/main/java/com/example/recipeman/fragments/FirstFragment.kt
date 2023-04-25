package com.example.recipeman.fragments

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.recipeman.R
import com.example.recipeman.adapters.CustomAdapter
import com.example.recipeman.databinding.FragmentFirstBinding
import com.example.recipeman.models.RecipeModel
import com.example.recipeman.utils.APP_ID
import com.example.recipeman.utils.APP_KEY
import com.example.recipeman.utils.BASE_URL
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var recipeList: ArrayList<RecipeModel>
    private lateinit var temporaryRecipeList: ArrayList<RecipeModel>
    private lateinit var searchView: SearchView
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeList = arrayListOf<RecipeModel>()
        temporaryRecipeList = arrayListOf<RecipeModel>()

        //searchView = findViewById()

        // TODO: redo the thing with network calls on main thread
        getRecipes("")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFirstBinding.inflate(inflater, container, false)
//        binding.btnBreakfast.tvText.typeface = Typeface.DEFAULT_BOLD
//        binding.btnLunch.tvText.typeface = Typeface.DEFAULT_BOLD
//        binding.btnDinner.tvText.typeface = Typeface.DEFAULT_BOLD
//        binding.btnDesert.tvText.typeface = Typeface.DEFAULT_BOLD
        searchView = binding.searchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {

                    recipeList.clear()
                    temporaryRecipeList.clear()
                    getRecipes(query)
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                }
                searchView.clearFocus()


                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchView.isSubmitButtonEnabled = newText.isNotEmpty()
                return false
            }

        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CustomAdapter(temporaryRecipeList) { recipeModel ->
            onRecipeClicked(recipeModel)
        }

        binding.recyclerView.adapter = adapter
    }

    // TODO: redo this
    // On click on item, open new fragment
    private fun onRecipeClicked(recipeModel: RecipeModel) {
        val bundle = Bundle()
        bundle.putString("RECIPE_IMAGE", recipeModel.image)
        bundle.putString("RECIPE_NAME", recipeModel.label)
        bundle.putString("RECIPE_CALORIES", recipeModel.calories)
        bundle.putStringArrayList("RECIPE_INGREDIENTS", recipeModel.ingredientLines)
        bundle.putParcelable("RECIPE", recipeModel)

        val fragment = SecondFragment()
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun getRecipes(query: String) {
        val urlBuilder: HttpUrl.Builder = BASE_URL.toHttpUrlOrNull()!!.newBuilder()

        // If the user didn't search for anything, make it a random word,
        // otherwise keep the search
        val newQuery: String = query.ifEmpty {
            getRandomQuery()
        }

        urlBuilder.addQueryParameter("type", "public")
        urlBuilder.addQueryParameter("q", newQuery)
        urlBuilder.addQueryParameter("app_id", APP_ID)
        urlBuilder.addQueryParameter("app_key", APP_KEY)

        val urlWithParams: String = urlBuilder.build().toString()

        val request = Request.Builder()
            .url(urlWithParams)
            .build()
        val call: Call = client.newCall(request)
        val response = call.execute()

        putRecipesIntoList(response)
    }

    private fun putRecipesIntoList(response: Response) {
        val jsonObject = JSONTokener(response.body!!.string()).nextValue() as JSONObject
        val hits = jsonObject.getJSONArray("hits")

        for (i in 0 until hits.length()) {
            val recipeJSONObject = hits.getJSONObject(i).getJSONObject("recipe")
            val recipeId = recipeJSONObject.getString("uri")
                .split("#recipe_")
                .last()
            val label = recipeJSONObject.getString("label")
            val image = recipeJSONObject.getString("image")
            val sourceURL = recipeJSONObject.getString("source")
            val ingredientLines =
                recipeJSONObject.getJSONArray("ingredientLines").toArrayList()
            val calories = recipeJSONObject.getDouble("calories").toInt().toString()
            val mealTypes = recipeJSONObject.getJSONArray("mealType").toArrayList()

            recipeList.add(
                RecipeModel(
                    recipeId = recipeId,
                    label = label,
                    image = image,
                    sourceURL = sourceURL,
                    ingredientLines = ingredientLines,
                    calories = calories,
                    mealTypes = mealTypes
                )
            )

        }
        temporaryRecipeList.addAll(recipeList)
    }

    /**
     *  If the user didn't search for anything, random word will be returned
     */
    private fun getRandomQuery(): String {
        val arrayOfRandomFoods: ArrayList<String> =
            arrayListOf("something", "tasty", "delicious", "recipe")
        return arrayOfRandomFoods.random()
    }

    /**
     * Convert JSONArray (of Strings) to ArrayList<String>
     */
    private fun JSONArray.toArrayList(): ArrayList<String> {
        val list = arrayListOf<String>()
        for (i in 0 until this.length()) {
            list.add(this.getString(i))
        }
        return list
    }
}