package com.example.sinalizeamor

data class PublicarResponse(
    val sucesso: Boolean,
    val mensagem: String,
    val id_post: Int? = null
)