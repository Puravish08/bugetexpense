package com.musict.budgetexpensemanagerhelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.musict.budgetexpensemanagerhelp.adapter.CatogoryAdapter
import com.musict.budgetexpensemanagerhelp.databinding.ActivityCategoriesBinding
import com.musict.budgetexpensemanagerhelp.sqlite.SqlLiteDataHelper

class CategoriesActivity : AppCompatActivity() {


    lateinit var binding: ActivityCategoriesBinding
    lateinit var database: SqlLiteDataHelper

    private lateinit var adapter: CatogoryAdapter

    private val addedCategories = HashSet<String>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = SqlLiteDataHelper(this)
        view()

    }

    private fun view() {

        binding.btnAdd.setOnClickListener {
            val categoryName = binding.edtCategories.text.toString().trim()

            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show()
            } else if (categoryName.length <= 3 || categoryName.length >= 15) {
                Toast.makeText(this, "Please enter a valid category name", Toast.LENGTH_SHORT).show()
            } else if (addedCategories.contains(categoryName)) {
                Toast.makeText(this, "Category already exists", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Added Successfully...", Toast.LENGTH_SHORT).show()
                database.categoriesinsert(categoryName)
                addedCategories.add(categoryName)
                binding.edtCategories.text.clear()
            }
        }



        binding.btnClear.setOnClickListener {
            openClearConfirmationDialog()
        }



        binding.imgback.setOnClickListener {

                onBackPressed()

            }


            binding.imgok.setOnClickListener {

                onBackPressed()

            }

            var list = database.displayCategory()
        val adapter = CatogoryAdapter(list) { selectedCategory ->
//            Toast.makeText(this, "Selected category: $selectedCategory", Toast.LENGTH_SHORT).show()
        }



        val initialData = database.displayCategory()
        adapter.updateData(initialData)

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            binding.rcvCategory.layoutManager = manager
            binding.rcvCategory.adapter = adapter

    }

    private fun openClearConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("Clear Data")
            .setMessage("Are you sure you want to clear all data?")
            .setPositiveButton("Clear") { dialog, _ ->
                clearData()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun clearData() {
        // Clear database and added categories
        database.clearCategories()
        addedCategories.clear()
        // Clear adapter's data and notify the change
        adapter.updateData(emptyList())
    }



}