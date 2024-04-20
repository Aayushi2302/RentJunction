package com.example.rentjunction

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_delivery_buy.*
import java.util.*

class DeliveryBuy : AppCompatActivity() {
    var emails: String? = null
    var home: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_delivery_buy)

        // login info passing
        val intents = intent
        emails = intents.getStringExtra("emails")
        // login info passing

        homeBut2.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@DeliveryBuy, Home::class.java)
            // pass login data to menuScreen
            intent.putExtra("emails", emails)
            // pass login data to menuScreen
            startActivity(intent) // go to sellBaseScreen screen
        })
    }

    override fun onBackPressed() {
        val intent = Intent(this@DeliveryBuy, Home::class.java)
        // pass login data to menuScreen
        intent.putExtra("emails", emails)
        // pass login data to menuScreen
        startActivity(intent) // go to sellBaseScreen screen
        super.onBackPressed()
    }
}