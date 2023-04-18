package com.app.baberagenda.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.app.baberagenda.R
import com.app.baberagenda.adapter.ServicesAdapter
import com.app.baberagenda.databinding.ActivityHomeBinding
import com.app.baberagenda.model.Services

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var servicesAdapter: ServicesAdapter
    private val listaServices: MutableList<Services> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val name = intent.extras?.getString("nome")

        binding.txtWelcome.text = "Bem Vindo(a), $name"

        val recyclerViewServices = binding.rvlServices
        recyclerViewServices.layoutManager = GridLayoutManager(this, 2)
        servicesAdapter = ServicesAdapter(this, listaServices)
        recyclerViewServices.setHasFixedSize(true)
        recyclerViewServices.adapter = servicesAdapter
        getServices()

        binding.btSchedule.setOnClickListener {
            val intent = Intent(this,Schedule::class.java)
            intent.putExtra("nome",name)
            startActivity(intent)
        }

    }

    private fun getServices() {
        val service1 = Services(R.drawable.img1 ,"Corte de Cabelo")
        listaServices.add(service1)

        val service2 = Services(R.drawable.img2, "Corte de Barba")
        listaServices.add(service2)

        val service3 = Services(R.drawable.img3 ,"Lavagem de Cabelo")
        listaServices.add(service3)

        val service4 = Services(R.drawable.img4 ,"Tratamento de cabelo (Luzes)")
        listaServices.add(service4)

    }
}