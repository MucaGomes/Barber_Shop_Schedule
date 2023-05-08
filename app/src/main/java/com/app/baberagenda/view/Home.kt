package com.app.baberagenda.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.app.baberagenda.R
import com.app.baberagenda.adapter.ServicesAdapter
import com.app.baberagenda.databinding.ActivityHomeBinding
import com.app.baberagenda.model.Services
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var servicesAdapter: ServicesAdapter
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
    private val db = Firebase.firestore
    private val listaServices: MutableList<Services> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val recyclerViewServices = binding.rvlServices
        recyclerViewServices.layoutManager = GridLayoutManager(this, 2)
        servicesAdapter = ServicesAdapter(this, listaServices)
        recyclerViewServices.setHasFixedSize(true)
        recyclerViewServices.adapter = servicesAdapter
        getServices()

        binding.btSchedule.setOnClickListener {
            val intent = Intent(this, Schedule::class.java)
            startActivity(intent)
        }

    }

    private fun getServices() {
        val service1 = Services(R.drawable.hair_cut, "Corte de Cabelo")
        listaServices.add(service1)

        val service2 = Services(R.drawable.beard_cut, "Corte de Barba")
        listaServices.add(service2)

        val service3 = Services(R.drawable.hair_wash, "Lavagem de Cabelo")
        listaServices.add(service3)

        val service4 = Services(R.drawable.products, "Tratamento de cabelo (Luzes)")
        listaServices.add(service4)

    }

    override fun onStart() {
        super.onStart()

        val documentRef = db.collection("users").document(userId)
        documentRef.addSnapshotListener(MetadataChanges.INCLUDE) { document , e ->
            if (document != null ){
                binding.txtWelcome.text = "Bem Vindo(a), ${document.getString("name")?.capitalize()}"
            }
        }
    }
}