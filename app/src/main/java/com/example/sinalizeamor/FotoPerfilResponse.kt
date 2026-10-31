package com.example.sinalizeamor

data class FotoPerfilResponse(
    val sucesso: Boolean,
    val mensagem: String,
    val foto_perfil: String? = null
)