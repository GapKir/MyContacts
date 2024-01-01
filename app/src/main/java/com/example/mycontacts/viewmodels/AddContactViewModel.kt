package com.example.mycontacts.viewmodels

import android.app.Application
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import com.example.mycontacts.App
import com.example.mycontacts.R
import com.example.mycontacts.model.Contact
import java.io.File
import java.io.IOException

class AddContactViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var imageUri: Uri? = null

    override fun onCleared() {
        super.onCleared()
        getApplication<App>().contactService.saveContactsToPrefs()
    }

    fun createContact(name: String, phone: String, photo: String? = null): Boolean {

        if (fieldAreEmpty(name, phone)) return false
        if (contactAlreadyExist(name, phone)) return false

        getApplication<App>().contactService.addContact(
            Contact(
                getApplication<App>().contactService.getContacts().size.toLong(),
                name,
                phone,
                photo ?: imageUri.toString()
            )
        )
        return true
    }

    fun copyImageToPath(fromUri: Uri) {
        createUriForFile()
        try {
            val contentResolver = getApplication<App>().contentResolver
            contentResolver.openInputStream(fromUri)?.use { inputStream ->
                contentResolver.openOutputStream(imageUri!!)?.use { outputStream ->
                    val buffer = ByteArray(1024)
                    while (inputStream.read(buffer) != -1) {
                        outputStream.write(buffer)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getContactsFromPhone() {
        val contentResolver = getApplication<App>().contentResolver
        val contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
        )

        val cursor = contentResolver.query(contactUri, projection, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {

                val nameIndex =
                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val photoIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

                do {
                    val name = it.getString(nameIndex)
                    val number = it.getString(numberIndex)
                    val photo = it.getString(photoIndex)

                    createContact(name, number, photo)
                } while (it.moveToNext())
            }
        }
        showToast(
            getApplication<App>()
                .resources
                .getString(R.string.import_contacts)
        )
    }

    private fun createUriForFile() {
        val imageFile = File(getApplication<App>().filesDir, IMAGE)

        imageUri = FileProvider.getUriForFile(
            getApplication(),
            getApplication<App>().getString(R.string.authority),
            imageFile
        )
    }

    private fun showToast(text: String) {
        Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show()
    }

    private fun contactAlreadyExist(name: String, phone: String): Boolean {
        val contacts = getApplication<App>().contactService.getContacts()
        val existContacts = contacts.any { contact ->
            contact.name == name && contact.phone == phone
        }
        if (existContacts) {
            showToast(
                getApplication<App>()
                    .resources
                    .getString(R.string.exist_contact, name)
            )
            return true
        }
        return false
    }

    private fun fieldAreEmpty(name: String, phone: String): Boolean {
        if (name.isBlank() || phone.isBlank()) {
            showToast(
                getApplication<App>()
                    .resources
                    .getString(R.string.empty_field)
            )
            return true
        }
        return false
    }

    companion object {
        private const val IMAGE = "photo.jpg"
    }


}