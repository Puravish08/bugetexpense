package com.musict.budgetexpensemanagerhelp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.musict.budgetexpensemanagerhelp.adapter.TransactionAdapter
import com.musict.budgetexpensemanagerhelp.databinding.ActivityTransactionBinding
import com.musict.budgetexpensemanagerhelp.databinding.TransactionDeleteDialogBinding
import com.musict.budgetexpensemanagerhelp.modelclass.Transaction
import com.musict.budgetexpensemanagerhelp.modelclass.tieddata
import com.musict.budgetexpensemanagerhelp.sqlite.SqlLiteDataHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class TransactionActivity : AppCompatActivity() {

    lateinit var tbinding: ActivityTransactionBinding
    lateinit var db: SqlLiteDataHelper
    var datastorage = ArrayList<tieddata>()
    var fetchTransactionData = ArrayList<Transaction>()
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
                val transactionList = fetchTransactionData(fromDate, toDate)

                // Start the Export Report Activity and pass the data
                val intent = Intent(this@TransactionActivity, ExposedReports::class.java)
                intent.putExtra("FROM_DATE", fromDate)
                intent.putExtra("TO_DATE", toDate)
                intent.putExtra("TRANSACTION_LIST", ArrayList(transactionList))
                startActivity(intent)
            }
        }



    }


    @SuppressLint("Range")
    private fun fetchTransactionData(fromDate: String, toDate: String): List<Transaction> {
        val transactionList = mutableListOf<Transaction>()

        // Get a readable database instance using the dbHandler
        val db = db.readableDatabase

        // Define the table and column names
        val tableName = "StorageTb"
        val columns = arrayOf("`date`", "type", "amount", "category", "time", "mode", "note")


        // Define the selection criteria (WHERE clause) to fetch data within the specified date range
        val selection = "date BETWEEN ? AND ?"
        val selectionArgs = arrayOf(fromDate, toDate)

        // Execute the query and retrieve the data using a Cursor
        val cursor = db.query(tableName, columns, selection, selectionArgs, null, null, null)

        // Parse the data from the Cursor and add it to the transactionList
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

                val transaction = Transaction(id, type, amount, category, date, time, mode, note)
                transactionList.add(transaction)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return transactionList
    }



    private suspend fun generatePDFReport(fromDate: String, toDate: String, transactionList: List<Transaction>) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        val title = "Transaction Report ($fromDate to $toDate)"
        val x = 40 // X-coordinate for the title
        val y = 50 // Y-coordinate for the title

        paint.textSize = 20f
        paint.color = Color.BLACK
        canvas.drawText(title, x.toFloat(), y.toFloat(), paint)

        // Assuming you have a list of transactions in the transactionList variable
        val startX = 40 // X-coordinate for the starting position of the content
        var startY = y + 30 // Y-coordinate for the starting position of the content
        val lineHeight = 30 // Line height for each entry

        paint.textSize = 14f
        for (transaction in transactionList) {
            val entry = "${transaction.date} - ${transaction.amount} - ${transaction.category}"
            canvas.drawText(entry, startX.toFloat(), startY.toFloat(), paint)
            startY += lineHeight
        }

        pdfDocument.finishPage(page)

        // Output file path for the PDF report (replace this with your desired file path)
        val outputFilePath = "${getExternalFilesDir(null)}/transaction_report.pdf"
        val outputFile = File(outputFilePath)

        try {
            FileOutputStream(outputFile).use { outputStream ->
                pdfDocument.writeTo(outputStream)
            }

            showToast("PDF report generated successfully.")
        } catch (e: IOException) {
            showToast("Error generating PDF report: ${e.message}")
        }

        pdfDocument.close()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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