package com.example.mvidecomposetest.presentation

import com.arkivanov.mvikotlin.core.store.Store

interface EditContactStore :
    Store<EditContactStore.Intent, EditContactStore.State, EditContactStore.Label> {

    data class State(
        val username: String,
        val phone: String,
        val id: Int,
    )

    // одноразовые действия, которые не меняют стейт экрана
    // в данном случае для навигации
    sealed interface Label {

        object ContactSaved : Label
    }

    sealed interface Intent {
        data class ChangeUsername(val username: String) : Intent
        data class ChangePhone(val phone: String) : Intent
        object SaveContact : Intent
    }
}