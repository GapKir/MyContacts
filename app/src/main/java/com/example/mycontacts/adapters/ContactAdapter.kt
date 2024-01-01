package com.example.mycontacts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mycontacts.R
import com.example.mycontacts.databinding.ItemContactBinding
import com.example.mycontacts.model.Contact

class ContactAdapter: RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var contacts: List<Contact> = emptyList()

    fun changedContacts(newContacts: List<Contact>){
        val diffCallback = DiffUtil.calculateDiff(ContactDiffCallback(contacts, newContacts))
        contacts = newContacts
        diffCallback.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(inflater, parent, false)

        return ContactViewHolder(binding)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
    }

    class ContactViewHolder(
        private val binding: ItemContactBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(contact: Contact){
            binding.apply {
                contactName.text = contact.name
                contactPhone.text = contact.phone
                contact.photo?.let { contactImage.setImageURI(it.toUri()) } ?: contactImage.setImageResource(
                    R.drawable.ic_launcher_foreground
                )
            }
        }
    }

    private class ContactDiffCallback(
        private val oldList: List<Contact>,
        private val newList: List<Contact>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}