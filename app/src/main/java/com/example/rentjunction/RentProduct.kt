package com.example.rentjunction

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class RentProduct : AppCompatActivity() {

    private var emails: String? = null
    private var name: String? = null
    private var description: String? = null
    private var rating: String? = null
    private var price: String? = null
    private var image: String? = null

    private lateinit var n: EditText
    private lateinit var d: EditText
    private lateinit var r: EditText
    private lateinit var p: EditText
    private lateinit var i: ImageView
    private lateinit var cancel: Button
    private lateinit var next: Button

    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val intent = intent
        emails = intent.getStringExtra("emails")
        name = intent.getStringExtra("name")
        description = intent.getStringExtra("description")
        rating = intent.getStringExtra("rating")
        price = intent.getStringExtra("price")
        image = intent.getStringExtra("image")

        setContentView(R.layout.activity_rent_product)

        i = findViewById(R.id.p_image)
        Glide.with(this@RentProduct).load(image).into(i)

        n = findViewById(R.id.p_name)
        d = findViewById(R.id.p_des)
        p = findViewById(R.id.p_price)
        r = findViewById(R.id.p_rating)
        n.setText(name)
        d.setText(description)
        p.setText(price)
        r.setText(rating)

        cancel = findViewById(R.id.cancel)
        cancel.setOnClickListener {
            val intent = Intent(this@RentProduct, BuyScreen::class.java)
            intent.putExtra("emails", emails)
            startActivity(intent)
        }

        firebaseFirestore = FirebaseFirestore.getInstance()
        next = findViewById(R.id.confirm)
        next.setOnClickListener {
            val items = HashMap<String, String>()
            items["name"] = name!!
            items["description"] = description!!
            items["rating"] = rating!!
            items["address"] = price!!
            items["image"] = image!!
            items["type"] = "Paid"
            items["head"] = "Bought By You"

            firebaseFirestore.collection(emails!!).add(items)

            val intent = Intent(this@RentProduct, DeliveryBuy::class.java)
            intent.putExtra("emails", emails)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@RentProduct, BuyScreen::class.java)
        intent.putExtra("emails", emails)
        startActivity(intent)
        super.onBackPressed()
    }
}