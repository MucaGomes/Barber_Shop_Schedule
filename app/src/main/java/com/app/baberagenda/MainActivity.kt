package com.app.baberagenda

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.app.baberagenda.databinding.ActivityMainBinding
import com.app.baberagenda.view.Home
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btLogin.setOnClickListener {
            val name = binding.edtName.text.toString()
            val password = binding.edtPassword.text.toString()

            when {
                name.isEmpty() -> {
                    mensagem(it, "Coloque seu Nome")
                }
                password.isEmpty() -> {
                    mensagem(it, "Preencha sua senha!")
                }
                password.length <= 5 -> {
                    mensagem(it, "Insira uma senha de no minimo 5 caracteres!")
                }else -> {
                    navigateHome(name)
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

    private fun navigateHome(name:String) {
        val intent = Intent(this, Home::class.java)
        intent.putExtra("nome",name)
        startActivity(intent)
    }
}