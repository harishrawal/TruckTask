package com.telematics.asset.trucktask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.telematics.asset.trucktask.databinding.VehicleTypeItemBinding
import com.telematics.asset.trucktask.model.VehicleType

class CustomAdapter(
    private val vehicleTypeList: ArrayList<VehicleType>,
    private val listener: (VehicleType, Int) -> Unit
) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = VehicleTypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(vehicleTypeList[position])
        holder.itemView.setOnClickListener { listener(vehicleTypeList[position], position) }
    }

    override fun getItemCount(): Int {
        return vehicleTypeList.size
    }

    fun updateData(data: ArrayList<VehicleType>){
        vehicleTypeList.clear()
        vehicleTypeList.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(var itemBinding: VehicleTypeItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(vehicleType: VehicleType) {
            itemBinding.name.text = vehicleType.text
        }
    }
}