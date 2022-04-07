package com.guillaume.project9.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.guillaume.project9.R

class MainActivity : AppCompatActivity() {

    //private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)*/

        val bSwitchActivity: Button = findViewById<Button>(R.id.main_button)


        bSwitchActivity.setOnClickListener {
            //val intent: Intent = Intent(this, AddPropertyActivity)
            startActivity(Intent(this, AddPropertyActivity::class.java))
        }

    }
}