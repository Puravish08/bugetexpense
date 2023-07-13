package com.musict.budgetexpensemanagerhelp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.musict.budgetexpensemanagerhelp.adapter.TransactionAdapter
import com.musict.budgetexpensemanagerhelp.databinding.ActivityTransactionBinding
import com.musict.budgetexpensemanagerhelp.databinding.CustomModeRecycleDialogBinding
import com.musict.budgetexpensemanagerhelp.databinding.TransactionDeleteDialogBinding
import com.musict.budgetexpensemanagerhelp.modelclass.tieddata
import com.musict.budgetexpensemanagerhelp.sqlite.SqlLiteDataHelper


import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar



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
        val incomeList = db.displayIncomeExpense().filter { it.type == 1 } // Filter income records from the list
        val expenseList = db.displayIncomeExpense().filter { it.type == 2 } // Filter expense records from the list

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


        datastorage  = db.displayIncomeExpense()
        var title = "update details"
        var donebtn = "update"
        db.displayIncomeExpense()


         adapter = TransactionAdapter(incomeExpense,{
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

        }, {id ->
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
                 Log.e("TAG", "delete recode"+id )


                 dialogmode.dismiss()

             }

             dialogBinding.btncan.setOnClickListener {
                 Toast.makeText(this, "Cancel Successfully", Toast.LENGTH_SHORT).show()
                 dialogmode.dismiss()
             }
             dialogmode.show()

         })








        var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        tbinding.rcvtransaction.adapter = adapter
        tbinding.rcvtransaction.layoutManager = manager


        datastorage = db.displayIncomeExpense()
        adapter.updateData(datastorage)



        tbinding.imgback.setOnClickListener {

            val transaction = Intent(this@TransactionActivity, MainActivity::class.java)
            startActivity(transaction)

        }

        tbinding.imgdone.setOnClickListener {

            val transaction = Intent(this@TransactionActivity, MainActivity::class.java)

            startActivity(transaction)
        }


        val window: Window = window
        val actionBar: Toolbar? = findViewById(R.id.tools)
        val toolbar2: Toolbar? = findViewById(R.id.tools2)
        val toolbar3: Toolbar? = findViewById(R.id.tools3)
        val toolbar4: Toolbar? = findViewById(R.id.tools4)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            val randomColor = Color.rgb(
                (Math.random() * 256).toInt(),
                (Math.random() * 256).toInt(),
                (Math.random() * 256).toInt()
            )
            window.statusBarColor = randomColor
            actionBar?.setBackgroundColor(randomColor)
            toolbar2?.setBackgroundColor(randomColor)
            toolbar3?.setBackgroundColor(randomColor)
            toolbar4?.setBackgroundColor(randomColor)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val decorView = window.decorView as ViewGroup
            val statusBarView = View(applicationContext)
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()
            )
            val randomColor = Color.rgb(
                (Math.random() * 256).toInt(),
                (Math.random() * 256).toInt(),
                (Math.random() * 256).toInt()
            )
            statusBarView.setBackgroundColor(randomColor)
            decorView.addView(statusBarView, params)
            actionBar?.background = ColorDrawable(randomColor)
            toolbar2?.background = ColorDrawable(randomColor)
            toolbar3?.background = ColorDrawable(randomColor)
            toolbar4?.background = ColorDrawable(randomColor)
        }
    }


    // Helper method to get status bar height
    @SuppressLint("InternalInsetResource")
    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier(
            "status_bar_height", "dimen", "android"
        )
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }




    }

