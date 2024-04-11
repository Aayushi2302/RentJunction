package com.example.rentjunction

import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class ProvideProduct : AppCompatActivity() {

    private lateinit var upload: Button
    private lateinit var img: ImageView
    private var imageUri: Uri? = null
    private val IMAGE_REQ = 2
    private lateinit var name: EditText
    private lateinit var description: EditText
    private lateinit var rating: EditText
    private lateinit var address: EditText
    private lateinit var price: EditText
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var emails: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_provide_product)

        intent.getStringExtra("emails")?.let {
            emails = it
        }

        upload = findViewById(R.id.select_img)
        upload.setOnClickListener {
            openImage()
        }

        name = findViewById(R.id.rent_name)
        description = findViewById(R.id.rent_des)
        rating = findViewById(R.id.rent_rating)
        address = findViewById(R.id.rent_address)
        price = findViewById(R.id.rent_price)
        img = findViewById(R.id.rent_img)

        firebaseFirestore = FirebaseFirestore.getInstance()

        val post: Button = findViewById(R.id.post_it)
        post.setOnClickListener {
            insertData()
        }
    }

    private fun insertData() {
        val pd = ProgressDialog(this)
        pd.setMessage("Uploading")
        pd.show()

        val item = HashMap<String, Any>()
        item["name"] = name.text.toString()
        item["description"] = description.text.toString()
        item["rating"] = rating.text.toString()
        item["price"] = price.text.toString()

        // Add image URI if available
        imageUri?.let {
            item["image"] = it.toString()
        }

        // Push data to Firestore
        firebaseFirestore.collection("Products").add(item)
            .addOnSuccessListener { documentReference ->
                pd.dismiss()
                Toast.makeText(this@ProvideProduct, "Post Successful", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQ && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            Glide.with(this).load(imageUri).into(img)
        }
    }

    // Getting the image extension type
    private fun getFileExtension(uri: Uri): String? {
        val contentResolver: ContentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    override fun onBackPressed() {
        val intent = Intent(this@ProvideProduct, Home::class.java)
        intent.putExtra("emails", emails)
        startActivity(intent)
        super.onBackPressed()
    }
}