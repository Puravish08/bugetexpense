package com.musict.budgetexpensemanagerhelp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.musict.budgetexpensemanagerhelp.R
import com.musict.budgetexpensemanagerhelp.modelclass.ModeModel
import com.musict.budgetexpensemanagerhelp.modelclass.modelclass

class ModeAdapter(var modeofTypes: ArrayList<ModeModel>,var n : ((String) -> Unit)) :
    RecyclerView.Adapter<ModeAdapter.myViewHolder>() {


    var pos=-1


    class myViewHolder (itemview : View) : RecyclerView.ViewHolder(itemview)

    {
        var modeName: RadioButton =itemview.findViewById(R.id.rbm)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {

        var view= LayoutInflater.from(parent.context).inflate(R.layout.modename,parent,false)
        var mm = myViewHolder(view)
        return mm


    }


    override fun getItemCount(): Int {

        return modeofTypes.size

    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {



        holder.modeName.text = modeofTypes[position].Mode


        holder.modeName.setOnClickListener{

            n.invoke(modeofTypes[position].Mode)


            pos = position
            notifyDataSetChanged()



            if (position==pos)
            {

                holder.modeName.isChecked = true
            }
            else
            {
                holder.modeName.isChecked =false
            }


        }




    }


}