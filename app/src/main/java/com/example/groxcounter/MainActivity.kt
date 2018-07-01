package com.example.groxcounter

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import butterknife.ButterKnife
import kotterknife.bindView

class MainActivity: AppCompatActivity() {

    private val simpleActivityButton: Button by bindView(R.id.simple_activity)
    private val persistActivityButton: Button by bindView(R.id.persist_activity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        simpleActivityButton.setOnClickListener {
            intent = Intent(this, SimpleCounterActivity::class.java)
            startActivity(intent)
        }

        persistActivityButton.setOnClickListener {
            intent = Intent(this, PersistCounterActivity::class.java)
            startActivity(intent)
        }

    }
}