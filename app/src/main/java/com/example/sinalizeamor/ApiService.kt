// Serve para dize ao kotlin onde está o PHP

package com.example.sinalizeamor

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Header
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.Part
import com.example.sinalizeamor.ui.viewmodel.ExclusaoPublicacaoResponse

interface ApiService {

    // Diz para o Retroft enviar os dados usaando POST.
    // Login
    @FormUrlEncoded

    @POST("tcc%20(2)/tcc/control/loginApp.php")

    suspend fun fazerLogin(
        @Field("email") email: String,
        @Field("senha") senha: String
    ) : LoginResponse

    // Cadastro de usuário
    @FormUrlEncoded
    @POST("tcc%20(2)/tcc/control/cadastroApp.php")
    suspend fun fazerCadastro(
        @Field("nome") nome: String,
        @Field("sobrenome") sobrenome: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("senha") senha: String
    ): CadastroResponse

    @FormUrlEncoded
    @POST("tcc%20(2)/tcc/control/curtirApp.php")
    suspend fun curtirPublicacao(
        @Field("token") token: String,
        @Field("id_post") idPost: Int
    ): CurtidaResponse

    // Publicar um comentário.
    @FormUrlEncoded
    @POST("tcc%20(2)/tcc/control/comentarioApp.php")
    suspend fun enviarComentario(
        @Header("Authorization") token: String,
        @Field("id_post") idPost: Int,
        @Field("comentario") comentario: String
    ): ComentarioResposta

    @Multipart
    @POST("tcc%20(2)/tcc/control/publicarApp.php")
    suspend fun criarPublicacao(
        @Header("Authorization") token: String,
        @Part("texto") texto: RequestBody,
        @Part imagem: MultipartBody.Part?
    ): PublicarResponse

    @FormUrlEncoded
    @POST("tcc%20(2)/tcc/control/excluirPublicacaoApp.php")
    suspend fun excluirPublicacao(
        @Header("Authorization") token: String,
        @Field("id_post") idPost: Int
    ): ExclusaoPublicacaoResponse

    // Busca os cursos cadastrados no banco de dados.
    @GET("tcc%20(2)/tcc/control/cursosApp.php")
    suspend fun buscarCursos(): CursoResponse

    // Busca os modulos de um curso especifico.
    @GET("tcc%20(2)/tcc/control/modulosApp.php")
    suspend fun buscarModulos(@Query("id_curso") idCurso: Int): ModuloResponse

    // Busca as aulas de modulo especifico.
    @GET("tcc%20(2)/tcc/control/aulasApp.php")
    suspend fun buscarAulas(@Query("id_modulo") idModulo: Int): AulaResponse

    @GET("tcc%20(2)/tcc/control/comunidadeApp.php")
    suspend fun buscarPublicacoes(
        @Header("Authorization") token: String
    ): ComunidadeResponse

    // Buscar comentários de uma publicação.
    @GET("tcc%20(2)/tcc/control/comentarioApp.php")
    suspend fun buscarComentarios(
        @Header("Authorization") token: String,
        @Query("id_post") idPost: Int
    ): ComentariosResponse

    @Multipart
    @POST("tcc%20(2)/tcc/control/trocarFotoApp.php")
    suspend fun trocarFotoPerfil(
        @Header("Authorization") token: String,
        @Part foto: MultipartBody.Part
    ): FotoPerfilResponse

}