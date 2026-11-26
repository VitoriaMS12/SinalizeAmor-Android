package com.example.sinalizeamor.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sinalizeamor.Aula
import com.example.sinalizeamor.RetrofitClient
import com.example.sinalizeamor.data.repository.AulaRepository
import kotlinx.coroutines.launch

class AulasViewModel : ViewModel() {

    private val repository = AulaRepository()

    var aulas by mutableStateOf<List<Aula>>(emptyList())
        private set

    var mensagem by mutableStateOf("")
        private set

    var carregando by mutableStateOf(true)
        private set

    // Aula que está sendo marcada como concluída agora.
    var aulaConcluindo by mutableStateOf<Int?>(null)
        private set

    // Erro ao concluir uma aula: (id da aula, mensagem).
    var erroConclusao by mutableStateOf<Pair<Int, String>?>(null)
        private set

    fun concluirAula(token: String?, idAula: Int) {
        if (token.isNullOrBlank()) {
            erroConclusao = idAula to "Sua sessão expirou. Entre novamente."
            return
        }

        if (aulaConcluindo != null) return

        viewModelScope.launch {
            aulaConcluindo = idAula
            erroConclusao = null

            try {
                val resposta = RetrofitClient.api.concluirAula(token, idAula)

                if (resposta.sucesso) {
                    aulas = aulas.map {
                        if (it.id_aula == idAula) it.copy(concluida = 1) else it
                    }
                } else {
                    erroConclusao = idAula to resposta.mensagem
                }
            } catch (e: Exception) {
                erroConclusao = idAula to "Não foi possível concluir a aula."
            } finally {
                aulaConcluindo = null
            }
        }
    }

    fun carregarAulas(idModulo: Int, token: String?) {
        viewModelScope.launch {
            carregando = true
            mensagem = ""

            try {
                val resposta = repository.buscarAulas(idModulo, token)

                if (resposta.sucesso) {
                    aulas = resposta.aulas

                    if (aulas.isEmpty()) {
                        mensagem = "Nenhuma aula disponível."
                    }
                } else {
                    mensagem = resposta.mensagem
                }
            } catch (e: Exception) {
                mensagem = "Não foi possível carregar as aulas."
            } finally {
                carregando = false
            }
        }
    }
}