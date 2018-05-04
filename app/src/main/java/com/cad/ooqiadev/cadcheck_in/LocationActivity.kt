package com.cad.ooqiadev.cadcheck_in

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class LocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        val locName = intent.getStringExtra(MainAdapter.ViewHolder.LOC_NAME)
        supportActionBar?.title = locName

    }
}