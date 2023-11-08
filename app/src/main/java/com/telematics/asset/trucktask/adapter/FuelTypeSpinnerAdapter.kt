package com.telematics.asset.trucktask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatTextView
import com.telematics.asset.trucktask.R
import com.telematics.asset.trucktask.model.FuelType

class FuelTypeSpinnerAdapter(context: Context, dataList: List<FuelType>) : ArrayAdapter<FuelType>(context, 0, dataList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        val fuelItem:FuelType = getItem(position) as FuelType

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)

        view.findViewById<AppCompatTextView>(R.id.name).text = fuelItem.text

        return view
    }
}