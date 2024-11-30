package com.example.mvidecomposetest.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class AddContact(val componentContext: AddContactComponent) : Child
        class EditContact(val componentContext: EditContactComponent) : Child
        class ContactList(val componentContext: ContactListComponent) : Child
    }
}