package com.example.rentjunction

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso

class Status : AppCompatActivity() {

    // RecyclerView
    private lateinit var recyclerView: RecyclerView
    // RecyclerView

    // Database
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var adapter: FirestoreRecyclerAdapter<UserProductTrack, StatusProductViewHolder>? = null
    // Database

    // Data Passing
    private var emails: String? = null
    private var type: String? = null
    // Data Passing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Login info passing
        val intents = intent
        emails = intents.getStringExtra("emails")
        type = intents.getStringExtra("type")
        // Login info passing

        setContentView(R.layout.activity_status)

        // Initializing variables
        firebaseFirestore = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.showProduct)

        // Query
        // Getting the data from database
        val query: Query? = if (type.equals("buy", ignoreCase = true)) {
            firebaseFirestore.collection(emails!!)
                .whereEqualTo("head", "Bought By You") // Collection name from database
        } else {
            firebaseFirestore.collection(emails!!)
                .whereEqualTo("head", "Rented By You") // Collection name from database
        }

        val options = FirestoreRecyclerOptions.Builder<UserProductTrack>()
            .setQuery(query!!, UserProductTrack::class.java)
            .build()

        adapter = object : FirestoreRecyclerAdapter<UserProductTrack, StatusProductViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusProductViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.product_list, parent, false)
                return StatusProductViewHolder(view)
            }

            override fun onBindViewHolder(holder: StatusProductViewHolder, position: Int, model: UserProductTrack) {
                holder.name.text = model.name
                holder.description.text = model.description
                holder.rating.text = model.rating
                holder.type.text = model.type
                holder.address.text = model.address

                val img: String? = model.image
                Picasso.get().load(img).into(holder.image)
            }
        }

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }


    private inner class StatusProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.user_name)
        var description: TextView = itemView.findViewById(R.id.user_des)
        var rating: TextView = itemView.findViewById(R.id.user_rating)
        var type: TextView = itemView.findViewById(R.id.user_type)
        var address: TextView = itemView.findViewById(R.id.user_price_address)
        var image: ImageView = itemView.findViewById(R.id.user_image)
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onBackPressed() {
        val intent = Intent(this@Status, Home::class.java)
        // Pass login data to MenuScreen
        intent.putExtra("emails", emails)
        // Pass login data to MenuScreen
        startActivity(intent) // Go to sellBaseScreen screen
        super.onBackPressed()
    }
}
