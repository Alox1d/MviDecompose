package com.example.mvidecomposetest.presentation

import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.AddContactUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DefaultAddContactComponent : AddContactComponent {

    // для упрощения без DI
    private val repository = RepositoryImpl
    private val addContact = AddContactUseCase(
        repository = repository
    )

    private val _model = MutableStateFlow(AddContactComponent.Model(
        username = "",
        phone = ""
    ))
    override val model: StateFlow<AddContactComponent.Model>
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
        addContact(
            username = username,
            phone = phone
        )
    }
}