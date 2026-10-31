package com.example.sinalizeamor

data class CurtidaResponse(
    val sucesso: Boolean,
    val mensagem: String,
    val curtido: Boolean? = null,
    val total_curtidas: Int? = null
)