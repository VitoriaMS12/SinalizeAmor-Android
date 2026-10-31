package com.example.sinalizeamor.data.repository

import com.example.sinalizeamor.LoginResponse
import com.example.sinalizeamor.RetrofitClient

class LoginRepository {

    // Envia o e-mail e a senha para a API PHP.
    suspend fun fazerLogin(
        email: String,
        senha: String
    ): LoginResponse {
        return RetrofitClient.api.fazerLogin(
            email = email,
            senha = senha
        )
    }
}