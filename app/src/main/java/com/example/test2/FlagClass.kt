package com.example.test2

import android.app.Application

class FlagClass : android.app.Application() {
    private var email: String? = null

    fun getEmail(): String? {
        return email
    }

    fun setEmail(id: String?) {
        this.email = id
    }
}