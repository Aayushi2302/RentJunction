package com.example.rentjunction

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
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

class BuyScreen : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FirestoreRecyclerAdapter<Products, ProductsViewHolder>
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var emails: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val intents = intent
        emails = intents.getStringExtra("emails")

        setContentView(R.layout.activity_buy_screen)

        firebaseFirestore = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.check)

        val query: Query = firebaseFirestore.collection("Products")
        val options = FirestoreRecyclerOptions.Builder<Products>()
            .setQuery(query, Products::class.java)
            .build()

        adapter = object : FirestoreRecyclerAdapter<Products, ProductsViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.popular_item, parent, false)
                return ProductsViewHolder(view)
            }

            override fun onBindViewHolder(holder: ProductsViewHolder, position: Int, model: Products) {
                holder.bind(model)
            }
        }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@BuyScreen)
            adapter = this@BuyScreen.adapter
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    inner class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.pop_name)
        private val description: TextView = itemView.findViewById(R.id.pop_des)
        private val rating: TextView = itemView.findViewById(R.id.pop_rat)
        private val price: TextView = itemView.findViewById(R.id.pop_rent_price)
        private val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(product: Products) {
            name.text = product.name
            description.text = product.description
            rating.text = product.rating
            price.text = product.price

            Picasso.get().load(product.image).into(image)

            itemView.setOnClickListener {
                val intent = Intent(this@BuyScreen, RentProduct::class.java).apply {
                    putExtra("emails", emails)
                    putExtra("name", product.name)
                    putExtra("description", product.description)
                    putExtra("rating", product.rating)
                    putExtra("price", product.price)
                    putExtra("image", product.image)
                }
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@BuyScreen, Home::class.java).apply {
            putExtra("emails", emails)
        }
        startActivity(intent)
        super.onBackPressed()
    }
}