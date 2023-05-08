package com.app.baberagenda

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.app.baberagenda.databinding.ActivityLoginBinding
import com.app.baberagenda.view.Home
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = Firebase.auth
        val db = Firebase.firestore

        binding.btLogin.setOnClickListener {
            val name = binding.edtName.text.toString()
            val password = binding.edtPassword.text.toString()
            val email = binding.edtEmail.text.toString()

            when {
                name.isEmpty() -> {
                    mensagem(it, "Coloque seu Nome")
                }
                password.isEmpty() -> {
                    mensagem(it, "Preencha sua senha!")
                }
                password.length <= 5 -> {
                    mensagem(it, "Insira uma senha de no minimo 5 caracteres!")
                }
                else -> {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "createUserWithPassword:sucess")

                                val userId = FirebaseAuth.getInstance().currentUser!!.uid

                                val user = hashMapOf(
                                    "name" to name
                                )

                                db.collection("users").document(userId).set(user)
                                    .addOnCompleteListener { documentReference ->
                                        Log.d(
                                            TAG,
                                            "Document added with ID: ${documentReference.result}"
                                        )
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(TAG, "Error adding document", e)
                                    }


                                navigateHome()
                            } else {
                                Log.w(TAG, "createUserWithPassword:failure", task.exception)
                                Toast.makeText(
                                    baseContext, "Authentication failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }

    }

    private fun mensagem(view: View, mensagem: String) {
        val snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor("#FF0000"))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

    private fun navigateHome() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

    public override fun onStart() {
        super.onStart()
        //check user signed
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateHome()
        }
    }
}