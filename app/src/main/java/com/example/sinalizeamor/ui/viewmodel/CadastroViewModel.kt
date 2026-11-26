package com.example.sinalizeamor.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sinalizeamor.Usuario
import com.example.sinalizeamor.ValidadorEmail
import com.example.sinalizeamor.data.repository.LoginRepository
import com.example.sinalizeamor.data.repository.CadastroRepository
import kotlinx.coroutines.launch

data class CadastroUiState(
    val nome: String = "",
    val sobrenome: String = "",
    val username: String = "",
    val email: String = "",
    val senha: String = "",
    val mostrarSenha: Boolean = false,
    val mensagem: String = "",
    val erroEmail: Boolean = false,
    val carregando: Boolean = false
)

class CadastroViewModel : ViewModel() {

    private val repository = CadastroRepository()

    var uiState = mutableStateOf(CadastroUiState())
        private set

    fun atualizarNome(valor: String) {
        uiState.value = uiState.value.copy(nome = valor)
    }

    fun atualizarSobrenome(valor: String) {
        uiState.value = uiState.value.copy(sobrenome = valor)
    }

    fun atualizarUsername(valor: String) {
        uiState.value = uiState.value.copy(username = valor)
    }

    fun atualizarEmail(valor: String) {
        uiState.value = uiState.value.copy(
            email = valor,
            erroEmail = false,
            mensagem = ""
        )
    }

    fun atualizarSenha(valor: String) {
        uiState.value = uiState.value.copy(
            senha = valor,
            mensagem = ""
        )
    }

    fun alternarVisibilidadeSenha() {
        uiState.value = uiState.value.copy(
            mostrarSenha = !uiState.value.mostrarSenha
        )
    }

    // Volta os campos ao estado inicial.
    fun limpar() {
        uiState.value = CadastroUiState()
    }

    // onConcluido recebe o usuário e o token se o login automático funcionar;
    // se não funcionar, recebe null e o app leva a pessoa para a tela de login.
    fun fazerCadastro(onConcluido: (Usuario?, String?) -> Unit) {
        val estado = uiState.value

        if (
            estado.nome.isBlank() ||
            estado.sobrenome.isBlank() ||
            estado.username.isBlank() ||
            estado.email.isBlank() ||
            estado.senha.isBlank()
        ) {
            uiState.value = estado.copy(
                mensagem = "Preencha todos os campos.",
                erroEmail = false
            )
            return
        }

        if (!ValidadorEmail.eValido(estado.email)) {
            uiState.value = estado.copy(
                erroEmail = true,
                mensagem = ""
            )
            return
        }

        viewModelScope.launch {
            uiState.value = uiState.value.copy(
                mensagem = "",
                erroEmail = false,
                carregando = true
            )

            try {
                val resposta = repository.fazerCadastro(
                    nome = estado.nome.trim(),
                    sobrenome = estado.sobrenome.trim(),
                    username = estado.username.trim(),
                    email = estado.email.trim().lowercase(),
                    senha = estado.senha
                )

                if (resposta.sucesso) {
                    var usuario: Usuario? = null
                    var token: String? = null

                    // Entra automaticamente com a conta recém-criada.
                    try {
                        val login = LoginRepository().fazerLogin(
                            email = estado.email.trim().lowercase(),
                            senha = estado.senha
                        )

                        if (login.sucesso) {
                            usuario = login.usuario
                            token = login.token
                        }
                    } catch (e: Exception) {
                        // Se falhar, a pessoa será levada para a tela de login.
                    }

                    uiState.value = CadastroUiState()
                    onConcluido(usuario, token)
                } else {
                    uiState.value = uiState.value.copy(
                        mensagem = resposta.mensagem
                    )
                }
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(
                    mensagem = "Não foi possível concluir o cadastro. Verifique a conexão e tente novamente."
                )
            } finally {
                uiState.value = uiState.value.copy(
                    carregando = false
                )
            }
        }
    }
}