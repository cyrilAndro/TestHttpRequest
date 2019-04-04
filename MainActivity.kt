package com.example.testhttprequest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var jsonURL = "https://api.nytimes.com/svc/topstories/v2/science.json?api-key=92Nbf4KeZSKhJXGm5QA3eTgNJjFW61gW"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gv = findViewById<GridView>(R.id.myGridView)
        val downloadButton = findViewById<Button>(R.id.downloadBtn)

        downloadButton.setOnClickListener({JSONDownloader(this@MainActivity, jsonURL, gv).execute()})


    }
}
