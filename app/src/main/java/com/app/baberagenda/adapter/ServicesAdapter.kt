package com.app.baberagenda.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.baberagenda.databinding.ServicesItemBinding
import com.app.baberagenda.model.Services

class ServicesAdapter(
    private val context: Context,
    private val listaServices: MutableList<Services>
) :
    RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {

        val itemLista = ServicesItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ServicesViewHolder(itemLista)

    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        holder.imgService.setImageResource(listaServices[position].img!!)
        holder.txtService.text = listaServices[position].name
    }

    override fun getItemCount(): Int {
        return listaServices.size
    }

    inner class ServicesViewHolder(binding: ServicesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imgService = binding.imgService
        val txtService = binding.txtTitleService
    }
}