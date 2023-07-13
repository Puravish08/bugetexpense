package com.musict.budgetexpensemanagerhelp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.musict.budgetexpensemanagerhelp.databinding.ActivityReportsBinding

import com.musict.budgetexpensemanagerhelp.sqlite.SqlLiteDataHelper
import android.os.Build
import android.view.Window
import android.view.WindowManager

// ...

// Change status bar color



class ReportsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportsBinding
    private lateinit var pieChart: PieChart
    private lateinit var dbHelper: SqlLiteDataHelper






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor = resources.getColor(R.color.black, null)
            }
        }



        pieChart = PieChart(this)
//        generateReport()



        dbHelper = SqlLiteDataHelper(this)



        val totalIncomeTextView = findViewById<TextView>(R.id.tvIncomeTotal)
        val totalExpenseTextView = findViewById<TextView>(R.id.tvExpenseTotal)

        val incomePieChart = binding.incomePieChart
        val expensePieChart = binding.expensePieChart

        dbHelper.generateIncomeReport(incomePieChart, totalIncomeTextView)
        dbHelper.generateExpenseReport(expensePieChart, totalExpenseTextView)





        // stustus bar randome colors change codes

//        val window: Window = window
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            // For Lollipop and above
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            val randomColor = Color.rgb(
//                (Math.random() * 256).toInt(),
//                (Math.random() * 256).toInt(),
//                (Math.random() * 256).toInt()
//            )
//            window.statusBarColor = randomColor
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // For KitKat and above
//            val randomColor = Color.rgb(
//                (Math.random() * 256).toInt(),
//                (Math.random() * 256).toInt(),
//                (Math.random() * 256).toInt()
//            )
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            val decorView = window.decorView as ViewGroup
//            val statusBarView = View(applicationContext)
//            val params = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()
//            )
//            statusBarView.setBackgroundColor(randomColor)
//            decorView.addView(statusBarView, params)
//        }

        // Helper method to get status bar height



    }




//    @SuppressLint("InternalInsetResource")
//    private fun getStatusBarHeight(): Int {
//        var result = 0
//        val resourceId = resources.getIdentifier(
//            "status_bar_height", "dimen", "android"
//        )
//        if (resourceId > 0) {
//            result = resources.getDimensionPixelSize(resourceId)
//        }
//        return result
//    }




    }