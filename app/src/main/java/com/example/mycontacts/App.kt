package com.example.mycontacts

import android.app.Application
import com.example.mycontacts.model.ContactService

class App : Application() {
    lateinit var contactService: ContactService

    override fun onCreate() {
        super.onCreate()
        contactService = ContactService(this)
    }
}