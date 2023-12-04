package com.example.rentjunction

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.appBar

class Home : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpViews()

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("user").document(userId)

        ref.get().addOnSuccessListener {
            if(it!=null){
                val name = it.data?.get("name")?.toString()
                val email = it.data?.get("email")?.toString()

                val userName : TextView = findViewById(R.id.userName)
                val userEmail : TextView = findViewById(R.id.userMail)
                userName.text = name
                userEmail.text = email

            }
        }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to fetch data",Toast.LENGTH_SHORT).show()
            }

    }

    private fun setUpViews(){
        setUpDrawerLayout()
    }

    private fun setUpDrawerLayout(){
        //you can directly use the id without using R.id.id_name
        setSupportActionBar(appBar)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener {

            it.isChecked = true
            drawerLayout.closeDrawer(GravityCompat.START)
            when(it.itemId){

                R.id.home->{
                    val intent = Intent(applicationContext,Home::class.java)
                    startActivity(intent)
                }
                R.id.aboutus -> replaceFragment(AboutFragment())
                R.id.logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                }
                R.id.feedback -> replaceFragment(FeedbackFragment())
                R.id.contactus -> replaceFragment(ContactFragment())
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment){

        val rootView : LinearLayout = findViewById(R.id.rootView)
        rootView.removeAllViews()
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}