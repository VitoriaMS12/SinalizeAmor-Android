package com.example.sinalizeamor

import com.google.gson.annotations.SerializedName

data class ComunidadeResponse(
    val sucesso: Boolean,
    val mensagem: String,
    val publicacoes: List<Publicacao> = emptyList()
)

data class Publicacao(
    @SerializedName("id_post")
    val idPost: Int,

    @SerializedName("id_usuario")
    val idUsuario: Int,
    val texto: String?,
    val privacidade: String,

    @SerializedName("data_postagem")
    val dataPostagem: String,
    val editado: Int,
    val nome: String,
    val sobrenome: String,
    val username: String,

    @SerializedName("foto_perfil")
    val fotoPerfil: String?,
    val midias: List<MidiaPublicacao> = emptyList(),

    @SerializedName("total_curtidas")
    val totalCurtidas: Int = 0,

    @SerializedName("total_comentarios")
    val totalComentarios: Int = 0,

    @SerializedName("curtido_pelo_usuario")
    val curtidoPeloUsuario: Boolean = false
)

data class MidiaPublicacao(
    @SerializedName("id_midia")
    val idMidia: Int,
    val arquivo: String,
    val tipo: String,
    val ordem: Int
)