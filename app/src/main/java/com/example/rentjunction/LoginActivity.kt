package com.example.rentjunction

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    // Button
    private lateinit var login: Button
    private lateinit var register: TextView

    // Database
    private lateinit var auth: FirebaseAuth
    private lateinit var userEmail: EditText
    private lateinit var pass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_login)

        // login Button starts
        auth = FirebaseAuth.getInstance()
        // search for id's
        userEmail = findViewById(R.id.linputEmail)
        pass = findViewById(R.id.linputPassword)
        login = findViewById(R.id.loginButton)

        login.setOnClickListener {
            val user = userEmail.text.toString()
            val password = pass.text.toString()

            // if pass and user box empty : show a message to fill all box
            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(password)) {
                Toast.makeText(this@LoginActivity, "Please! Fill Up All Field", Toast.LENGTH_SHORT).show()
            } else {
                // Authentication : checking database with email and password
                auth.signInWithEmailAndPassword(user, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, Home::class.java)
                        // pass login data
                        intent.putExtra("emails", user)
                        // pass login data
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        // login Button ends

        // Registration Button Starts
        register = findViewById(R.id.lnewLogin)
        register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent) // go to registration screen
        }
        // Registration Button Ends

        lresetPassword.setOnClickListener {
            val intent = Intent(this,ResetPassword::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        // Exit app
        val alertDialog = AlertDialog.Builder(this@LoginActivity)
        alertDialog.setTitle("Exit App")
        alertDialog.setMessage("Do you want to exit app?")
        alertDialog.setPositiveButton("Yes") { _, _ ->
            finishAffinity()
        }

        alertDialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialog.show()
        // exit app
    }

//    override fun onStart() {
//        super.onStart()
//
//        if(auth.currentUser != null)
//        {
//            val intent = Intent(this,Home::class.java)
//            startActivity(intent)
//        }
//    }
}
