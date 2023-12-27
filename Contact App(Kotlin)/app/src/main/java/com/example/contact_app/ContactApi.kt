package com.example.contact_app

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface ContactApi {
    @GET("updateContact/{oldPhoneNumber}/{name}/{phoneNumber}/")
    fun updateContact(
        @Path("oldPhoneNumber") oldPhoneNumber: String,
        @Path("name") name: String,
        @Path("phoneNumber") phoneNumber: String
    ): Call<ResponseBody>

    @GET("allContacts")
    fun getAllContacts(): Call<MutableList<Contact>>

    @GET("contacts/deleteContact/{phone}/")
    fun deleteContact(@Path("phone") phone: String): Call<String>


    @GET("contacts/addContact/{name}/{phone_number}/")
    fun addContact(
        @Path("name") name: String,
        @Path("phone_number") phoneNumber: String
    ): Call<ResponseBody>
}