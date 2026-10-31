package com.example.sinalizeamor.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sinalizeamor.Aula
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

    fun carregarAulas(idModulo: Int) {
        viewModelScope.launch {
            carregando = true
            mensagem = ""

            try {
                val resposta = repository.buscarAulas(idModulo)

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