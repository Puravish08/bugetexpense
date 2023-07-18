package com.musict.budgetexpensemanagerhelp

import android.Manifest
import android.app.DownloadManager
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.barteksc.pdfviewer.PDFView
import com.musict.budgetexpensemanagerhelp.R
import com.musict.budgetexpensemanagerhelp.modelclass.tieddata
import java.io.File
import java.io.FileOutputStream

class ExposedReports : AppCompatActivity() {
    private lateinit var pdfView: PDFView


    private val REPORT_URL =
        "https://example.com/report.pdf" // Replace with your report PDF file URL

    companion object {
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exposed_reports)



        pdfView = findViewById(R.id.pdfView)

        val pdfFilePath = intent.getStringExtra("pdfFilePath")
        if (pdfFilePath != null) {
            // Load and display the PDF file
            val file = File(pdfFilePath)
            if (file.exists()) {
                pdfView.fromFile(file)
                    .load()
            } else {
                Toast.makeText(this, "PDF file not found", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "PDF file path not provided", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    }


//    private fun generatePDFReport(dataList: List<tieddata>) {
//        // Create the PDF document
//        val pdfDocument = PdfDocument()
//
//        val pageWidth = 595 // Width of the page in points (1 point = 1/72 inch)
//        val pageHeight = 842 // Height of the page in points (1 point = 1/72 inch)
//        val pageNumber = 1 // Page number (e.g., 1 for the first page)
//
//        val paint = Paint()
//        paint.textSize = 12f // Set the font size
//        paint.color = Color.BLACK // Set the text color
//
//        val x = 50f // X coordinate of the starting point for drawing the text
//        val y = 50f // Y coordinate of the baseline for drawing the text
//
//
//        // Create a page with the desired attributes
//        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
//
//        // Start a page
//        val page = pdfDocument.startPage(pageInfo)
//
//        // Get the canvas for drawing
//        val canvas = page.canvas
//
//        // Draw your content on the canvas using the retrieved data
//        for (data in dataList) {
//            // Draw each data item on the canvas
//            val text =
//                "${data.type} ${data.amount} ${data.category} ${data.date} ${data.time} ${data.mode} ${data.note}"
//            canvas.drawText(text, x, y, paint)
//            // Adjust the x and y coordinates for the next item
//            // ...
//        }
//
//        // Finish the page
//        pdfDocument.finishPage(page)
//
//        // Save the PDF document to a file
//        val outputFile = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "report.pdf")
//        pdfDocument.writeTo(FileOutputStream(outputFile))
//
//        // Close the PDF document
//        pdfDocument.close()
//
//        // Load and display the PDF using a PDF viewer library of your choice
//        // For example, you can use PDFView from the AndroidPdfViewer library
//        val pdfView: PDFView = findViewById(R.id.pdfView)
//        pdfView.fromFile(outputFile)
//            .load()
//
//    }

