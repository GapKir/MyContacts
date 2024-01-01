package com.example.mycontacts.ui

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mycontacts.databinding.FragmentAddContactBinding
import com.example.mycontacts.viewmodels.AddContactViewModel

class AddContactFragment : Fragment() {
    private lateinit var binding: FragmentAddContactBinding
    private val viewModel: AddContactViewModel by viewModels()

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { imageGalleryUri ->
        imageGalleryUri?.let {

            viewModel.copyImageToPath(it)

            with(binding) {
                image.visibility = View.VISIBLE
                image.setImageURI(it)
            }
        }
    }
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            viewModel.getContactsFromPhone()
            parentFragmentManager.popBackStack()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddContactBinding.inflate(layoutInflater, container, false)

        initListeners()

        return binding.root
    }

    private fun initListeners() {
        with(binding) {

            btnCloseDialog.setOnClickListener {
                parentFragmentManager.popBackStack()
            }

            btnConfirm.setOnClickListener {
                val name = etName.text.toString()
                val phone = etPhone.text.toString()

                if (viewModel.createContact(name, phone)) {
                    parentFragmentManager.popBackStack()
                }
            }

            btnAddPhoto.setOnClickListener {
                galleryLauncher.launch("image/*")
            }

            btnImportContacts.setOnClickListener {
                requestPermission.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

}