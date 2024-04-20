package com.example.rentjunction

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    // Button
    private lateinit var register: MaterialButton

    // database
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var username: EditText
    private lateinit var pass: EditText
    private lateinit var email: EditText
    private lateinit var address: EditText
    private lateinit var repass: EditText
    private lateinit var ph: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_register)

        // pushing user information into database
        // Registration Button starts
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        username = findViewById(R.id.inputUsername)
        pass = findViewById(R.id.inputPassword)
        email = findViewById(R.id.inputEmail)
        address = findViewById(R.id.inputLocation)
        repass = findViewById(R.id.confirmPassword)
        ph = findViewById(R.id.inputPhone)
        register = findViewById(R.id.registerButton)

        register.setOnClickListener {
            val user = username.text.toString()
            val password = pass.text.toString()
            val emails = email.text.toString()
            val adr = address.text.toString()
            val pasRe = repass.text.toString()
            val phone = ph.text.toString()

            // check all info given or not
            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(password) || TextUtils.isEmpty(pasRe) || TextUtils.isEmpty(emails) || TextUtils.isEmpty(adr) || TextUtils.isEmpty(phone)) {
                Toast.makeText(this@RegisterActivity, "Please! Fill Up All Information", Toast.LENGTH_SHORT).show()
            } else {
                // check both pass and repass same or not
                if (password == pasRe) {
                    // creating a account in authentication  with email and password just for login
                    auth.createUserWithEmailAndPassword(emails, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // creating another account with all information of registration
                            val userModel = UserModel(user, emails, adr, password, phone)
                            // generating a user id for registration
                            val id = task.result?.user?.uid
                            id?.let {
                                database.reference.child("Users").child(it).setValue(userModel)
                            }
                            // check registration successful or not
                            Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        } else {
                            Toast.makeText(this@RegisterActivity, "Registration Failed" + task.exception, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // if password and re password not match reset the registration screen
                    Toast.makeText(this@RegisterActivity, "Password not matched", Toast.LENGTH_SHORT).show()
                    // mis match pass
                    val intent = Intent(applicationContext, RegisterActivity::class.java)
                    startActivity(intent) // restart screen
                }
            }
        }
        // Registration Button ends
        // pushing user information into database
    }
}
