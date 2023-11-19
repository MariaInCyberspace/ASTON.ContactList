package maria.incyberspace.contactslistapp

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import java.util.Collections

class MainAdapter(onItemClickListener: (Contact) -> Unit) : ListDelegationAdapter<List<Contact>>() {

    init {
        delegatesManager
            .addDelegate(contactItemAdapterDelegate(onItemClickListener))
            .addDelegate(contactItemWithCheckBoxAdapterDelegate(onItemClickListener))
    }

    companion object {
        fun getItemTouchCallback() = object:
            ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.DOWN or ItemTouchHelper.UP
                        or ItemTouchHelper.START or ItemTouchHelper.END, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.bindingAdapterPosition
                val toPosition = target.bindingAdapterPosition
                val contacts = ContactsSource.getContacts()
                Collections.swap(contacts, fromPosition, toPosition)
                recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                ContactsSource.updateContacts(contacts.associateBy { it.id } as HashMap<Int, Contact>)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { }

        }
    }

    override fun setItems(items: List<Contact>?) {
        updateAdapter(items!!)
    }

    private fun updateAdapter(list: List<Contact>) {
        val oldList = this.items?.toMutableList() ?: mutableListOf()
        val newList = list.toMutableList()

        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldList[oldItemPosition].id == newList[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldList[oldItemPosition] == newList[newItemPosition]

            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newList.size
        })
        items = newList
        diff.dispatchUpdatesTo(this)
        notifyItemRangeChanged(0, itemCount)
    }

    fun update(addCheckBox: Boolean, contacts: MutableList<Contact>) {
        items = getContacts(addCheckBox, contacts)
    }

    fun updateRecyclerView(oldContacts: MutableList<Contact>, id: Int, addCheckBox: Boolean) : Int {
        var index: Int
        val contacts = getContacts(addCheckBox, oldContacts)
        contacts.apply {
            val m = this.associateBy { it.id }.toMap()
            val newContact = ContactsSource.getContactsAsMap()[id] as Contact
            if (m.containsKey(id)) {
                val oldContact = m[id]
                index = indexOf(oldContact)
                this[index] = newContact
            } else {
                this.add(newContact)
                index = this.size - 1
            }
            update(false, this)
            this@MainAdapter.notifyItemChanged(index)
            return index
        }
    }

    private fun getContacts(addCheckBox: Boolean, contacts: MutableList<Contact>) : ArrayList<Contact> {
        return if (addCheckBox) {
            ArrayList<Contact>().apply {
                contacts.forEach {
                    val c = ContactWithCheckBox(it.id, it.name, it.surname, it.phoneNumber)
                    this.add(c)
                }
            }
        } else {
            ArrayList<Contact>().apply {
                contacts.forEach {
                    val c = ContactNoCheckBox(it.id, it.name, it.surname, it.phoneNumber)
                    this.add(c)
                }
            }
        }
    }

}

