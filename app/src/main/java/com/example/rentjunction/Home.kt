package com.example.rentjunction

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header.*

class Home : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    var emails: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val intents = intent
        emails = intents.getStringExtra("emails")

        setUpViews()
        task1()
        task2()

        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("Users")


        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //check all user data and its child one by one
                for (ds in snapshot.children) {
                    // stop where the tracked emails matched with database email
                    // then show all email's parent's all child info

                    if (ds.child("email").getValue(String::class.java) == emails) {
                        userName.text = ds.child("name").getValue(String::class.java)
                        userMail.text = ds.child("email").getValue(String::class.java)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Toast.makeText(this@Home,"Failed to fetch data",Toast.LENGTH_SHORT).show()
            }
        })
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

//        navbottom.setOnNavigationItemSelectedListener {
//
//            it.isChecked = true
//            when(it.itemId){
//
//                R.id.home->{
//                    val intent = Intent(this@Home,Home::class.java)
//                    startActivity(intent)
//                }
//                R.id.mycart ->{
//                    val intent = Intent(this@Home,Status::class.java)
//                    intent.putExtra("emails" ,emails);
//                    intent.putExtra("type" ,"buy");
//                    startActivity(intent)
//                }
//                R.id.myorder -> {
//                    val intent = Intent(this@Home,Status::class.java)
//                    intent.putExtra("emails" ,emails);
//                    intent.putExtra("type" ,"rent");
//                    startActivity(intent)
//                }
//            }
//            true
//        }
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

    private fun task1() {
        val card: CardView = findViewById(R.id.task1)
        card.setOnClickListener {
            val intent = Intent(applicationContext, BuyScreen::class.java)
            intent.putExtra("emails" ,emails)
            startActivity(intent)
        }

    }

    private fun task2() {
        val card: CardView = findViewById(R.id.task2)
        card.setOnClickListener {
            val intent = Intent(applicationContext, ProvideProduct::class.java)
            intent.putExtra("emails" ,emails)
            startActivity(intent)
        }
    }
}