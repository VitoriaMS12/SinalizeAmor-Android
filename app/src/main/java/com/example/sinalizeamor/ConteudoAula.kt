package com.example.sinalizeamor

// Representa a tela de conteúdo de uma aula.
data class AulaSelecionada(
    val id_aula: Int,
    val titulo: String,
    val descricao: String,
    val conteudo: String?,
    val video: String?
)