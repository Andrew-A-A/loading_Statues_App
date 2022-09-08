package com.udacity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        findViewById<MotionLayout>(R.id.motion_layout)
       val name=findViewById<TextView>(R.id.file_name_text_view)
       val status=findViewById<TextView>(R.id.statues_text_view)
        val okButton=findViewById<Button>(R.id.button)
        if(intent.getStringExtra("fileName")!=null)
            name.text=intent.getStringExtra("fileName").toString()
        Log.i("LOL",intent.getStringExtra("fileName").toString())
        Log.i("LOL", intent.getBooleanExtra("isSuccesful",true).toString())
        status.text=if (intent.getBooleanExtra("isSuccesful",true)) "Successful"
                     else "Failed"

       if (status.text=="Successful")
            status.setTextColor(Color.GREEN)
        else
            status.setTextColor(Color.RED)

        okButton.setOnClickListener {
            finish()
        }
    }



}
