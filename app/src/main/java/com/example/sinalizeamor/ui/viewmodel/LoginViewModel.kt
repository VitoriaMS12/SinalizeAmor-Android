package com.example.sinalizeamor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sinalizeamor.Usuario
import kotlinx.coroutines.launch
import com.example.sinalizeamor.data.repository.LoginRepository

data class LoginUiState(
    val email: String = "",
    val senha: String = "",
    val erroEmail: String = "",
    val mensagem: String = "",
    val carregando: Boolean = false,
    val mostrarSenha: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val repository = LoginRepository()

    var uiState = androidx.compose.runtime.mutableStateOf(LoginUiState())
        private set

    fun atualizarEmail(email: String) {
        uiState.value = uiState.value.copy(
            email = email,
            erroEmail = ""
        )
    }

    fun atualizarSenha(senha: String) {
        uiState.value = uiState.value.copy(senha = senha)
    }

    fun alternarVisibilidadeSenha() {
        uiState.value = uiState.value.copy(
            mostrarSenha = !uiState.value.mostrarSenha
        )
    }

    fun fazerLogin(onLoginSucesso: (Usuario, String) -> Unit) {
        val estado = uiState.value

        if (!estado.email.contains("@")) {
            uiState.value = estado.copy(
                erroEmail = "Digite um e-mail válido.",
                mensagem = ""
            )
            return
        }

        if (estado.senha.isBlank()) {
            uiState.value = estado.copy(
                erroEmail = "",
                mensagem = "Digite sua senha."
            )
            return
        }

        viewModelScope.launch {
            uiState.value = uiState.value.copy(
                erroEmail = "",
                mensagem = "",
                carregando = true
            )

            try {
                val resposta = repository.fazerLogin(
                    email = estado.email.trim(),
                    senha = estado.senha
                )

                uiState.value = uiState.value.copy(
                    mensagem = resposta.mensagem,
                    carregando = false
                )

                if (resposta.sucesso) {
                    val usuario = resposta.usuario
                    val token = resposta.token

                    if (usuario != null && token != null) {
                        onLoginSucesso(usuario, token)
                    } else {
                        uiState.value = uiState.value.copy(
                            mensagem = "Login realizado, mas não foi possível obter os dados de autenticação.",
                            carregando = false
                        )
                    }
                }
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(
                    mensagem = "Erro: ${e.message ?: "Falha na conexão."}",
                    carregando = false
                )
            }
        }
    }
}