package com.musict.budgetexpensemanagerhelp.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.musict.budgetexpensemanagerhelp.R
import com.musict.budgetexpensemanagerhelp.modelclass.tieddata

class TransactionAdapter(var datastorage  :ArrayList<tieddata>,var invo : ((tieddata) -> Unit), var deletivoke : (Int ) -> Unit) : RecyclerView.Adapter<TransactionAdapter.myViewHolder>() {




    class myViewHolder (itemview : View) : RecyclerView.ViewHolder(itemview){


        var txtmoney: TextView =itemview.findViewById(R.id.txtmoney)
        var txtnote: TextView =itemview.findViewById(R.id.txtnote)
        var txtccategory: TextView =itemview.findViewById(R.id.txtccategory)
        var txttime: TextView =itemview.findViewById(R.id.txttime)
        var txtdate: TextView =itemview.findViewById(R.id.txtdate)
        var txttype: TextView =itemview.findViewById(R.id.txttype)
        var txtmode: TextView =itemview.findViewById(R.id.txtmode)

        var txtedit:ImageView = itemview.findViewById(R.id.editUp)
        var imgDelet : ImageView = itemview.findViewById(R.id.imgdel)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {

        val View = LayoutInflater.from(parent.context).inflate(R.layout.tstorageitem,parent,false)
        val adapter = myViewHolder(View)

        return adapter

    }

    override fun getItemCount(): Int {

        return   datastorage.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {

        holder.txtmoney.text = datastorage[position].amount
        holder.txtnote.text = datastorage[position].note
        holder.txtccategory.text = datastorage[position].category
        holder.txttime.text = datastorage[position].time
        holder.txttype.text = datastorage[position].type.toString()
        holder.txtdate.text = datastorage[position].date
        holder.txtmode.text = datastorage[position].mode


        if (datastorage.get(position).amount.toString().toInt()>0) {
            holder.txtmoney.setTextColor(Color.GREEN)
        } else {

            holder.txtmoney.setTextColor(Color.RED)

        }

        // Amount colot change in category
        holder.txtmode.setTextColor(Color.parseColor("#5271ff"))
//        amount.setTextColor(Color.parseColor("#FF0000"))


        holder.txtedit.setOnClickListener{

            invo.invoke(datastorage[position])

        }


        holder.imgDelet.setOnClickListener{

            deletivoke.invoke(datastorage[position].id)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(datastorage: ArrayList<tieddata>){

        this.datastorage = ArrayList()
        this.datastorage.addAll(datastorage)
        notifyDataSetChanged()


    }










}