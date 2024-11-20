package com.example.mvidecomposetest.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.statekeeper.consume
import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.AddContactUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DefaultAddContactComponent(
    componentContext: ComponentContext,
    val onContactSaved: () -> Unit,
) : AddContactComponent, ComponentContext by componentContext {

    // для упрощения без DI
    private val repository = RepositoryImpl
    private val addContact = AddContactUseCase(
        repository = repository
    )
    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY) ?: AddContactComponent.Model(
            username = "",
            phone = ""
        )
    )

    init {
        stateKeeper.register(KEY){
            _model.value
        }
    }

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
        onContactSaved()
    }

    companion object {
        private const val KEY = "DefaultAddContactComponent"
    }
}