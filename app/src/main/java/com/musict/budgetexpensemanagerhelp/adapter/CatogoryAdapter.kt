package com.musict.budgetexpensemanagerhelp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.musict.budgetexpensemanagerhelp.R
import com.musict.budgetexpensemanagerhelp.modelclass.modelclass

class CatogoryAdapter(var listofTypes: ArrayList<modelclass>,var n : ((String) -> Unit)) : RecyclerView.Adapter<CatogoryAdapter.myViewHolder>() {


    var pos = -1


    class myViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        var categoryName: RadioButton = itemview.findViewById(R.id.rbc)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {

        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.categoryieslist, parent, false)
        var vv = myViewHolder(view)
        return vv
    }


    override fun getItemCount(): Int {

        return listofTypes.size

    }


    override fun onBindViewHolder(holder: myViewHolder, position: Int) {

        holder.categoryName.setText(listofTypes[position].category)

        holder.categoryName.setOnClickListener {

            n.invoke(listofTypes[position].category)


            pos = position
            notifyDataSetChanged()

        }

        if (position == pos) {

            holder.categoryName.isChecked = true
        } else {
            holder.categoryName.isChecked = false
        }


    }

}