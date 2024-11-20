package com.example.mvidecomposetest.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.statekeeper.consume
import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.EditContactUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DefaultEditContactComponent(
    componentContext: ComponentContext,
    private val contact: Contact,
    private val onContactSaved: () -> Unit,
) : EditContactComponent, ComponentContext by componentContext {

    // для упрощения без DI
    private val repository = RepositoryImpl
    private val editContact = EditContactUseCase(
        repository = repository
    )

    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY) ?: EditContactComponent.Model(
            username = contact.username,
            phone = contact.phone,
        )
    )

    init {
        stateKeeper.register(KEY) {
            _model.value
        }
    }

    override val model: StateFlow<EditContactComponent.Model>
        get() = _model.asStateFlow()

    override fun onUsernameChanged(username: String) {
        _model.update {
            it.copy(
                username = username
            )
        }
    }

    override fun onPhoneChanged(phone: String) {
        _model.update {
            it.copy(
                phone = phone
            )
        }
    }

    override fun onSaveContactClicked() {
        val (username, phone) = _model.value
        editContact(
            contact.copy(
                username = username,
                phone = phone
            )
        )
        onContactSaved()
    }

    companion object {
        private const val KEY = "DefaultEditContactComponent"
    }
}