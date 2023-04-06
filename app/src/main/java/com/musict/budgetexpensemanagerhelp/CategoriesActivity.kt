package com.musict.budgetexpensemanagerhelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.musict.budgetexpensemanagerhelp.databinding.ActivityCategoriesBinding
import com.musict.budgetexpensemanagerhelp.sqlite.SqlLiteDataHelper

class CategoriesActivity : AppCompatActivity() {


    lateinit var binding: ActivityCategoriesBinding
    lateinit var database: SqlLiteDataHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = SqlLiteDataHelper(this)
        view()

    }

    private fun view() {

        binding.btnAdd.setOnClickListener {

            var categoryName = binding.edtCategories.text.toString()



            if (categoryName.isEmpty()) {

                Toast.makeText(this, "Please Enter Field", Toast.LENGTH_SHORT).show()
            } else if (categoryName.length <= 3 || categoryName.length >= 15) {
                Toast.makeText(this, "Please Enter Valid Field", Toast.LENGTH_SHORT).show()

            }

            database.categoriesinsert(categoryName)


//            val adapter = CatogoryAdapter(list)
//            val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//
//            binding.rcvCategory.layoutManager = manager
//            binding.rcvCategory.adapter = adapter
            //        var list = database.displayCategory()

        }
    }
}