package com.example.sinalizeamor

import com.google.gson.annotations.SerializedName

data class ComentariosResponse(
    val sucesso: Boolean,
    val mensagem: String,
    val comentarios: List<Comentario> = emptyList()
)

data class ComentarioResposta(
    val sucesso: Boolean,
    val mensagem: String,
    @SerializedName("id_comentario")
    val idComentario: Int? = null
)

data class Comentario(
    @SerializedName("id_comentario")
    val idComentario: Int,

    @SerializedName("id_post")
    val idPost: Int,

    @SerializedName("id_usuario")
    val idUsuario: Int,

    val comentario: String,
    val editado: Int,

    @SerializedName("data_comentario")
    val dataComentario: String,

    val nome: String,
    val sobrenome: String,
    val username: String,

    @SerializedName("foto_perfil")
    val fotoPerfil: String?,

    // Respostas feitas a este comentário.
    val respostas: List<RespostaComentario>? = emptyList()
)

data class RespostaComentario(
    @SerializedName("id_resposta")
    val idResposta: Int,

    @SerializedName("id_comentario")
    val idComentario: Int,

    @SerializedName("id_usuario")
    val idUsuario: Int,

    val resposta: String,

    @SerializedName("data_resposta")
    val dataResposta: String,

    val nome: String,
    val sobrenome: String,
    val username: String,

    @SerializedName("foto_perfil")
    val fotoPerfil: String?
)