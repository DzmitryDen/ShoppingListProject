package com.hfad.shoppinglist.login_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
//    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _loginEvents = MutableSharedFlow<LoginEvent>()

    val loginEvents = _loginEvents.asSharedFlow()

    private fun sendEvent(event: LoginEvent) {
        viewModelScope.launch {
            _loginEvents.emit(event)
        }
    }

    fun signIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            sendEvent(LoginEvent.Error("Empty fields"))
            return
        }
        sendEvent(LoginEvent.Loading)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    sendEvent(
                        LoginEvent.LoginSuccess(
                            it.result.user?.email!!,
                            it.result.user?.uid!!
                        )
                    )
                }
            }
            .addOnFailureListener {
                sendEvent(LoginEvent.Error(it.message!!))
            }
    }

    fun signUp(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            sendEvent(LoginEvent.Error("Empty fields"))
            return
        }
        sendEvent(LoginEvent.Loading)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    sendEvent(
                        LoginEvent.LoginSuccess(
                            it.result.user?.email!!,
                            it.result.user?.uid!!
                        )
                    )
                }
            }
            .addOnFailureListener {
                sendEvent(LoginEvent.Error(it.message!!))
            }
    }

    fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    fun signOut() {
        auth.signOut()
    }
}

sealed class LoginEvent {
    data object Loading : LoginEvent()
    data class Error(val message: String) : LoginEvent()
    data class LoginSuccess(val email: String, val uid: String) : LoginEvent()

}