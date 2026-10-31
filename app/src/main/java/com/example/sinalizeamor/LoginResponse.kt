// Serve para representar a resposata do PHP.

package com.example.sinalizeamor

data class LoginResponse(
    val sucesso: Boolean,
    val mensagem: String,
    val usuario: Usuario?,
    val token: String? = null
)

data class Usuario(
    val id_usuario: Int,
    val nome: String,
    val sobrenome: String,
    val username: String,
    val email: String,
    val foto_perfil: String?
)