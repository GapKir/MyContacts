package com.example.mycontacts.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mycontacts.App
import com.example.mycontacts.model.Contact

class ContactListViewModel(
    application: Application
):AndroidViewModel(application){

    private val _contactList = MutableLiveData<List<Contact>>()
    val contactList: LiveData<List<Contact>> = _contactList

    init {
            _contactList.value = (application as App).contactService.getContacts()
    }

}