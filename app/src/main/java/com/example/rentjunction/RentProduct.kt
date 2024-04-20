package com.example.rentjunction

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        setContentView(R.layout.activity_rent_product)

        i = findViewById(R.id.p_image)
        n = findViewById(R.id.p_name)
        d = findViewById(R.id.p_des)
        p = findViewById(R.id.p_price)
        r = findViewById(R.id.p_rating)
        cancel = findViewById(R.id.cancel)
        next = findViewById(R.id.confirm)

        val intent = intent
        emails = intent.getStringExtra("emails")
        name = intent.getStringExtra("name")
        description = intent.getStringExtra("description")
        rating = intent.getStringExtra("rating")
        price = intent.getStringExtra("price")
        image = intent.getStringExtra("image")

        // Load image using Glide
        if (image != null) {
            Glide.with(this@RentProduct)
                .load(Uri.parse(image)) // Convert string URI to Uri object
                .into(i)
        }

        // Set text for EditText fields
        n.setText(name)
        d.setText(description)
        p.setText(price)
        r.setText(rating)

        // Set click listeners
        cancel.setOnClickListener {
            navigateToBuyScreen()
        }

        next.setOnClickListener {
            handlePermissionsAndSaveData()
        }

        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    private fun navigateToBuyScreen() {
        val intent = Intent(this@RentProduct, BuyScreen::class.java)
        intent.putExtra("emails", emails)
        startActivity(intent)
    }

    private fun handlePermissionsAndSaveData() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission is already granted, save data
            saveData()
        }
    }

    private fun saveData() {
        // Check if 'emails' is null or empty
        if (emails.isNullOrEmpty()) {
            // Handle the case where 'emails' is null or empty
            Toast.makeText(this@RentProduct, "Email address is empty or null", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a collection reference using 'emails'
        val collectionReference = firebaseFirestore.collection(emails!!)

        val items = HashMap<String, String>()
        items["name"] = name ?: ""
        items["description"] = description ?: ""
        items["rating"] = rating ?: ""
        items["address"] = price ?: ""
        items["image"] = image ?: ""
        items["type"] = "Paid"
        items["head"] = "Bought By You"

        collectionReference.add(items)
            .addOnSuccessListener {
                val intent = Intent(this@RentProduct, DeliveryBuy::class.java)
                intent.putExtra("emails", emails)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@RentProduct, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with saving data
                saveData()
            } else {
                // Permission denied, show a message or redirect to settings
                Toast.makeText(this, "Permission denied. You can grant the permission in the app settings.", Toast.LENGTH_SHORT)
                    .show()
                // Optionally, you can redirect the user to the app settings
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        navigateToBuyScreen()
        super.onBackPressed()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}
