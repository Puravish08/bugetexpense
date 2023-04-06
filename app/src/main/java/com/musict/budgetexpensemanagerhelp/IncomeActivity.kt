package com.musict.budgetexpensemanagerhelp

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.musict.budgetexpensemanagerhelp.adapter.CatogoryAdapter
import com.musict.budgetexpensemanagerhelp.adapter.ModeAdapter
import com.musict.budgetexpensemanagerhelp.databinding.ActivityCustomRecycleDialogBinding
import com.musict.budgetexpensemanagerhelp.databinding.ActivityIncomeBinding
import com.musict.budgetexpensemanagerhelp.databinding.CustomModeRecycleDialogBinding
import com.musict.budgetexpensemanagerhelp.modelclass.modelclass
import com.musict.budgetexpensemanagerhelp.sqlite.ModeSqlLite
import com.musict.budgetexpensemanagerhelp.sqlite.SqlLiteDataHelper
import java.text.SimpleDateFormat
import java.util.*


class IncomeActivity : AppCompatActivity() {

    lateinit var ibinding: ActivityIncomeBinding

    lateinit var db1: SqlLiteDataHelper

    lateinit var edtamount: EditText
    lateinit var edtnote: EditText
    lateinit var txttitleincome: TextView

    lateinit var db2: ModeSqlLite
    var type = -1


    var datevalue = ""
    var mode = " "
    var time = ""
    var category = ""
    var storage_id = 0

    var flag = 0

    var addList = ArrayList<modelclass>()
    lateinit var tvdate: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ibinding = ActivityIncomeBinding.inflate(layoutInflater)
        setContentView(ibinding.root)



        db1 = SqlLiteDataHelper(this)

        db2 = ModeSqlLite(this)



        initview2()
        datatime()

    }


    private fun initview2() {


        tvdate = findViewById(R.id.tvDate)
        edtamount = findViewById(R.id.edtamount)
        edtnote = findViewById(R.id.edtnote)

        txttitleincome = findViewById(R.id.txtTitle)





        if (intent != null && intent.hasExtra("updateRecord")) {


            flag = 1
            var newamt: String? = intent.getStringExtra("amount")
            var notenew: String? = intent.getStringExtra("note")
            var newtitle: String? = intent.getStringExtra("title")
            var needonebtn: String? = intent.getStringExtra("icone")
            storage_id = intent.getIntExtra("id", 0)


            edtamount.setText(newamt)
            edtnote.setText(notenew)
            txttitleincome.setText(newtitle)
            ibinding.imgok.setText(needonebtn)


        }


        val textView: TextView = findViewById(R.id.tvTime)
        val simpleDateFormat = SimpleDateFormat(" hh:mm a")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        textView.text = currentDateAndTime

        time = currentDateAndTime

        val textv: TextView = findViewById(R.id.tvDate)
        val simpleTimeFormat = SimpleDateFormat("  dd/MM/yyyy")
        val currentTime: String = simpleTimeFormat.format(Date())
        textv.text = currentTime

        datevalue = currentTime

//
//        val textv: TextView = findViewById(R.id.tvDate)
//        val simpleTimeFormat = SimpleDateFormat("  dd/MM/yyyy")
//        val currentTime: String = simpleTimeFormat.format(Date())
//        textv.text = currentTime
//
//


        ibinding.imgback.setOnClickListener {


            var b = Intent(this, MainActivity::class.java)
            startActivity(b)
        }



        ibinding.imgok.setOnClickListener {

//
//            var o = Intent(this, TransactionActivity::class.java)
//
//            startActivity(o)
//

            var note = ibinding.edtnote.text.toString()

            var amount = ibinding.edtamount.text.toString()





            if (amount.isEmpty()) {
                Toast.makeText(this, "Enter Please Amount", Toast.LENGTH_SHORT).show()
            } else if (amount.length <= 1 || amount.length >= 10) {

                Toast.makeText(this, "Enter Your Amount", Toast.LENGTH_SHORT).show()
                Log.e("TAG", "amount: " + amount)


            } else {


                if (ibinding.rg.checkedRadioButtonId == -1) {

                } else {
                    var selectid: Int = ibinding.rg.checkedRadioButtonId
                    var selecteRadioButton: RadioButton = findViewById(selectid)
                    var text = selecteRadioButton.text.toString()


                    if (text.equals("INCOME")) {
                        type = 1

                    } else {
                        type = 2
                    }

                    Log.e("TAG", "working: " + type)
                }

            }

            if (flag == 1) {
                db1.updateUserData(amount, note, storage_id)

            } else {


                db1.insertIncomeExense(

                    type,
                    amount,
                    note,
                    time,
                    datevalue,
                    category,
                    mode


                )
            }

//                Toast.makeText(this, "Click Your Note", Toast.LENGTH_SHORT).show()

            var intent = Intent(this, TransactionActivity::class.java)
            startActivity(intent)
        }




        ibinding.imgCat.setOnClickListener {


            val dialog = Dialog(this)


            val dialogBinding = ActivityCustomRecycleDialogBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)


            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setLayout(

                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT


            )

            val btncancel: AppCompatButton = dialog.findViewById(R.id.btncancel)
            val btnset: AppCompatButton = dialog.findViewById(R.id.btnset)



            btnset.setOnClickListener {

                Toast.makeText(this, "click on yes", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            btncancel.setOnClickListener {
                Toast.makeText(this, "click on Cancel", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }


            val listofTypes = db1.displayCategory();


            val adapter = CatogoryAdapter(listofTypes, { category ->

                Log.e("TAG", "working: " + category)
                this.category = category

            })


            val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            dialogBinding.rcvCategory.layoutManager = manager
            dialogBinding.rcvCategory.adapter = adapter



            dialog.show()


        }


        ibinding.imgMode.setOnClickListener {


            val dialogmode = Dialog(this)


            val dialogBinding = CustomModeRecycleDialogBinding.inflate(layoutInflater)
            dialogmode.setContentView(dialogBinding.root)


            dialogmode.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogmode.window?.setLayout(

                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT


            )


            val btncancelc: AppCompatButton = dialogmode.findViewById(R.id.btncancelc)
            val btnsett: AppCompatButton = dialogmode.findViewById(R.id.btnsett)



            btnsett.setOnClickListener {

                Toast.makeText(this, "click on yes", Toast.LENGTH_SHORT).show()
                dialogmode.dismiss()

            }

            btncancelc.setOnClickListener {
                Toast.makeText(this, "click on Cancel", Toast.LENGTH_SHORT).show()
                dialogmode.dismiss()
            }
            dialogmode.show()


            val modeofTypes = db2.displaymode();


            val adapter = ModeAdapter(modeofTypes, { Modee ->

                mode = Modee
                Log.e("TAG", "working: " + Modee)

            })
            val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            dialogBinding.rcvmode.layoutManager = manager
            dialogBinding.rcvmode.adapter = adapter


        }


        val Page = intent.getStringExtra("Page")
        val name: String? = intent.getStringExtra("title")


        when (Page) {


            "income" ->
                ibinding.rb1.isChecked = true


            "expense" ->
                ibinding.rb2.isChecked = true


        }

        ibinding.txtTitle.text = name


    }


    private fun datatime() {


        val myCalendar = Calendar.getInstance()

        val datapicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofmonth ->

            myCalendar.set(Calendar.DAY_OF_MONTH, dayofmonth)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.YEAR, year)
            updateLabal(myCalendar)

        }
        tvdate.setOnClickListener {

            DatePickerDialog(
                this, datapicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
//
//
//        ibinding.tvTime.setOnClickListener {
//            val mcurrentTime = Calendar.getInstance()
//            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
//            val minute = mcurrentTime[Calendar.MINUTE]
//            val mTimePicker: TimePickerDialog
//            mTimePicker = TimePickerDialog(this@IncomeActivity,
//                { timePicker, selectedHour, selectedMinute -> time.setText("$selectedHour:$selectedMinute") }, hour, minute,
//                false
//            ) //Yes 24 hour time
//            mTimePicker.setTitle("Update Your Time")
//            mTimePicker.show()
//        }

    }


    private fun updateLabal(myCalendar: Calendar) {


        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        tvdate.setText(sdf.format(myCalendar.time))

    }


}





