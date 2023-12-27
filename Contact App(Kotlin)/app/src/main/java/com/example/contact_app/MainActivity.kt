package com.example.contact_app

import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import android.widget.Toast
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var myAdapter: MyRecyclerViewAdapter
    private var contacts: MutableList<Contact> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_options)
        //Calling
        getContacts()

        val add_contact_btn = findViewById<Button>(R.id.add_contact)
        val search_contact_field = findViewById<EditText>(R.id.search_contact)
        val contactsRecylyerView = findViewById<RecyclerView>(R.id.recyclerview)
        contactsRecylyerView.layoutManager = LinearLayoutManager(this)
        //contacts = getData()
//        names = getNames()
        println(contacts.count())
        myAdapter = MyRecyclerViewAdapter(this) { selectedContact, id, col, list ->
            val builder = AlertDialog.Builder(col)
            builder.setTitle("Choose an option")
                .setItems(arrayOf("Edit", "Delete")) { dialog, which ->
                    when (which) {
                        0 -> editContact(selectedContact, col)

                        1 -> selectedContact.let {
                            showDeleteConfirmationDialog(it, id, col, list.toMutableList())

                        }
                    }
                }
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }

            builder.create().show()
        }

        contactsRecylyerView.adapter = myAdapter

        add_contact_btn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddContact::class.java)
            startActivity(intent)
        })


        search_contact_field.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                var queryText = s.toString()
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    onQueryTextChange(queryText)
                }, 0) // Adjust the delay as needed
            }
        })
    }

    private fun onQueryTextChange(query: String) {
        if (query.isEmpty()) {
            getContacts()
            myAdapter.notifyDataSetChanged()
        } else {
            filterContacts(query)
        }
        //hideKeyboard(this@MainActivity)
    }

    private fun hideKeyboard(context: Context) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }


    private fun filterContacts(query: String) {
        val filteredContacts = contacts.filter {
            it.fields.name.contains(query, ignoreCase = true) || it.fields.phone_number.contains(
                query,
                ignoreCase = true
            )
        }
        myAdapter.clear()
        myAdapter.setBooks(filteredContacts)
        myAdapter.notifyDataSetChanged()
    }

    fun getContacts() {
        val baseUrl = "http://192.168.43.230:8000/contacts/"

        val retrofitt = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ContactApi::class.java)

        // val apiService: ContactApi = retrofitt.create(ContactApi::class.java)
        val retrofitData = retrofitt.getAllContacts()
        retrofitData.enqueue(object : Callback<MutableList<Contact>?> {
            override fun onResponse(
                call: Call<MutableList<Contact>?>,
                response: Response<MutableList<Contact>?>
            ) {
                if (response.isSuccessful) {
                    val contactResponse = response.body()!!
                    for (mydata in contactResponse) {
                        println(mydata.fields.name)
                        println(mydata.model)
                    }
                    contacts = contactResponse
                    myAdapter.setBooks(contactResponse)
                } else {
                    Log.e("MainActivityF", "Success getting Contacts")
                }

            }

            override fun onFailure(call: Call<MutableList<Contact>?>, t: Throwable) {
                Log.e("MainActivityF", "Error getting Contacts", t)
            }
        })
    }

    private fun deleteContact(
        contact: Contact?,
        position: Int,
        context: Context,
        contactList: MutableList<Contact>
    ) {
        Toast.makeText(context, "Delete: ${contact?.fields?.name}", Toast.LENGTH_SHORT).show()

        // Remove the item from the list
        contactList.removeAt(position)
        deleteContactApi(contact?.fields?.phone_number!!, context)
        getContacts()
        myAdapter.notifyDataSetChanged()
    }

    private fun showDeleteConfirmationDialog(
        selectedContact: Contact,
        position: Int,
        context: Context,
        contactList: MutableList<Contact>
    ) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Delete Contact")
        alertDialogBuilder.setMessage("Are you sure you want to delete this contact?")

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            deleteContact(selectedContact, position, context, contactList)
        }

        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.create().show()
    }

    fun deleteContactApi(phone: String, context: Context) {
        val baseUrl = "http://192.168.43.230:8000/"

        val retrofitt = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ContactApi::class.java)
        var success = 0
        val call = retrofitt.deleteContact(phone)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() == 200) {
                    success = 1
                    Log.d("SUCCESS", "${success}")
                } else {
                    Log.e("DeleteContact", "Failed to delete Contact: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("DeleteContact", "Failed to delete Contact: ${t}")
            }
        })

        Toast.makeText(context, "Contact deleted successfully", Toast.LENGTH_LONG).show()
    }



    private fun editContact(contact: Contact?, context: Context) {

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Contact")

        val inflater = LayoutInflater.from(context)
        val contactFieldView = inflater.inflate(R.layout.edit_data_field, null)
        val contactFieldText = contactFieldView.findViewById<EditText>(R.id.contact_edit_field)
        contactFieldText.setText(contact?.fields?.phone_number)

        val nameFieldText = contactFieldView.findViewById<EditText>(R.id.name_edit_field)
        nameFieldText.setText(contact?.fields?.name)

        builder.setView(contactFieldView)

        builder.setPositiveButton("OK") { dialog, _ ->
            val contactText = contactFieldText.text.toString()
            val nameText = nameFieldText.text.toString()

            UpdateContactApi(
                contact?.fields?.phone_number!!,
                nameText,
                contactText,
                context
            )
           dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    fun UpdateContactApi(
        phone: String,
        newName: String,
        newphone: String,
        context: Context
    ){
        val baseUrl = "http://192.168.43.230:8000/contacts/"

        val retrofitt = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ContactApi::class.java)
        var success = 0
        val call = retrofitt.updateContact(phone, newName, newphone)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    success = 1
                    Log.d("SUCCESS", "${success}")
                    myAdapter.clear()
                    getContacts()
                    myAdapter.notifyDataSetChanged()
                    Toast.makeText(context, "Contact updated!!!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Contact not updated!!!", Toast.LENGTH_SHORT).show()
                    Log.e("UPdateContact", "Failed to Update Contact: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show()
                Log.e("UpdateContact", "Failed to Update Contact: ${t}")
            }
        })
    }
}
