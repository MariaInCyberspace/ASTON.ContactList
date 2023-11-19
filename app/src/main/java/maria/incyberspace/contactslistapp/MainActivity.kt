package maria.incyberspace.contactslistapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import maria.incyberspace.contactslistapp.Constants.ADD_EDIT
import maria.incyberspace.contactslistapp.Constants.CHANGE
import maria.incyberspace.contactslistapp.Constants.ID
import maria.incyberspace.contactslistapp.Constants.NEW_CONTACT
import maria.incyberspace.contactslistapp.MainAdapter.Companion.getItemTouchCallback
import maria.incyberspace.contactslistapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val chosen = ArrayList<ContactWithCheckBox>()
    private val itemTouchCallback = getItemTouchCallback()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAdapter()
        setButtonsOnClickListeners()
    }

    private fun setAdapter() {
        adapter = MainAdapter { onItemClick(it) }
        binding.recyclerViewContacts.apply {
            adapter = this@MainActivity.adapter
            (adapter as MainAdapter).apply {
                items = ContactsSource.getContacts().map { ContactNoCheckBox(it.id, it.name, it.surname, it.phoneNumber) }
            }
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }

    private fun setButtonsOnClickListeners() {
        binding.apply {
            binButton.setOnClickListener { showCheckBoxes() }
            undoSelectButton.setOnClickListener { undoOrCancel() }
            cancelSelectButton.setOnClickListener { undoOrCancel() }
            deleteSelectedButton.setOnClickListener { deleteSelected() }
            newContactButton.setOnClickListener { startFragment(addContact = true) }
        }
    }

    private fun onItemClick(contact: Contact, addContact: Boolean = false) {
        if (contact is ContactWithCheckBox) {
            onCheckedChanged(contact)
        } else {
            startFragment(contact, addContact)
        }
    }

    private fun startFragment(c: Contact? = null, addContact: Boolean = false) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            val bundle = if (addContact) bundleOf(NEW_CONTACT to true) else bundleOf(ID to c?.id)
            add<ContactDetailsFragment>(R.id.fragment_container_view, args = bundle)
            addToBackStack(null)
        }
        supportFragmentManager.setFragmentResultListener(CHANGE, this) { _, bundle ->
            val resultId = bundle.getInt(ADD_EDIT)
            val index = adapter.updateRecyclerView(ContactsSource.getContacts(), resultId, false)
            binding.recyclerViewContacts.smoothScrollToPosition(index)
        }
    }


    private fun setButtonVisibility(binBtn: Boolean, undoSelectBtn: Boolean, cancelBtn: Boolean, deleteBtn: Boolean, newContBtn: Boolean) {
        binding.binButton.isVisible = binBtn
        binding.undoSelectButton.isVisible = undoSelectBtn
        binding.cancelSelectButton.isVisible = cancelBtn
        binding.deleteSelectedButton.isVisible = deleteBtn
        binding.newContactButton.isVisible = newContBtn
    }


    private fun showCheckBoxes() {
        setButtonVisibility(binBtn = false, undoSelectBtn = true, cancelBtn = true, deleteBtn = true, newContBtn = false)
        adapter.update(true, ContactsSource.getContacts())
    }

    private fun deleteSelected() {
        setButtonVisibility(binBtn = true, undoSelectBtn = false, cancelBtn = false, deleteBtn = false, newContBtn = true)
        val contacts = ContactsSource.getContacts().apply { removeAll(chosen.toSet()) }
        ContactsSource.updateContacts(contacts.associateBy { it.id } as HashMap<Int, Contact>)
        adapter.update(false, contacts)
        chosen.clear()
    }

    private fun undoOrCancel() {
        setButtonVisibility(binBtn = true, undoSelectBtn = false, cancelBtn = false, deleteBtn = false, newContBtn = true)
        adapter.update(false, ContactsSource.getContacts())
        chosen.clear()
    }

    private fun onCheckedChanged(c: Contact) {
        chosen += ContactWithCheckBox(c.id, c.name, c.surname, c.phoneNumber)
    }

}