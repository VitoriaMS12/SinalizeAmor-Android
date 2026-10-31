package com.example.sinalizeamor.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sinalizeamor.Comentario
import com.example.sinalizeamor.Publicacao
import com.example.sinalizeamor.RetrofitClient
import com.example.sinalizeamor.TokenManager
import com.example.sinalizeamor.data.repository.ComunidadeRepository
import kotlinx.coroutines.launch
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class ComunidadeUiState(
    val carregando: Boolean = false,
    val publicacoes: List<Publicacao> = emptyList(),
    val mensagem: String? = null,
    val comentariosPorPost: Map<Int, List<Comentario>> = emptyMap(),
    val comentariosExpandidos: Set<Int> = emptySet(),
    val textosComentario: Map<Int, String> = emptyMap(),
    val enviandoComentario: Set<Int> = emptySet(),
    val textoNovaPublicacao: String = "",
    val publicando: Boolean = false,
    val mensagemPublicacao: String? = null,
    val publicacaoConcluida: Boolean = false,
    val imagemNovaPublicacao: Uri? = null
)

data class ExclusaoPublicacaoResponse(
    val sucesso: Boolean,
    val mensagem: String
)

class ComunidadeViewModel : ViewModel() {

    private val repository = ComunidadeRepository(RetrofitClient.api)

    var estado by mutableStateOf(ComunidadeUiState())
        private set

    fun carregarPublicacoes(context: Context) {
        Log.d("ComunidadeDebug", "carregarPublicacoes foi chamada!")

        viewModelScope.launch {
            estado = estado.copy(carregando = true)

            try {
                val token = TokenManager.obterToken(context)

                if (token.isNullOrBlank()) {
                    estado = estado.copy(
                        carregando = false,
                        mensagem = "Sessão não encontrada. Faça login novamente."
                    )
                    return@launch
                }

                val resposta = repository.buscarPublicacoes(token)

                estado = estado.copy(
                    carregando = false,
                    publicacoes = resposta.publicacoes
                )

            } catch (e: Exception) {
                Log.e("ComunidadeDebug", "Erro ao carregar publicações", e)

                estado = estado.copy(
                    carregando = false,
                    mensagem = "Erro ao carregar publicações: ${e.message}"
                )
            }
        }
    }

    fun curtirPublicacao(context: Context, idPost: Int) {
        viewModelScope.launch {
            try {
                val token = TokenManager.obterToken(context)

                if (token.isNullOrBlank()) {
                    estado = estado.copy(
                        mensagem = "Sua sessão expirou. Entre novamente."
                    )
                    return@launch
                }

                val resposta = repository.curtirPublicacao(token, idPost)

                if (resposta.sucesso) {
                    atualizarInteracoes(context)
                } else {
                    estado = estado.copy(mensagem = resposta.mensagem)
                }
            } catch (e: Exception) {
                Log.e("CurtidaDebug", "Erro ao registrar curtida", e)
                estado = estado.copy(
                    mensagem = "Não foi possível registrar a curtida."
                )
            }
        }
    }

    fun excluirPublicacao(context: Context, idPost: Int) {
        viewModelScope.launch {
            try {
                val token = TokenManager.obterToken(context)

                if (token.isNullOrBlank()) {
                    estado = estado.copy(
                        mensagem = "Sua sessão expirou. Entre novamente."
                    )
                    return@launch
                }

                val resposta = repository.excluirPublicacao(token, idPost)

                if (resposta.sucesso) {
                    estado = estado.copy(
                        publicacoes = estado.publicacoes.filter {
                            it.idPost != idPost
                        }
                    )
                } else {
                    estado = estado.copy(mensagem = resposta.mensagem)
                }
            } catch (e: Exception) {
                Log.e("ComunidadeDebug", "Erro ao excluir publicação", e)
                estado = estado.copy(
                    mensagem = "Não foi possível excluir a publicação."
                )
            }
        }
    }

    fun criarPublicacao(context: Context) {
        val texto = estado.textoNovaPublicacao.trim()
        val imagemUri = estado.imagemNovaPublicacao

        if (texto.isBlank() && imagemUri == null) {
            estado = estado.copy(
                mensagemPublicacao = "Escreva algo ou selecione uma imagem."
            )
            return
        }

        if (estado.publicando) return

        viewModelScope.launch {
            estado = estado.copy(
                publicando = true,
                mensagemPublicacao = null
            )

            var arquivoImagem: File? = null

            try {
                val token = TokenManager.obterToken(context)

                if (token.isNullOrBlank()) {
                    estado = estado.copy(
                        mensagemPublicacao = "Sua sessão expirou. Entre novamente."
                    )
                    return@launch
                }

                val textoRequest = texto.toRequestBody(
                    "text/plain; charset=utf-8".toMediaType()
                )

                val imagemPart = imagemUri?.let { uri ->
                    arquivoImagem = prepararImagemParaEnvio(context, uri)

                    val tipoMime = context.contentResolver.getType(uri)
                        ?: "application/octet-stream"

                    val arquivoRequest = arquivoImagem!!.asRequestBody(
                        tipoMime.toMediaType()
                    )

                    MultipartBody.Part.createFormData(
                        "imagem",
                        arquivoImagem!!.name,
                        arquivoRequest
                    )
                }

                val resposta = repository.criarPublicacao(
                    token = token,
                    texto = textoRequest,
                    imagem = imagemPart
                )

                if (resposta.sucesso) {
                    estado = estado.copy(
                        textoNovaPublicacao = "",
                        imagemNovaPublicacao = null,
                        mensagemPublicacao = resposta.mensagem,
                        publicacaoConcluida = true
                    )

                    atualizarInteracoes(context)
                } else {
                    estado = estado.copy(
                        mensagemPublicacao = resposta.mensagem
                    )
                }

            } catch (e: Exception) {
                Log.e("PublicacaoDebug", "Erro ao criar publicação", e)

                estado = estado.copy(
                    mensagemPublicacao =
                        "Não foi possível publicar. Verifique sua conexão e tente novamente."
                )

            } finally {
                arquivoImagem?.delete()

                estado = estado.copy(publicando = false)
            }
        }
    }

    fun alternarComentarios(context: Context, idPost: Int) {
        val expandidos = estado.comentariosExpandidos

        estado = estado.copy(
            comentariosExpandidos = if (idPost in expandidos) {
                expandidos - idPost
            } else {
                expandidos + idPost
            }
        )

        if (idPost !in expandidos) {
            carregarComentarios(context, idPost)
        }
    }

    fun carregarComentarios(context: Context, idPost: Int) {
        viewModelScope.launch {
            try {
                val token = TokenManager.obterToken(context)

                if (token.isNullOrBlank()) {
                    estado = estado.copy(
                        mensagem = "Sua sessão expirou. Entre novamente."
                    )
                    return@launch
                }

                val resposta = RetrofitClient.api.buscarComentarios(
                    token = token,
                    idPost = idPost
                )

                if (resposta.sucesso) {
                    estado = estado.copy(
                        comentariosPorPost = estado.comentariosPorPost +
                                (idPost to resposta.comentarios)
                    )
                } else {
                    estado = estado.copy(mensagem = resposta.mensagem)
                }
            } catch (e: Exception) {
                Log.e("ComentarioDebug", "Erro ao carregar comentários", e)
                estado = estado.copy(
                    mensagem = "Não foi possível carregar os comentários."
                )
            }
        }
    }

    fun atualizarTextoComentario(idPost: Int, texto: String) {
        estado = estado.copy(
            textosComentario = estado.textosComentario + (idPost to texto)
        )
    }

    fun enviarComentario(context: Context, idPost: Int) {
        val texto = estado.textosComentario[idPost]?.trim().orEmpty()

        if (texto.isBlank()) {
            estado = estado.copy(mensagem = "Digite um comentário.")
            return
        }

        if (idPost in estado.enviandoComentario) return

        viewModelScope.launch {
            estado = estado.copy(
                enviandoComentario = estado.enviandoComentario + idPost,
                mensagem = null
            )

            try {
                val token = TokenManager.obterToken(context)

                if (token.isNullOrBlank()) {
                    estado = estado.copy(
                        mensagem = "Sua sessão expirou. Entre novamente."
                    )
                    return@launch
                }

                val resposta = RetrofitClient.api.enviarComentario(
                    token = token,
                    idPost = idPost,
                    comentario = texto
                )

                if (resposta.sucesso) {
                    estado = estado.copy(
                        textosComentario = estado.textosComentario +
                                (idPost to ""),
                        comentariosExpandidos =
                            estado.comentariosExpandidos + idPost
                    )

                    carregarComentarios(context, idPost)
                    atualizarInteracoes(context)
                } else {
                    estado = estado.copy(mensagem = resposta.mensagem)
                }
            } catch (e: Exception) {
                Log.e("ComentarioDebug", "Erro ao enviar comentário", e)
                estado = estado.copy(
                    mensagem = "Não foi possível enviar o comentário."
                )
            } finally {
                estado = estado.copy(
                    enviandoComentario = estado.enviandoComentario - idPost
                )
            }
        }
    }

    fun atualizarTextoNovaPublicacao(texto: String) {
        estado = estado.copy(
            textoNovaPublicacao = texto,
            mensagemPublicacao = null
        )
    }

    fun atualizarImagemNovaPublicacao(uri: Uri?) {
        estado = estado.copy(
            imagemNovaPublicacao = uri,
            mensagemPublicacao = null
        )
    }

    private fun prepararImagemParaEnvio(
        context: Context,
        uri: Uri
    ): File {
        val arquivoTemporario = File.createTempFile(
            "publicacao_",
            ".jpg",
            context.cacheDir
        )

        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("Não foi possível abrir a imagem selecionada.")

        inputStream.use { entrada ->
            arquivoTemporario.outputStream().use { saida ->
                entrada.copyTo(saida)
            }
        }

        return arquivoTemporario
    }

    fun atualizarInteracoes(context: Context) {
        viewModelScope.launch {
            try {
                val token = TokenManager.obterToken(context) ?: return@launch
                if (token.isBlank()) return@launch

                val resposta = repository.buscarPublicacoes(token)

                if (resposta.sucesso) {
                    estado = estado.copy(
                        publicacoes = resposta.publicacoes
                    )
                }
            } catch (e: Exception) {
                Log.e("ComunidadeDebug", "Erro ao atualizar interações", e)
            }
        }
    }
}