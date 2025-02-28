package com.example.mvidecomposetest.presentation

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.EditContactUseCase

class EditContactStoreFactory(

) {

    private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())
    private val editContactUseCase: EditContactUseCase = EditContactUseCase(RepositoryImpl)

    fun create(contact: Contact): EditContactStore = object : EditContactStore,
        Store<EditContactStore.Intent, EditContactStore.State, EditContactStore.Label> by storeFactory.create(
            name = "EditContactStore",
            initialState = EditContactStore.State(
                id = contact.id,
                username = contact.username,
                phone = contact.phone,
            ),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl,
        ) {}.apply {
        Log.d("STORE_FACTORY", "CREATED EditContact")
    }


    private sealed interface Action

    private sealed interface Msg {
        data class ChangeUsername(val username: String) : Msg
        data class ChangePhone(val phone: String) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<
            EditContactStore.Intent, Action, EditContactStore.State, Msg, EditContactStore.Label>() {

        override fun executeAction(action: Action, getState: () -> EditContactStore.State) {
            super.executeAction(action, getState)
        }

        override fun executeIntent(
            intent: EditContactStore.Intent,
            getState: () -> EditContactStore.State
        ) {
            when (intent) {
                is EditContactStore.Intent.ChangePhone -> {
                    dispatch(Msg.ChangePhone(intent.phone))
                }

                is EditContactStore.Intent.ChangeUsername -> {
                    dispatch(Msg.ChangeUsername(intent.username))
                }

                EditContactStore.Intent.SaveContact -> {
                    val state = getState()
                    val contact = Contact(
                        id = state.id,
                        username = state.username,
                        phone = state.phone
                    )
                    editContactUseCase(contact)
                    publish(EditContactStore.Label.ContactSaved)
                }
            }
        }
    }


    private object ReducerImpl : Reducer<EditContactStore.State, Msg> {

        override fun EditContactStore.State.reduce(msg: Msg): EditContactStore.State {
            return when (msg) {
                is Msg.ChangePhone -> {
                    copy(phone = msg.phone)
                }

                is Msg.ChangeUsername -> {
                    copy(username = msg.username)
                }
            }
        }
    }
}