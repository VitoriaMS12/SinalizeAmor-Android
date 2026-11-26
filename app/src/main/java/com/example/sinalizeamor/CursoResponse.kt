package com.example.sinalizeamor

// Resposta enviada pelo PHP.
data class CursoResponse(

    // Informa se a consulta deu certo.
    val sucesso: Boolean,

    // Mensagem enviada pelo PHP.
    val mensagem: String,

    // Nível do usuário (Iniciante, Intermediário ou Avançado).
    val nivel: String? = null,

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
    val data_criacao: String,

    // Progresso do usuário no curso.
    val total_aulas: Int = 0,
    val aulas_concluidas: Int = 0
)