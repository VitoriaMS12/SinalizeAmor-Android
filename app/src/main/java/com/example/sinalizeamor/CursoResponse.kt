package com.example.sinalizeamor

// Resposta enviada pelo PHP.
data class CursoResponse(

    // Informa se a consulta deu certo.
    val sucesso: Boolean,

    // Mensagem enviada pelo PHP.
    val mensagem: String,

    // Lista de cursos encontrados.
    val cursos: List<Curso>
)

// Representa um curso.
data class Curso(

    val id_curso: Int,
    val titulo: String,
    val descricao: String,
    val imagem: String?,
    val ativo: Int,
    val data_criacao: String
)