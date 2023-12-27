package com.example.contact_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddContact : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact_view)

        val addContactField = findViewById<EditText>(R.id.add_contact_field)
        val addNameField = findViewById<EditText>(R.id.add_name_field)
        val addContactBtn = findViewById<Button>(R.id.add_contact_btn)
        addContactBtn.setOnClickListener(View.OnClickListener {
            val contact: String = addContactField.text.toString()
            val name: String = addNameField.text.toString()

            if (contact != "" && name != "") {
                if (contact.length == 11) {
                    addCon(name, contact)
                } else {
                    Toast.makeText(this, "Please enter 11 digits!!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please Enter Name and Contact!!", Toast.LENGTH_SHORT).show()
            }
        })

    }

fun addCon(name: String, phone: String) {
    val api: ContactApi = Retrofit.Builder()
        .baseUrl("http://192.168.43.230:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ContactApi::class.java)

    val call = api.addContact(name, phone)

    call.enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                val store = responseBody.string()
                Log.d("YourActivity", "Success: $store")

                // Display the success message directly
                Toast.makeText(
                    this@AddContact,
                    store,  // Assuming the success message is in the response body
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@AddContact, MainActivity::class.java)
                startActivity(intent)
            } else {
                Log.d("YourActivity", "Error")
                Toast.makeText(this@AddContact, "Error!!!", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.d("YourActivity", t.toString())
            Toast.makeText(this@AddContact, t.toString(), Toast.LENGTH_SHORT).show()
        }
    })
}



}