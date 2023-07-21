package com.musict.budgetexpensemanagerhelp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import com.musict.budgetexpensemanagerhelp.databinding.ActivityTearmsShowBinding

class TearmsShowActivity : AppCompatActivity() {

    lateinit var bindign : ActivityTearmsShowBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindign =  ActivityTearmsShowBinding.inflate(layoutInflater)
        setContentView(bindign.root)


        bindign.btnContinue.setOnClickListener {

            val i = Intent(this@TearmsShowActivity, TearmsShowActivity::class.java)
            startActivity(i)
            finish()
        }

        bindign.privcy.setOnClickListener {

            val privacyPolicyIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://secureprivacy2.blogspot.com/2023/07/bugetexpenssemangerhelp-privacy-policy.html")
            )
            startActivity(privacyPolicyIntent)

        }
        bindign.termsofservice.setOnClickListener {

            val termsIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://secureprivacy2.blogspot.com/2023/07/bugetexpenssemangerhelp-terms-of-use.html")
            )
            startActivity(termsIntent)

        }








    }
}