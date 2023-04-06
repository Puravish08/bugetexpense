package com.musict.budgetexpensemanagerhelp.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.musict.budgetexpensemanagerhelp.modelclass.ModeModel


class ModeSqlLite (context: Context) : SQLiteOpenHelper(context, "modeDb", null, 1) {

    var mlist = ArrayList<ModeModel>()


    override fun onCreate(db: SQLiteDatabase?) {


        var sql =
            "create table modeTb(mode_id Integer Primary key autoincrement,mode text)"
        db?.execSQL(sql)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {


    }



    fun modeinsert(modem: String) {

        var db = writableDatabase

        var m = ContentValues()

        m.put("mode", modem)

        db.insert("modeTb", null, m)


    }


    fun displaymode(): ArrayList<ModeModel> {

        mlist.clear()


        var db = readableDatabase

        var sql = "select * from modeTb"

        var cursor = db.rawQuery(sql, null)


        if (cursor.count > 0) {

            cursor.moveToFirst()

            do {

                val modelName = cursor.getString(1)

                var mmodel = ModeModel(modelName)
                mlist.add(mmodel)

            } while (cursor.moveToNext())
        } else {

            Log.e("TAG", "displayModel: " + "no data found")
        }

        return mlist

    }





}



