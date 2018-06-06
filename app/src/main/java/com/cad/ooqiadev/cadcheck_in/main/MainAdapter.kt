package com.cad.ooqiadev.cadcheck_in.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.Intent
import com.cad.ooqiadev.cadcheck_in.R
import com.cad.ooqiadev.cadcheck_in.customer.CustomerActivity
import com.cad.ooqiadev.cadcheck_in.models.Customer

class MainAdapter(val customerList: ArrayList<Customer>): RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    companion object {
        val CUSTOMER_ID = "customerId"
        val CUSTOMER_NAME = "customerName"
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customer = customerList[position]
        holder.customerName?.text = customer.name
        holder.customerAddress?.text = customer.address1 + ", " +
                customer.state + ". " + customer.city + ", " + customer.zipCode
        holder.customerBadge?.text = customer.type
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CustomerActivity::class.java)
            intent.putExtra(CUSTOMER_ID, customer.id)
            intent.putExtra(CUSTOMER_NAME, customer.name)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_customer, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val customerName = itemView.findViewById<TextView>(R.id.customerName)
        val customerAddress = itemView.findViewById<TextView>(R.id.customerAddress)
        val customerBadge = itemView.findViewById<TextView>(R.id.customerBadge)
    }
}