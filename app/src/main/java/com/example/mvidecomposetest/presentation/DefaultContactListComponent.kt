package com.example.mvidecomposetest.presentation

import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.GetContactsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DefaultContactListComponent(
    val onEditContactRequested: (Contact) -> Unit,
    val onAddContactRequested: () -> Unit,
) : ContactListComponent {

    private val repo = RepositoryImpl
    private val getContacts = GetContactsUseCase(repo)
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    override val model: StateFlow<ContactListComponent.Model> = getContacts()
        .map { ContactListComponent.Model(it) }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = ContactListComponent.Model(emptyList())
        )


    override fun onContactClicked(contact: Contact) {
        onEditContactRequested(contact)
    }

    override fun onAddContactClicked() {
        onAddContactRequested()
    }
}