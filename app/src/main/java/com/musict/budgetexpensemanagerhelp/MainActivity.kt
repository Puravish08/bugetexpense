package com.musict.budgetexpensemanagerhelp


import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.musict.budgetexpensemanagerhelp.databinding.ActivityMainBinding
import com.musict.budgetexpensemanagerhelp.sqlite.SqlLiteDataHelper
import io.github.muddz.styleabletoast.StyleableToast
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class MainActivity : AppCompatActivity() {


    private lateinit var pieChart: PieChart
    lateinit var binding: ActivityMainBinding

    private val packageName = "com.example.myapp"



    private lateinit var dbHelper: SqlLiteDataHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initview()

        pieChart = findViewById(R.id.pieChart) // Replace with the actual ID of your PieChart view
        dbHelper = SqlLiteDataHelper(this)
        displayPieChart()


        openAppAndNavigate(this, "com.musict.budgetexpensemanagerhelp.FingerPrintuseSqitch");



    }


    private fun openAppAndNavigate(context: Context, packageName: String) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)




        binding.fingerprintlock.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                openFingerprintActivity()
            } else
            {
                openMainActivity()
            }
        }


    }

    private fun openFingerprintActivity() {
        val intent = Intent(this, FingerPrintuseSqitch::class.java)
        startActivity(intent)
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }



    private fun displayPieChart() {
        val data = dbHelper.getAllIncomeExpenses()

        val categoryAmountMap = HashMap<String, Float>()
        var totalIncome = 0f
        var totalExpense = 0f

        for (item in data) {
            val category = item.mode
            val amount = item.amount.toFloat()

            if (categoryAmountMap.containsKey(category)) {
                val sum = categoryAmountMap[category] ?: 0f
                categoryAmountMap[category] = sum + amount
            } else {
                categoryAmountMap[category] = amount
            }

            if (category == "income") {
                totalIncome += amount
            } else if (category == "expense") {
                totalExpense += amount
            }
        }

        val entries = ArrayList<PieEntry>()

        for (categoryAmountEntry in categoryAmountMap.entries) {
            val category = categoryAmountEntry.key
            val amount = categoryAmountEntry.value

            entries.add(PieEntry(amount, category))
        }

        val dataSet = PieDataSet(entries, " ")

        val colors = ArrayList<Int>()
        colors.add(Color.GREEN) // Set green color for income
        colors.add(Color.RED) // Set red color for expenses
        dataSet.colors = colors

        val pieData = PieData(dataSet)

        pieChart.data = pieData
        pieChart.invalidate()

        val incomeText = "Income: %.2f".format(totalIncome)
        val expenseText = "Expense: %.2f".format(totalExpense)
        val totalText = "Total: %.2f".format(totalIncome + totalExpense)

        // Replace `incomeTextView`, `expenseTextView`, and `totalTextView` with the actual TextViews
        binding.incomeTextView.text = incomeText
        binding.incomeTextView.setTextColor(Color.BLUE)

        binding.expenseTextView.text = expenseText
        binding.expenseTextView.setTextColor(Color.RED)

        binding.totalTextView.text = totalText
        binding.totalTextView.setTextColor(Color.BLACK)
    }
















    private fun initview() {


        var title_income = "Add Income"
        binding.addInconme.setOnClickListener {

            var income = Intent(this, IncomeActivity::class.java)
            income.putExtra("Page", "income")
            income.putExtra("title", title_income)
            startActivity(income)

        }

        binding.calnderlin.setOnClickListener {


            var cal = Intent(this, CalenderActivity::class.java)
            startActivity(cal)

            binding.drawalLayout.closeDrawer(GravityCompat.START)


        }
        binding.calanderin.setOnClickListener {


            var cal = Intent(this, CalenderActivity::class.java)
            startActivity(cal)

        }


        var title_expence = "Add Expense"
        binding.addExpenses.setOnClickListener {


            var expense = Intent(this, IncomeActivity::class.java)
            expense.putExtra("Page", "expense")
            expense.putExtra("title", title_expence)
            startActivity(expense)

        }


        binding.allTransaction.setOnClickListener {

            var a = Intent(this, TransactionActivity::class.java)
            startActivity(a)

        }
        binding.privacyopolicy.setOnClickListener {

            val privacyPolicyIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://secureprivacy2.blogspot.com/2023/07/bugetexpenssemangerhelp-privacy-policy.html")
            )
            startActivity(privacyPolicyIntent)
            binding.drawalLayout.closeDrawer(GravityCompat.START)

        }
        binding.tearmsandcondition.setOnClickListener {

            val termsIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://secureprivacy2.blogspot.com/2023/07/bugetexpenssemangerhelp-terms-of-use.html")
            )
            startActivity(termsIntent)
            binding.drawalLayout.closeDrawer(GravityCompat.START)

        }

        binding.reports.setOnClickListener {


            var r = Intent(this, ReportsActivity::class.java)
            startActivity(r)

        }


        binding.mode.setOnClickListener {

            var c = Intent(this, ModeActivity::class.java)
            startActivity(c)
            binding.drawalLayout.closeDrawer(GravityCompat.START)


        }

        binding.ratetheapp.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_rate_us, null)
            val moodImageView = dialogView.findViewById<ImageView>(R.id.moodImageView)
            val titleTextView = dialogView.findViewById<TextView>(R.id.titleTextView)
            val messageTextView = dialogView.findViewById<TextView>(R.id.messageTextView)
            val ratingBar = dialogView.findViewById<MaterialRatingBar>(R.id.ratingBar)
            val rateButton = dialogView.findViewById<Button>(R.id.rateButton)
            val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)

            titleTextView.text = "Rate Us"
            messageTextView.text = "Enjoying the app? Please take a moment to rate it."

            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setView(dialogView)
            dialogBuilder.setCancelable(false)

            val dialog = dialogBuilder.create()

            rateButton.setOnClickListener {
                val rating = ratingBar.rating
                if (rating > 3) {
                    moodImageView.setImageResource(R.drawable.happyy)
                } else {
                    moodImageView.setImageResource(R.drawable.badmood)
                }
                if (rating >= 4) {
                    launchPlayStore(applicationContext)
                } else {
                    // Show feedback form or any other action for lower ratings
                }
                dialog.dismiss()
            }

            cancelButton.setOnClickListener {
                moodImageView.setImageResource(R.drawable.badmood)
                dialog.dismiss()
            }

            dialog.show()

            binding.drawalLayout.closeDrawer(GravityCompat.START)

        }




        binding.category.setOnClickListener {

            var m = Intent(this, CategoriesActivity::class.java)
            startActivity(m)
            binding.drawalLayout.closeDrawer(GravityCompat.START)

        }




        binding.exportreport.setOnClickListener {
            binding.drawalLayout.closeDrawer(GravityCompat.START)







        }





        binding.settings.setOnClickListener {
            binding.drawalLayout.closeDrawer(GravityCompat.START)


        }



        binding.adformyapp.setOnClickListener {
            binding.drawalLayout.closeDrawer(GravityCompat.START)
            val appPlayStoreLink =
                "https://play.google.com/store/apps/details?id=com.poorvish.advancebmicalculater"

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Check out app: $appPlayStoreLink")
                type = "text/plain" }
            startActivity(sendIntent)
        }



//        binding.fingerlock.setOnClickListener {
//            binding.drawalLayout.closeDrawer(GravityCompat.START)
//
//        }

        binding.home.setOnClickListener {
            binding.drawalLayout.closeDrawer(GravityCompat.START)
            StyleableToast.makeText(this, "Your Already Home!", Toast.LENGTH_LONG, R.style.mytoast).show();
        }



        binding.share.setOnClickListener {

            binding.drawalLayout.closeDrawer(GravityCompat.START)

            val packageManager: PackageManager = packageManager

            val appPlayStoreLink =
                "https://play.google.com/store/apps/details?id=com.musict.budgetexpensemanagerhelp"

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Check out app: $appPlayStoreLink")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)

            // Verify if the app is installed on the device
            if (isPackageInstalled(packageName, packageManager)) {
                shareIntent.`package` = packageName
            }

            startActivity(shareIntent)
        }




        binding.menuopen.setOnClickListener {

            binding.drawalLayout.openDrawer(GravityCompat.START)
        }

    }


}

private fun launchPlayStore(context: Context) {
    val packageName = "com.musict.budgetexpensemanagerhelp"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        intent.data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        context.startActivity(intent)
    }
}


private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
