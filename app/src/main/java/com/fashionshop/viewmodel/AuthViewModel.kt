package com.fashionshop.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel: ViewModel() {
    private val _loggedIn = MutableStateFlow(false)
    val loggedIn: StateFlow<Boolean> = _loggedIn

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    fun login(email: String, pwd: String) {
        val e = email.trim().lowercase()
        // Simple demo logic: treat specific credentials as admin
        if (e == "admin@shop.com" && pwd == "admin") {
            _isAdmin.value = true
            _loggedIn.value = true
            return
        }
        // Always-accept demo user credentials
        if (e == "demo@shop.com" && pwd == "123456") {
            _isAdmin.value = false
            _loggedIn.value = true
            return
        }
        val ok = email.isNotBlank() && pwd.length >= 4
        _loggedIn.value = ok
        _isAdmin.value = false
    }

    fun loginWithGoogle() {
        // Simulate Google Sign-In success for demo purposes (non-admin)
        _isAdmin.value = false
        _loggedIn.value = true
    }

    fun loginAsGuest() {
        // Allow entering the app without an account (non-admin)
        _isAdmin.value = false
        _loggedIn.value = true
    }

    fun register(name: String, email: String, pwd: String) {
        _loggedIn.value = name.isNotBlank() && email.contains("@") && pwd.length >= 4
        _isAdmin.value = false
    }

    fun logout() {
        _loggedIn.value = false
        _isAdmin.value = false
    }
}
