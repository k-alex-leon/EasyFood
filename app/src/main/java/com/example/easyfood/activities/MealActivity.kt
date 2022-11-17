package com.example.easyfood.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.database.MealDatabase
import com.example.easyfood.databinding.ActivityMealBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.MealViewModel
import com.example.easyfood.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mMealMvvm : MealViewModel

    private lateinit var mMealId : String
    private lateinit var mMealName : String
    private lateinit var mMealThumb : String
    private lateinit var mYoutubeUrl : String

    private var mMealToSave : Meal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mMealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getIntentMealInformation()

        setInformationInViews()

        loadingCase()

        // conecta con la api / realiza la busqueda con el id del meal
        mMealMvvm.getMealDetail(mMealId)

        observerMealDetailsLiveData()

        onYoutubeIconClicked()
        onFavoriteClicked()
    }

    @SuppressLint("ResourceAsColor")
    private fun onFavoriteClicked() {

        binding.lavLike.setOnClickListener {
            mMealToSave?.let {
                binding.lavLike.playAnimation()
                mMealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeIconClicked() {
        binding.imgVYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mYoutubeUrl))
            startActivity(intent)
        }
    }

    // observador de cambios / obtiene mas info del meal seleccionado
    private fun observerMealDetailsLiveData() {
        mMealMvvm.observerMealDetailLiveData().observe(this, object : Observer<Meal>{
            @SuppressLint("SetTextI18n")
            override fun onChanged(meal: Meal?) {
                onResponseCase()
                mMealToSave = meal
                binding.txtVMealCategory.text = "Category : ${meal!!.strCategory}"
                binding.txtVMealLocation.text = "Area : ${meal.strArea}"
                binding.txtVInstructions.text = meal.strInstructions

                mYoutubeUrl = meal.strYoutube.toString()
            }

        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mMealThumb)
            .into(binding.imgVMealDetail)

        binding.collapsingToolBar.title = mMealName
        binding.collapsingToolBar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolBar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    // obtenemos data del intent enviado desde homeFragment
    private fun getIntentMealInformation() {
        val intent = intent
        mMealId = intent.getStringExtra("meal_id")!!
        mMealName = intent.getStringExtra("meal_name")!!
        mMealThumb = intent.getStringExtra("meal_thumb")!!
    }

    // quita las views mientras carga la data
    private fun loadingCase(){
        binding.lavLike.visibility = View.GONE
        binding.imgVYoutube.visibility = View.GONE
        binding.pbMealDetail.visibility = View.VISIBLE
        binding.txtVMealCategory.visibility = View.GONE
        binding.txtVInstructions.visibility = View.GONE
        binding.txtVMealLocation.visibility = View.GONE
    }

    private fun onResponseCase(){
        binding.lavLike.visibility = View.VISIBLE
        binding.imgVYoutube.visibility = View.VISIBLE
        binding.pbMealDetail.visibility = View.GONE
        binding.txtVMealCategory.visibility = View.VISIBLE
        binding.txtVInstructions.visibility = View.VISIBLE
        binding.txtVMealLocation.visibility = View.VISIBLE
    }
}