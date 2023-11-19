package maria.incyberspace.contactslistapp

import com.github.javafaker.Faker

object ContactsSource {

    private val faker = Faker()
    private var contacts = HashMap<Int, Contact>()

    fun getContactsAsMap() : HashMap<Int, Contact> {
        if (contacts.isEmpty()) setContacts()
        return contacts
    }

    fun getContacts() = (getContactsAsMap().map { it.value }).toMutableList()

    fun updateContacts(updatedContacts: HashMap<Int, Contact>) {
        contacts = updatedContacts
    }

    fun addContact(c: Contact) {
        contacts[c.id] = c
    }

    fun editContact(c: Contact) {
        contacts[c.id] = c
    }

    private fun setContacts() {
        for (i in 1..100) {
            val (name, surname) = faker.name().name().split(" ")
            val phoneNumber = faker.phoneNumber().cellPhone()
            Contact(i, name, surname, phoneNumber).apply {
                contacts[this.id] = this
            }
        }
    }
}