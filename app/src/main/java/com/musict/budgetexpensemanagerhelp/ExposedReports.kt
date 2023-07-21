package com.musict.budgetexpensemanagerhelp

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.barteksc.pdfviewer.PDFView
import com.musict.budgetexpensemanagerhelp.R
import com.musict.budgetexpensemanagerhelp.modelclass.Transaction
import com.musict.budgetexpensemanagerhelp.modelclass.tieddata
import java.io.File
import java.io.FileOutputStream

class ExposedReports : AppCompatActivity() {
    private lateinit var pdfView: PDFView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exposed_reports)


        val fromDate = intent.getStringExtra("FROM_DATE")
        val toDate = intent.getStringExtra("TO_DATE")
        val transactionList = intent.getSerializableExtra("TRANSACTION_LIST") as List<Transaction>

        // Now, you have the fromDate, toDate, and transactionList data.
        // You can use this data to display the PDF report in this activity.
        // Implement the code to display the PDF report here.
        // For demonstration purposes, let's just log the data.
        Log.d("ExportReportActivity", "From Date: $fromDate, To Date: $toDate")
        Log.d("ExportReportActivity", "Transaction List: $transactionList")


    }
}