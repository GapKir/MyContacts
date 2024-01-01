package com.example.mycontacts.model

import android.content.Context
import com.example.mycontacts.App
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ContactService(
    context: Context
) {
    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    init {
        getContactsFromPrefs()
    }

    fun getContacts(): List<Contact>{
        return contactList
    }

    fun addContact(contact: Contact){
        contactList.add(contact)
    }

    fun saveContactsToPrefs(){
        val jsonString = Gson().toJson(contactList)
        sharedPrefs.edit().putString(KEY_CONTACTS, jsonString).apply()
    }

    private fun getContactsFromPrefs() {
        val json = sharedPrefs.getString(KEY_CONTACTS, null)
        val type = object : TypeToken<List<Contact>>() {}.type

        if (json != null) {
            contactList = Gson().fromJson<List<Contact>?>(json, type).toMutableList()
        }
    }


    companion object{
        const val SHARED_PREFS_NAME = "my_prefs"
        const val KEY_CONTACTS = "contacts"

        private var contactList = mutableListOf<Contact>().apply {
            add(Contact(1, "Kyrylo", "+0440000001", null))
            add(Contact(2, "Olya", "+0440000002", null))
            add(Contact(3, "Nikita", "+0440000003", null))
            add(Contact(4, "Alexander", "+0440000004", null))
            add(Contact(5, "Nastya", "+0440000005", null))
        }
    }
}
