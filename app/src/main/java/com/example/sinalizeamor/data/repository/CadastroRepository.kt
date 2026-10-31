package com.example.sinalizeamor.data.repository

import com.example.sinalizeamor.RetrofitClient

class CadastroRepository {

    // Envia os dados do novo usuário para a API PHP.
    suspend fun fazerCadastro(
        nome: String,
        sobrenome: String,
        username: String,
        email: String,
        senha: String
    ) = RetrofitClient.api.fazerCadastro(
        nome = nome,
        sobrenome = sobrenome,
        username = username,
        email = email,
        senha = senha
    )
}