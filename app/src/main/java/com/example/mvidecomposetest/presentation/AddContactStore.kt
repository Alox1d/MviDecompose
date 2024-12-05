package com.example.mvidecomposetest.presentation

import android.provider.ContactsContract.CommonDataKinds.Phone
import com.arkivanov.mvikotlin.core.store.Store

interface AddContactStore :
    Store<AddContactStore.Intent, AddContactStore.State, AddContactStore.Label> {

    data class State(
        val username: String,
        val phone: String,
    )

    // одноразовые действий
    sealed interface Label {

        object ContactSaved : Label
    }

    sealed interface Intent {
        data class ChangeUsername(val username: String) : Intent
        data class ChangePhone(val phone: String) : Intent
        object SaveContact : Intent
    }
}