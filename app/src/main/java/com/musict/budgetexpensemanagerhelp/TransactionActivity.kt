package com.musict.budgetexpensemanagerhelp

import com.musict.budgetexpensemanagerhelp.ExposedReports

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.ViewDebug.ExportedProperty
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.musict.budgetexpensemanagerhelp.adapter.TransactionAdapter
import com.musict.budgetexpensemanagerhelp.databinding.ActivityTransactionBinding
import com.musict.budgetexpensemanagerhelp.databinding.TransactionDeleteDialogBinding
import com.musict.budgetexpensemanagerhelp.modelclass.tieddata
import com.musict.budgetexpensemanagerhelp.sqlite.SqlLiteDataHelper
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


class TransactionActivity : AppCompatActivity() {

    lateinit var tbinding: ActivityTransactionBinding
    lateinit var db: SqlLiteDataHelper
    var datastorage = ArrayList<tieddata>()
    var incomeExpense = ArrayList<tieddata>()
    lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tbinding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(tbinding.root)

        db = SqlLiteDataHelper(this)


        ie()
        updateTextViews()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTextViews() {
        val incomeList =
            db.displayIncomeExpense().filter { it.type == 1 } // Filter income records from the list
        val expenseList = db.displayIncomeExpense()
            .filter { it.type == 2 } // Filter expense records from the list

        val totalIncome = incomeList.sumBy { it.amount.toInt() }
        val totalExpense = expenseList.sumBy { it.amount.toInt() }
        val balance = totalIncome + totalExpense

        tbinding.income.text = "Rs $totalIncome"
        tbinding.expense.text = "Rs $totalExpense"
        tbinding.totalBalance.text = "Rs $balance"
    }


    private fun ie() {


        incomeExpense = db.displayIncomeExpense()
        db.displayIncomeExpense()


        datastorage = db.displayIncomeExpense()
        var title = "update details"
        var donebtn = "update"
        db.displayIncomeExpense()


        adapter = TransactionAdapter(incomeExpense, {
            val tansaction = Intent(this@TransactionActivity, IncomeActivity::class.java)

            tansaction.putExtra("id", it.id)
            tansaction.putExtra("type", it.type)
            tansaction.putExtra("amount", it.amount)
            tansaction.putExtra("category", it.category)
            tansaction.putExtra("data", it.date)
            tansaction.putExtra("time", it.time)
            tansaction.putExtra("mode", it.mode)
            tansaction.putExtra("note", it.note)
            tansaction.putExtra("title", title)
            tansaction.putExtra("icone", donebtn)
            tansaction.putExtra("updateRecord", true)

            startActivity(tansaction)

        }, { id ->
            val dialogmode = Dialog(this)


            val dialogBinding = TransactionDeleteDialogBinding.inflate(layoutInflater)
            dialogmode.setContentView(dialogBinding.root)


            dialogmode.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogmode.window?.setLayout(

                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT


            )


            dialogBinding.btndelet.setOnClickListener {

                Toast.makeText(this, "Delete Successfully", Toast.LENGTH_SHORT).show()



                db.deletData(id)
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                datastorage = db.displayIncomeExpense()
                adapter.updateData(datastorage)
                Log.e("TAG", "delete recode" + id)


                dialogmode.dismiss()

            }

            dialogBinding.btncan.setOnClickListener {
                Toast.makeText(this, "Cancel Successfully", Toast.LENGTH_SHORT).show()
                dialogmode.dismiss()
            }
            dialogmode.show()

        })



//            val transactionData = db.displayIncomeExpense()
//
//            // Create an intent to start the new activity
//            val intent = Intent(this, ExposedReports::class.java)
//            intent.putParcelableArrayListExtra("transactionData", transactionData)
//            startActivity(intent)







        var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        tbinding.rcvtransaction.adapter = adapter
        tbinding.rcvtransaction.layoutManager = manager


        datastorage = db.displayIncomeExpense()
        adapter.updateData(datastorage)



        tbinding.imgback.setOnClickListener {

            onBackPressed()

        }

        tbinding.imgdone.setOnClickListener {

            val transaction = Intent(this@TransactionActivity, MainActivity::class.java)

            startActivity(transaction)
        }


        tbinding.imgok.setOnClickListener {
            val fromDate = "2023-07-01" // Replace with your actual from date
            val toDate = "2023-07-31" // Replace with your actual to date
            CoroutineScope(Dispatchers.Main).launch {
                generatePDFReport(fromDate, toDate)
            }
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
        val intent = Intent(this@TransactionActivity, ExposedReports::class.java)
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








//        val window: Window = window
//        val actionBar: Toolbar? = findViewById(R.id.tools)
//        val toolbar2: Toolbar? = findViewById(R.id.tools2)
//        val toolbar3: Toolbar? = findViewById(R.id.tools3)
//        val toolbar4: Toolbar? = findViewById(R.id.tools4)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            val randomColor = Color.rgb(
//                (Math.random() * 256).toInt(),
//                (Math.random() * 256).toInt(),
//                (Math.random() * 256).toInt()
//            )
//            window.statusBarColor = randomColor
//            actionBar?.setBackgroundColor(randomColor)
//            toolbar2?.setBackgroundColor(randomColor)
//            toolbar3?.setBackgroundColor(randomColor)
//            toolbar4?.setBackgroundColor(randomColor)
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            val decorView = window.decorView as ViewGroup
//            val statusBarView = View(applicationContext)
//            val params = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()
//            )
//            val randomColor = Color.rgb(
//                (Math.random() * 256).toInt(),
//                (Math.random() * 256).toInt(),
//                (Math.random() * 256).toInt()
//            )
//            statusBarView.setBackgroundColor(randomColor)
//            decorView.addView(statusBarView, params)
//            actionBar?.background = ColorDrawable(randomColor)
//            toolbar2?.background = ColorDrawable(randomColor)
//            toolbar3?.background = ColorDrawable(randomColor)
//            toolbar4?.background = ColorDrawable(randomColor)
//        }
//    }
//
//
//    // Helper method to get status bar height
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
//