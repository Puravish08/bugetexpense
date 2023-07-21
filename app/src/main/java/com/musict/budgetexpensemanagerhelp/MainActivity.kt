package com.musict.budgetexpensemanagerhelp



import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.musict.budgetexpensemanagerhelp.databinding.ActivityMainBinding
import com.musict.budgetexpensemanagerhelp.modelclass.tieddata
import com.musict.budgetexpensemanagerhelp.sqlite.SqlLiteDataHelper
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {


    private lateinit var pieChart: PieChart
    lateinit var binding: ActivityMainBinding
    lateinit var db: SqlLiteDataHelper
    private lateinit var fingerprintPrompt: BiometricPrompt

    private lateinit var sharedPreferences: SharedPreferences
    private val packageName = "com.example.myapp"





    private companion object{
        private const val TAG = "BANNER_AD_TAG"
    }



    private lateinit var madview :AdView




    private lateinit var dbHelper: SqlLiteDataHelper



    private var mInterstitialAd:InterstitialAd?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//
//        MobileAds.initialize(this){
//
//
//        }

//        loadAd()
//
//        binding.txttransactionad.setOnClickListener{
//
//            loadAd()
//
//            if (mInterstitialAd!=null){
//                mInterstitialAd!!.show(this)
//            }
//
//            else {
//                Toast.makeText(this, "The ad did not load", Toast.LENGTH_SHORT).show()
//            }
//
//        }


        MobileAds.initialize(this) {

            Log.d(TAG, "onIntializeCompled: ")

        }


        madview = binding.adView

        val adRequest = AdRequest.Builder().build()
        madview.loadAd(adRequest)



        madview.adListener = object : AdListener(){
            override fun onAdClicked() {
                super.onAdClicked()
                Log.d(TAG, "onAdClicked: ")
            }

            override fun onAdClosed() {
                super.onAdClosed()
                Log.d(TAG ,"onAdClosed: ")
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.d(TAG, "onAdFailedToLoad: {${p0.message}")
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.d(TAG, "onAdImpression: ")
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.d(TAG, "onAdLoaded: ")
            }

            override fun onAdOpened() {
                super.onAdOpened()
                Log.d(TAG, "onAdOpened: ")
            }

            override fun onAdSwipeGestureClicked() {
                super.onAdSwipeGestureClicked()
            Log.d(TAG,"onAdSwipeGestureClick")
            }


        }












        db = SqlLiteDataHelper(this)
        initview()

        pieChart = findViewById(R.id.pieChart)
        dbHelper = SqlLiteDataHelper(this)
        displayPieChart()



        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        // Retrieve the saved switch state and update the switch
        val savedSwitchState = sharedPreferences.getBoolean("fingerprintSwitchState", false)
        binding.fingerprintlock.isChecked = savedSwitchState

        // Set a listener to the Switch
        binding.fingerprintlock.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showAppropriateScreen()
            } else {
                // Disable fingerprint lock or perform any desired action
            }
        }

        showAppropriateScreen()

    }









//    private fun  loadAd(){
//
//        val adUnitId ="ca-app-pub-5017607716747138/6381300547"
//        val adRequest = AdRequest.Builder().build()
//
//        InterstitialAd.load(this,adUnitId,adRequest,object : InterstitialAdLoadCallback(){
//
//            override fun onAdLoaded(p0: InterstitialAd) {
//
//                mInterstitialAd=p0
//            }
//
//
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//
//                mInterstitialAd=null
//            }
//
//        })
//
//    }

    private fun showAppropriateScreen() {
        if (binding.fingerprintlock.isChecked && FingerprintUtils.isFingerprintSupported(this) && !FingerprintUtils.isPermissionGranted(this)) {
            FingerprintUtils.requestFingerprintPermission(this)
        } else if (binding.fingerprintlock.isChecked) {
            authenticateWithFingerprint()
        }
    }

    private fun authenticateWithFingerprint() {
        val executor = ContextCompat.getMainExecutor(this)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                openApp()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    exitApp()
                }
            }
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Fingerprint Authentication")
            .setDescription("Scan your fingerprint to unlock the app.")
            .setNegativeButtonText("Cancel")
            .build()

        fingerprintPrompt = BiometricPrompt(this, executor, callback)
        fingerprintPrompt.authenticate(promptInfo)
    }

    private fun openApp() {
        // Proceed to your app's main screen/activity.
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
    }

    private fun exitApp() {
        finish()
    }

    override fun onPause() {
        super.onPause()
        // Save the state of the switch to SharedPreferences
        sharedPreferences.edit().putBoolean("fingerprintSwitchState", binding.fingerprintlock.isChecked).apply()
    }




    private fun displayPieChart() {
        val data = dbHelper.getAllIncomeExpenses()

        val categoryAmountMap = HashMap<String, Float>()
        var totalIncome = 0f
        var totalExpense = 0f

        for (item in data) {
            val category = item.mode
            val amount = item.amount.toFloat()

            // Remove the category check and directly accumulate amounts
            val sum = categoryAmountMap[category] ?: 0f
            categoryAmountMap[category] = sum + amount

            if (category == "income") {
                totalIncome += amount
            } else if (category == "expense") {
                totalExpense += amount
            }
        }

        val entries = ArrayList<PieEntry>()

        for (categoryAmountEntry in categoryAmountMap.entries) {
            val amount = categoryAmountEntry.value

            entries.add(PieEntry(amount))
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



//            MobileAds.initialize(this){
//
//
//            }
//
//
//                loadAd()
//
//                if (mInterstitialAd!=null){
//                    mInterstitialAd!!.show(this)
//                }
//
//                else {
//                    Toast.makeText(this, "The ad did not load", Toast.LENGTH_SHORT).show()
//                }

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

        binding.subcription.setOnClickListener {


            var r = Intent(this, SubscriptionActivity::class.java)
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

            val fromDate = "2023-07-01" // Replace with your actual from date
            val toDate = "2023-07-31" // Replace with your actual to date
            CoroutineScope(Dispatchers.Main).launch {
                generatePDFReport(fromDate, toDate)
            }
        }





        binding.settings.setOnClickListener {
            binding.drawalLayout.closeDrawer(GravityCompat.START)
            var se = Intent(this, SettingActivity::class.java)
            startActivity(se)
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

    private suspend fun generatePDFReport(fromDate: String, toDate: String) {
        // Retrieve transaction data from SQLite database
        val dataList = displayIncomeExpense(fromDate, toDate)

        // Create the PDF document
        val pdfDocument = PdfDocument()

        // Create a page with the desired attributes
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()

        // Start a page
        val page = pdfDocument.startPage(pageInfo)

        // Get the canvas for drawing
        val canvas = page.canvas

        // Set up paint for drawing text
        val paint = Paint()
        paint.textSize = 12f
        paint.color = Color.BLACK

        // Draw the transaction data on the canvas
        var y = 50f
        for (data in dataList) {
            val text = "${data.type} ${data.amount} ${data.category} ${data.date} ${data.time} ${data.mode} ${data.note}"
            canvas.drawText(text, 50f, y, paint)
            y += 20f
        }

        // Finish the page
        pdfDocument.finishPage(page)

        // Save the PDF document to a file
        val outputFile = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "report.pdf")
        pdfDocument.writeTo(FileOutputStream(outputFile))

        // Close the PDF document
        pdfDocument.close()

        // Open the ExportReportActivity and pass the PDF file path as an extra
        val intent = Intent(this@MainActivity, ExposedReports::class.java)
        intent.putExtra("pdfFilePath", outputFile.absolutePath)
        startActivity(intent)
    }


    @SuppressLint("Range")
    private fun displayIncomeExpense(fromDate: String, toDate: String): List<tieddata.TransactionData> {
        val dataList = mutableListOf<tieddata.TransactionData>()

        val dbHelper = SqlLiteDataHelper(this) // Replace with your actual SQLite database helper class
        val db = dbHelper.readableDatabase

        val query = "SELECT * FROM StorageTb WHERE date BETWEEN '$fromDate' AND '$toDate'"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val type = cursor.getInt(cursor.getColumnIndex("type"))
                val amount = cursor.getString(cursor.getColumnIndex("amount"))
                val category = cursor.getString(cursor.getColumnIndex("category"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val time = cursor.getString(cursor.getColumnIndex("time"))
                val mode = cursor.getString(cursor.getColumnIndex("mode"))
                val note = cursor.getString(cursor.getColumnIndex("note"))

                val transactionData = tieddata.TransactionData(id, type, amount, category, date, time, mode, note)
                dataList.add(transactionData)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return dataList
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

