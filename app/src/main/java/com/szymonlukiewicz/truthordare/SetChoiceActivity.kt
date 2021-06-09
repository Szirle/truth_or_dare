package com.szymonlukiewicz.truthordare

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView


class SetChoiceActivity : AppCompatActivity() {

    fun onXClick(view: View) {

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_choice)

        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)


    }


}