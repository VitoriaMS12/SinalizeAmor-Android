package com.example.sinalizeamor

// Resposta enviada pelo PHP.
data class ModuloResponse(
    val sucesso: Boolean,
    val mensagem: String,
    val modulos: List<Modulo>
)

// Representa um módulo do banco de dados.
data class Modulo(
    val id_modulo: Int,
    val id_curso: Int,
    val titulo: String,
    val descricao: String,
    val ordem: Int
)