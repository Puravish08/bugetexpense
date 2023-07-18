package com.musict.budgetexpensemanagerhelp.modelclass

import android.os.Parcel
import android.os.Parcelable

data class modelclass (var category: String)


data class tieddata(var id:Int,var type : Int,var amount : String,var category: String , var date : String,var time : String,var mode : String , var note  :String): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    data class TransactionData(
        val id: Int,
        val type: Int,
        val amount: String,
        val category: String,
        val date: String,
        val time: String,
        val mode: String,
        val note: String
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(type)
        parcel.writeString(amount)
        parcel.writeString(category)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(mode)
        parcel.writeString(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<tieddata> {
        override fun createFromParcel(parcel: Parcel): tieddata {
            return tieddata(parcel)
        }

        override fun newArray(size: Int): Array<tieddata?> {
            return arrayOfNulls(size)
        }
    }}