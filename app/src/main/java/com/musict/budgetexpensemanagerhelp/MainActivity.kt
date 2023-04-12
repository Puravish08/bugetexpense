package com.musict.budgetexpensemanagerhelp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.core.view.GravityCompat
import com.musict.budgetexpensemanagerhelp.databinding.ActivityMainBinding
import java.security.AllPermission
import kotlin.math.exp

class MainActivity : AppCompatActivity() {


    lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        initview()
    }

    private fun initview() {


        var title_income = "Add Income"
        binding.addInconme.setOnClickListener{

            var income = Intent(this,IncomeActivity::class.java)
            income.putExtra("Page","income")
            income.putExtra("title",title_income)
            startActivity(income)

        }


        var title_expence="Add Expense"
        binding.addExpenses.setOnClickListener{


            var expense = Intent(this,IncomeActivity::class.java)
            expense.putExtra("Page","expense")
            expense.putExtra("title",title_expence)
            startActivity(expense)

        }


        binding.allTransaction.setOnClickListener{

            var a = Intent(this,TransactionActivity::class.java)
            startActivity(a)

        }

        binding.reports.setOnClickListener{


            var r = Intent(this,ReportsActivity::class.java)
            startActivity(r)

        }


        binding.mode.setOnClickListener{

            var c = Intent(this,ModeActivity::class.java)
            startActivity(c)

        }

        binding.category.setOnClickListener{

            var m = Intent(this,CategoriesActivity::class.java)
            startActivity(m)

        }


binding.menuopen.setOnClickListener {


    binding.drawalLayout.openDrawer(GravityCompat.START)
}








    }




}