package com.musict.budgetexpensemanagerhelp

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


            Log.e("TAG", "putextra: "+it.amount+""+it.note+""+it.date+""+it.time+""+it.mode+""+it.category)


        }, {id ->
             val dialogmode = Dialog(this)



             val dialogBinding = TransactionDeleteDialogBinding.inflate(layoutInflater)
             dialogmode.setContentView(dialogBinding.root)


             dialogmode.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
             dialogmode.window?.setLayout(

                 LinearLayout.LayoutParams.MATCH_PARENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT


             )


//             val btncancel: AppCompatButton = dialogmode.findViewById(R.id.btncan)
//             val btndelet: AppCompatButton = dialogmode.findViewById(R.id.btndelet)



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






    }

    }