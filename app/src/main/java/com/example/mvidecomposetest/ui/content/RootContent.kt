package com.example.mvidecomposetest.ui.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.example.mvidecomposetest.presentation.DefaultRootComponent
import com.example.mvidecomposetest.ui.theme.MviDecomposeTestTheme

@Composable
fun RootComponent(
    rootComponent: DefaultRootComponent,
) {
    MviDecomposeTestTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Children(
                stack = rootComponent.stack
            ) {
                when(val child = it.instance){
                    is DefaultRootComponent.Child.AddContact -> {
                        AddContact(child.componentContext)
                    }
                    is DefaultRootComponent.Child.ContactList -> {
                        Contacts(child.componentContext)
                    }
                    is DefaultRootComponent.Child.EditContact -> {
                        EditContact(child.componentContext)
                    }
                }
            }
        }
    }
}