package maria.incyberspace.contactslistapp

open class Contact(
    open val id: Int,
    open val name: String,
    open val surname: String,
    open val phoneNumber: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (name != (other as Contact).name) return false
        if (surname != other.surname) return false
        if (phoneNumber != other.phoneNumber) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + surname.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        return result
    }

}

class ContactNoCheckBox(
    override val id: Int,
    override val name: String,
    override val surname: String,
    override val phoneNumber: String,
) : Contact(id, name, surname, phoneNumber)

class ContactWithCheckBox(
    override val id: Int,
    override val name: String,
    override val surname: String,
    override val phoneNumber: String,
) : Contact(id, name, surname, phoneNumber)

