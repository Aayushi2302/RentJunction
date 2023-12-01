package com.example.rentjunction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        newLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        firebaseAuth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener {
            val name = inputUsername.text.toString()
            val pwd = inputPassword.text.toString()
            val phone = inputPhone.text.toString()
            val email = inputEmail.text.toString()

            if(email.isNotEmpty() && name.isNotEmpty() && pwd.isNotEmpty() && phone.isNotEmpty())
            {
                if(phone.length == 10) {
                    if(email.contains("@gmail.com")) {
                        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener {

                                if (it.isSuccessful) {

                                    val userMap = hashMapOf(
                                        "name" to name,
                                        "phone" to phone,
                                        "email" to email,
                                        "password" to pwd
                                    )

                                    val uid = firebaseAuth.currentUser?.uid.toString()
                                    db.collection("user").document(uid).set(userMap)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                this,
                                                "Sucessfully data added!!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                this,
                                                "Failed to store data!!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, "Unable to Register!!", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    }else{
                        Toast.makeText(this,"Enter valid email!!",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,"Enter valid number!!",Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this,"Empty Fields  is not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}