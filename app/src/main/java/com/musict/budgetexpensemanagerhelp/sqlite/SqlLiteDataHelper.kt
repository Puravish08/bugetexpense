package com.musict.budgetexpensemanagerhelp.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.musict.budgetexpensemanagerhelp.modelclass.modelclass
import com.musict.budgetexpensemanagerhelp.modelclass.tieddata

class SqlLiteDataHelper(context: Context) : SQLiteOpenHelper(context, "categoriesDb", null, 1) {

    var list = ArrayList<modelclass>()
    var datastore = ArrayList<tieddata>()




    override fun onCreate(db: SQLiteDatabase?) {

        var sql =
            "create table categoriesTb(categories_id Integer Primary key autoincrement,categories text)"
        db?.execSQL(sql)

        var sqll =
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

        var db = writableDatabase

        var c = ContentValues()

        c.put("categories", categoriesN)

        db.insert("categoriesTb", null, c)


    }


    fun displayCategory(): ArrayList<modelclass> {

        list.clear()


        var db = readableDatabase

        var sql = "select * from categoriesTb"

        var cursor = db.rawQuery(sql, null)


        if (cursor.count > 0) {

            cursor.moveToFirst()

            do {

                val categoryName = cursor.getString(1)

                var model = modelclass(categoryName)
                list.add(model)

            } while (cursor.moveToNext())
        } else {

            Log.e("TAG", "displayCategory: " + "no data found")
        }

        return list


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

        var inexp = writableDatabase
        var add = ContentValues()

        add.put("type", type)
        add.put("amount", amount)
        add.put("category", category)
        add.put("date", date)
        add.put("time", time)
        add.put("mode", mode)
        add.put("note", note)

        inexp.insert("StorageTb", null, add)


    }

    fun displayIncomeExpense(): ArrayList<tieddata> {

        datastore.clear()


        var inexp = writableDatabase
        val sql2 = "select * from StorageTb"
        val cursor = inexp.rawQuery(sql2, null)
        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {

                var id = cursor.getInt(0)
                var type = cursor.getInt(1)
                var amount = cursor.getString(2)
                var category = cursor.getString(3)
                var date = cursor.getString(4)
                var time = cursor.getString(5)
                var mode = cursor.getString(6)
                var note = cursor.getString(7)


                val modetwo = tieddata(id,type, amount, category, date, time, mode, note)

                datastore.add(modetwo)

                Log.e(
                    "TAG",
                    "displayIncomeExpense: " + type + " " + amount + " " + category + " " + date + " " + time + " " + " " + mode + " " + note
                )
            } while (cursor.moveToNext())

        }


        return datastore
    }



    fun updateUserData( amount: String ,note: String,id: Int ) {


        val db = writableDatabase
        val sqli = "update StorageTb set amount='$amount',note='$note' where storage_id='$id'"
        db.execSQL(sqli)
//        Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show()
    }


    fun deletData (id: Int)
    {


        var db = writableDatabase

        val delet = "delete from StorageTb where storage_id  = '$id'"
        db.execSQL(delet)

    }




}