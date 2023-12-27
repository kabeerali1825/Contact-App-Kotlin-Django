package com.example.contact_app


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView



class MyRecyclerViewAdapter(private val context: Context, private val itemClickListener: (Contact, Int, Context, List<Contact>) -> Unit) :
    RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    private var contacts: List<Contact>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setBooks(contacts: List<Contact>?) {
        this.contacts = contacts
        notifyDataSetChanged()
    }
    fun clear(){
        contacts= listOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = contacts?.get(position)
        holder.bind(product!!)
        holder.itemView.setOnClickListener {
            val myContact = contacts?.get(position)
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${myContact?.fields?.phone_number}")
            context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener{
            val contact = contacts?.get(position)
            itemClickListener(contact!!,position,context,contacts!!)
            true

//
//            true // Consume the long click event
        }
    }


    override fun getItemCount(): Int {
        return contacts?.size ?: 0
    }

    inner class ViewHolder(currentView: View) :
        RecyclerView.ViewHolder(currentView) {
        fun bind(contacts: Contact) {
            val nameTv: TextView = itemView.findViewById(R.id.textName)
            val priceTv: TextView = itemView.findViewById(R.id.textContact)
            nameTv.text = contacts.fields.name
            priceTv.text = contacts.fields.phone_number
        }
    }
}
