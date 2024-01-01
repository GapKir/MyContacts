package com.example.mycontacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycontacts.ui.ContactListFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.activity_root, ContactListFragment())
            .commit()
    }
}