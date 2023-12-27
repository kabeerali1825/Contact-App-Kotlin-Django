package com.example.contact_app


data class Contact(
    val model: String,
    val pk: Int,
    val fields: Fields
)

data class Fields(
    val name: String,
    val phone_number: String
)
