package com.musict.budgetexpensemanagerhelp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import java.util.Calendar

class CalenderActivity : AppCompatActivity() {

    // on below line we are creating
    // variables for text view and calendar view
    private lateinit var dateTextView: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var backme: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)
        iniView()
    }

    private fun iniView() {





        dateTextView = findViewById(R.id.dateTextView)
        calendarView = findViewById(R.id.calendarView)
        backme = findViewById(R.id.backme)



        backme.setOnClickListener {

             var arrow = Intent(this, MainActivity::class.java)
            startActivity(arrow)
        }

        dateTextView.setOnClickListener {
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)

            showDatePickerDialog(year, month, dayOfMonth)
        }

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val date = "${dayOfMonth}-${month + 1}-${year}"
            dateTextView.text = date
        }
    }

    private fun showDatePickerDialog(year: Int, month: Int, dayOfMonth: Int) {
        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val date = "${selectedDayOfMonth}-${selectedMonth + 1}-${selectedYear}"
                dateTextView.text = date
            }, year, month, dayOfMonth
        )

        datePickerDialog.show()
    }





}