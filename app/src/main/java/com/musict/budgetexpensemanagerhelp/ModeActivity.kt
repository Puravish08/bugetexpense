package com.musict.budgetexpensemanagerhelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.musict.budgetexpensemanagerhelp.adapter.ModeAdapter
import com.musict.budgetexpensemanagerhelp.databinding.ActivityModeBinding
import com.musict.budgetexpensemanagerhelp.sqlite.ModeSqlLite

class ModeActivity : AppCompatActivity() {



    lateinit var binding: ActivityModeBinding
    lateinit var database: ModeSqlLite
    lateinit var  adapter: ModeAdapter
    private val addedCategories = HashSet<String>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = ModeSqlLite(this)
        view()



    }

    private fun view() {


        binding.btnClear.setOnClickListener {
            openClearConfirmationDialog()
        }



        binding.btnAdd.setOnClickListener {

            val categoryName = binding.edtMode.text.toString().trim()

            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show()
            } else if (categoryName.length <= 3 || categoryName.length >= 15) {
                Toast.makeText(this, "Please enter a valid category name", Toast.LENGTH_SHORT).show()
            } else if (addedCategories.contains(categoryName)) {
                Toast.makeText(this, "Category already exists", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Added Successfully...", Toast.LENGTH_SHORT).show()
                database.modeinsert(categoryName)
                addedCategories.add(categoryName)
                binding.edtMode.text.clear()
            }

        }
            var mlist = database.displaymode()

            val adapter = ModeAdapter(mlist){ selectedCategory ->

//                Toast.makeText(this, "Selected category: $selectedCategory", Toast.LENGTH_SHORT).show()
            }



        val initialData = database.displaymode()
        adapter.updateData(initialData)




        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            binding.rcvMode.layoutManager = manager
            binding.rcvMode.adapter = adapter




            binding.imgback.setOnClickListener {

                onBackPressed()

            }


            binding.imgok.setOnClickListener {

                onBackPressed()

            }



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