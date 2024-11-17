package com.example.mvidecomposetest.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.example.mvidecomposetest.core.componentScope
import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.GetContactsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DefaultContactListComponent(
    componentContext: ComponentContext,
    val onEditContactRequested: (Contact) -> Unit,
    val onAddContactRequested: () -> Unit,
) : ContactListComponent, ComponentContext by componentContext {

    private val repo = RepositoryImpl
    private val getContacts = GetContactsUseCase(repo)
    private val coroutineScope = componentScope()

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