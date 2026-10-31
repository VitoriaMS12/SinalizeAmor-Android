package com.example.sinalizeamor

// Resposta enviada pelo PHP.
data class AulaResponse(
    val sucesso: Boolean,
    val mensagem: String,
    val aulas: List<Aula>
)

// Representa uma aula do banco de dados.
data class Aula(
    val id_aula: Int,
    val id_modulo: Int,
    val titulo: String,
    val descricao: String,
    val conteudo: String?,
    val video: String?,
    val imagem: String?,
    val duracao_minutos: Int?,
    val ordem: Int,
    val data_criacao: String?,
    val data_atualizacao: String?,
    val materiais: List<MaterialAula> = emptyList()
)

// Representa um material vinculado a uma aula.
data class MaterialAula(
    val id_material: Int,
    val titulo: String,
    val arquivo: String,
    val tipo: String,
    val ordem: Int
)