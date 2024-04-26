package com.example.to5_hlc

import androidx.appcompat.app.AppCompatActivity

class MyToolbar {

    fun show(activities: AppCompatActivity){
        activities.setSupportActionBar(activities.findViewById(R.id.materialToolbar))
        activities.supportActionBar?.title = ""
    }
}