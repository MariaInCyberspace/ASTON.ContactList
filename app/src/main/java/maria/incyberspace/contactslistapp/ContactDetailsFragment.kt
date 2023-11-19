package maria.incyberspace.contactslistapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import maria.incyberspace.contactslistapp.Constants.ADD_EDIT
import maria.incyberspace.contactslistapp.Constants.CHANGE
import maria.incyberspace.contactslistapp.Constants.ID
import maria.incyberspace.contactslistapp.Constants.NEW_CONTACT
import maria.incyberspace.contactslistapp.databinding.ContactDetailsBinding

class ContactDetailsFragment : Fragment(R.layout.contact_details) {

    private val binding get() = _binding!!
    private var _binding: ContactDetailsBinding? = null

    private var contactId = 0
    private var nextIndex = 0
    private var addContact = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ContactDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireArguments().getBoolean(NEW_CONTACT)) {
            setContactDetails(addContact = true)
            addContact = true
        } else {
            contactId = requireArguments().getInt(ID)
            setContactDetails(contactId)
            binding.contactDetailsTitle.setText(R.string.edit_contact_frag_tv)
            binding.addOrEditContactButton.setText(R.string.edit_contact_frag_tv)
        }

        binding.addOrEditContactButton.setOnClickListener {
            if (addContact) addContact(getContact())
            else editContact(getContact())
            goBack()
        }

        binding.cancelAddingContactButton.setOnClickListener {
            goBack()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun addContact(c: Contact) {
        ContactsSource.addContact(c)
        setFragmentResult(CHANGE, bundleOf(ADD_EDIT to c.id))
    }


    private fun editContact(c: Contact) {
        ContactsSource.editContact(c)
        setFragmentResult(CHANGE, bundleOf(ADD_EDIT to c.id))
    }

    private fun getContact() =
        Contact(
            if (addContact) nextIndex + 1 else contactId,
            binding.contactName.text.toString(),
            binding.contactSurname.text.toString(),
            binding.contactPhoneNumber.text.toString()
        )

    private fun setContactDetails(id: Int = 0, addContact: Boolean = false) {
        val contacts = ContactsSource.getContactsAsMap()
        nextIndex = contacts.keys.max()
        val contact = if (addContact) {
            Contact(nextIndex, "", "", "")
        } else {
            contacts[id]
        }
        with (contact) {
            binding.contactName.setText(this!!.name)
            binding.contactSurname.setText(surname)
            binding.contactPhoneNumber.setText(phoneNumber)
        }
    }

    private fun goBack() {
        this.parentFragmentManager.popBackStack()
    }
}