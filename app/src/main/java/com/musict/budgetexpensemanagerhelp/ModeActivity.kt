package com.musict.budgetexpensemanagerhelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.musict.budgetexpensemanagerhelp.adapter.CatogoryAdapter
import com.musict.budgetexpensemanagerhelp.adapter.ModeAdapter
import com.musict.budgetexpensemanagerhelp.databinding.ActivityModeBinding
import com.musict.budgetexpensemanagerhelp.sqlite.ModeSqlLite

class ModeActivity : AppCompatActivity() {



    lateinit var binding: ActivityModeBinding
    lateinit var database: ModeSqlLite
    lateinit var  modeadan: ModeAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = ModeSqlLite(this)
        view()



    }

    private fun view() {



        binding.btnAdd.setOnClickListener {


            var ModeName = binding.edtMode.text.toString()



            database.modeinsert(ModeName)


        }
            var mlist = database.displaymode()

//            val adapter = ModeAdapter(mlist)
//
//            val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//
//            binding.rcvMode.layoutManager = manager
//            binding.rcvMode.adapter = adapter




            binding.imgback.setOnClickListener {

                onBackPressed()

            }


            binding.imgok.setOnClickListener {

                onBackPressed()

            }



    }

}