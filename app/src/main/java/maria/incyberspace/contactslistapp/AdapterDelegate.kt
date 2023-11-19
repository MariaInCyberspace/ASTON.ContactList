package maria.incyberspace.contactslistapp

import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import maria.incyberspace.contactslistapp.databinding.ContactItemBinding
import maria.incyberspace.contactslistapp.databinding.ContactItemWithCheckboxBinding


fun contactItemAdapterDelegate(onItemClick: (ContactNoCheckBox) -> Unit) = adapterDelegateViewBinding<ContactNoCheckBox,
        Contact, ContactItemBinding>(
    { layoutInflater, root -> ContactItemBinding.inflate(layoutInflater, root, false) }
) {
    bind {
        binding.apply {
            contactId.text = item.id.toString()
            val fullName = "${item.name} ${item.surname}"
            contactItemName.text = fullName
            contactItemPhoneNumber.text = item.phoneNumber
            itemView.setOnClickListener { onItemClick(item) }
        }
    }
}

fun contactItemWithCheckBoxAdapterDelegate(onCheckedChange: (ContactWithCheckBox) -> Unit) = adapterDelegateViewBinding<ContactWithCheckBox,
        Contact, ContactItemWithCheckboxBinding>(
    { layoutInflater, parent -> ContactItemWithCheckboxBinding.inflate(layoutInflater, parent, false) }
) {
    bind {
        binding.apply {
            contactId.text = item.id.toString()
            val fullName = "${item.name} ${item.surname}"
            contactItemName.text = fullName
            contactItemPhoneNumber.text = item.phoneNumber
            toBeDeletedCheckBox.apply {
                isVisible = true
                isChecked = false
                setOnCheckedChangeListener {_, _ ->
                    onCheckedChange(item)
                }
            }
        }
    }
}