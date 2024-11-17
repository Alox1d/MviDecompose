package com.example.mvidecomposetest.presentation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.coroutines.flow.StateFlow

interface AddContactComponent {

    val model: StateFlow<Model>

    fun onUsernameChanged(username: String)

    fun onPhoneChanged(phone: String)

    fun onSaveContactClicked()

    @Parcelize
    data class Model(
        val username: String,
        val phone: String,
    ): Parcelable
}