package com.example.sinalizeamor

// Resposta simples do PHP (usada em denúncia e conclusão de aula).
data class RespostaSimples(
    val sucesso: Boolean,
    val mensagem: String
)
