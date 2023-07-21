package com.musict.budgetexpensemanagerhelp.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.SurfaceControl
import android.widget.TextView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.musict.budgetexpensemanagerhelp.modelclass.Transaction
import com.musict.budgetexpensemanagerhelp.modelclass.modelclass
import com.musict.budgetexpensemanagerhelp.modelclass.tieddata
import java.text.NumberFormat
import java.util.Random
import java.util.*

class SqlLiteDataHelper(context: Context) : SQLiteOpenHelper(context, "categoriesDb", null, 1) {

    private var list = ArrayList<modelclass>()
    private var datastore = ArrayList<tieddata>()




    override fun onCreate(db: SQLiteDatabase?) {

        val sql =
            "create table categoriesTb(categories_id Integer Primary key autoincrement,categories text)"
        db?.execSQL(sql)

        val sqll =
            "create table StorageTb(storage_id Integer Primary key autoincrement,type integer,amount integer,category text,date text,time text,mode text ,note text)"
        db?.execSQL(sqll)


    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//
//
//        db?.execSQL("DROP TABLE IF EXISTS" +datastore)
//        onCreate(db)

    }


    fun categoriesinsert(categoriesN: String) {

        val db = writableDatabase

        val c = ContentValues()

        c.put("categories", categoriesN)

        db.insert("categoriesTb", null, c)

    }

    fun clearCategories() {
        val db = this.writableDatabase
        db.delete("categoriesTb", null, null)
        db.close()
    }





    @SuppressLint("Recycle")
    fun displayCategory(): ArrayList<modelclass> {

        list.clear()


        val db = readableDatabase



        val sql = "select * from categoriesTb"

        val cursor = db.rawQuery(sql, null)


        if (cursor.count > 0) {

            cursor.moveToFirst()

            do {

                val categoryName = cursor.getString(1)

                val model = modelclass(categoryName)
                list.add(model)

            } while (cursor.moveToNext())
        }
        else {

            Log.e("TAG", "displayCategory: " + "no data found")
        }

        return list


    }
    private fun fetchTransactionData(fromDate: String, toDate: String): List<Transaction> {
        val db = readableDatabase
        val sql = "SELECT * FROM StorageTb WHERE date BETWEEN ? AND ?"
        val cursor = db.rawQuery(sql, arrayOf(fromDate, toDate))
        val transactionList = mutableListOf<Transaction>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val type = cursor.getInt(1)
                val amount = cursor.getString(2)
                val category = cursor.getString(3)
                val date = cursor.getString(4)
                val time = cursor.getString(5)
                val mode = cursor.getString(6)
                val note = cursor.getString(7)

                val transaction = Transaction(id, type, amount, category, date, time, mode, note)
                transactionList.add(transaction)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return transactionList
    }



    fun insertIncomeExense(

        type: Int,
        amount: String,
        category: String,
        date: String,
        time: String,
        mode: String,
        note: String

    ) {

//        datastore.clear()

        val inexp = writableDatabase
        val add = ContentValues()

        add.put("type", type)
        add.put("amount", amount)
        add.put("category", category)
        add.put("date", date)
        add.put("time", time)
        add.put("mode", mode)
        add.put("note", note)

        inexp.insert("StorageTb", null, add)


    }


    // Assuming you have a database helper class named `DatabaseHelper` with a readable SQLiteDatabase instance.

    // Function to get all income entries from the database

    @SuppressLint("Recycle")
        fun displayIncomeExpense(): ArrayList<tieddata> {

            datastore.clear()


            val inexp = writableDatabase
            val sql2 = "select * from StorageTb"
            val cursor = inexp.rawQuery(sql2, null)
            if (cursor.count > 0) {
                cursor.moveToFirst()

                do {

                   val id = cursor.getInt(0)
                   val type = cursor.getInt(1)
                   val amount = cursor.getString(2)
                   val category = cursor.getString(3)
                   val date = cursor.getString(4)
                   val time = cursor.getString(5)
                   val mode = cursor.getString(6)
                   val note = cursor.getString(7)


                    val modetwo = tieddata(id,type, amount, category, date, time, mode, note)

                    datastore.add(modetwo)

                } while (cursor.moveToNext())
            }


            return datastore
        }



    fun updateUserData( amount: String ,note: String,id: Int ) {


        val db = writableDatabase
        val sqli = "update StorageTb set amount='$amount',note='$note' where storage_id='$id'"
        db.execSQL(sqli)

    }

 fun deletData (id: Int)
    {

        val db = writableDatabase

        val delet = "delete from StorageTb where storage_id  = '$id'"
        db.execSQL(delet)

    }



    fun  getAllIncomeExpenses(): ArrayList<tieddata> {
        datastore.clear()

        val db = readableDatabase
        val sql = "SELECT * FROM StorageTb"
        val cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val type = cursor.getInt(1)
                val amount = cursor.getString(2)
                val category = cursor.getString(3)
                val date = cursor.getString(4)
                val time = cursor.getString(5)
                val mode = cursor.getString(6)
                val note = cursor.getString(7)

                val data = tieddata(id, type, amount, category, date, time, mode, note)
                datastore.add(data)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return datastore
    }





    fun generateIncomeReport(pieChart: PieChart, totalIncomeTextView: TextView) {
        // Retrieve income data from SQLite database
        val incomeDat = getAllIncomeExpenses().filter { it.type == 1 }

        // Calculate total income amount
        var totalIncome = 0.0
        incomeDat.forEach { entry ->
            totalIncome += entry.amount.toDouble()
        }

        // Create pie chart entries for income
        val incomeEntries = ArrayList<PieEntry>()
        incomeDat.forEach { entry ->
            val pieEntry = PieEntry(entry.amount.toFloat(), entry.mode)
            incomeEntries.add(pieEntry)
        }

        // Create pie chart dataset for income
        val incomeDataSet = PieDataSet(incomeEntries, "")
        incomeDataSet.colors = getChartColors(incomeEntries.size)

        // Create pie chart data for income
        val incomeData = PieData(incomeDataSet)
        incomeData.setValueTextSize(12f)
        incomeData.setValueTextColor(Color.BLACK)

        // Set pie chart properties for income
        pieChart.data = incomeData
        pieChart.description.isEnabled = false
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true
        pieChart.legend.isEnabled = true
        pieChart.legend.textSize = 14f
        pieChart.animateY(1000)

        // Set total income text without any currency symbol
        totalIncomeTextView.text = "Total Income: ${totalIncome}"
    }



    fun generateExpenseReport(pieChart: PieChart, totalExpenseTextView: TextView) {
        // Retrieve expense data from SQLite database
        val expenseDat = getAllIncomeExpenses().filter { it.type == 2 }

        // Calculate total expense amount
        var totalExpense = 0.0
        expenseDat.forEach { entry ->
            totalExpense += entry.amount.toDouble()
        }

        // Create pie chart entries for expenses
        val expenseEntries = ArrayList<PieEntry>()
        expenseDat.forEach { entry ->
            val pieEntry = PieEntry(entry.amount.toFloat(), entry.mode)
            expenseEntries.add(pieEntry)
        }

        // Create pie chart dataset for expenses
        val expenseDataSet = PieDataSet(expenseEntries, "")
        expenseDataSet.colors = getChartColors(expenseEntries.size)

        // Create pie chart data for expenses
        val expenseData = PieData(expenseDataSet)
        expenseData.setValueTextSize(12f)
        expenseData.setValueTextColor(Color.BLACK)

        // Set pie chart properties for expenses
        pieChart.data = expenseData
        pieChart.description.isEnabled = false
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true
        pieChart.legend.isEnabled = true
        pieChart.legend.textSize = 14f
        pieChart.animateY(1000)


        totalExpenseTextView.text = "Total Expense: ${totalExpense}"
    }



}


private fun getChartColors(size: Int): ArrayList<Int> {
    val colors = ArrayList<Int>()
    val random = Random()
    for (i in 0 until size) {
        val color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
        colors.add(color)
    }
    return colors
}
