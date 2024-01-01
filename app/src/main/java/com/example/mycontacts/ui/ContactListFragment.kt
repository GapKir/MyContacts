package com.example.mycontacts.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycontacts.R
import com.example.mycontacts.adapters.ContactAdapter
import com.example.mycontacts.databinding.FragmentContactListBinding
import com.example.mycontacts.viewmodels.ContactListViewModel

class ContactListFragment: Fragment() {
    private lateinit var binding: FragmentContactListBinding
    private val adapter = ContactAdapter()
    private val viewModel: ContactListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactListBinding.inflate(inflater, container, false)

        initAdapter()
        initListeners()
        observeViewModel()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.contactList.observe(viewLifecycleOwner){ contacts->
            adapter.changedContacts(contacts)
        }
    }

    private fun initListeners() {
        with(binding){
            btnAddContact.setOnClickListener {
                openDialog()
            }
        }
    }

    private fun initAdapter() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        with(binding){
            recyclerview.layoutManager = layoutManager
            recyclerview.adapter = adapter
        }
    }

    private fun openDialog() {
        parentFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.activity_root, AddContactFragment())
            .addToBackStack(null)
            .commit()

    }

}