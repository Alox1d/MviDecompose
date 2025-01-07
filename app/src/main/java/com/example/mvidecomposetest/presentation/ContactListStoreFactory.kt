package com.example.mvidecomposetest.presentation

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.GetContactsUseCase
import kotlinx.coroutines.launch

class ContactListStoreFactory(

) {

    private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())
    private val getContactsUseCase: GetContactsUseCase = GetContactsUseCase(RepositoryImpl)

    fun create(): ContactListStore = object : ContactListStore,
        Store<ContactListStore.Intent, ContactListStore.State, ContactListStore.Label> by storeFactory.create(
            name = "ContactListStore",
            initialState = ContactListStore.State(contactList = listOf()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl,
        ) {}.apply {
        Log.d("STORE_FACTORY", "CREATED ContactList")
    }

    private sealed interface Action {

        data class ContactsLoaded(val contacts: List<Contact>) : Action
    }

    private sealed interface Msg {
        data class ContactsLoaded(val contacts: List<Contact>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        // вызывается при создании стора
        override fun invoke() {
            scope.launch {
                getContactsUseCase().collect {
                    dispatch(Action.ContactsLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<ContactListStore.Intent, Action, ContactListStore.State, Msg, ContactListStore.Label>() {

        override fun executeAction(action: Action, getState: () -> ContactListStore.State) {
            when (action) {
                is Action.ContactsLoaded -> dispatch(Msg.ContactsLoaded(action.contacts))
            }
        }

        override fun executeIntent(
            intent: ContactListStore.Intent,
            getState: () -> ContactListStore.State
        ) {
            when (intent) {
                ContactListStore.Intent.AddContact -> {
                    publish(ContactListStore.Label.AddContact)
                }

                is ContactListStore.Intent.SelectContact -> {
                    publish(ContactListStore.Label.EditContact(intent.contact))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<ContactListStore.State, Msg> {
        override fun ContactListStore.State.reduce(msg: Msg) =
            when (msg) {
                is Msg.ContactsLoaded -> copy(contactList = msg.contacts)
            }
    }
}