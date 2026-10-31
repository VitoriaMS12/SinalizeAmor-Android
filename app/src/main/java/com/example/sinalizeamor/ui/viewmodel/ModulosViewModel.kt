package com.example.sinalizeamor.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sinalizeamor.Modulo
import com.example.sinalizeamor.data.repository.ModuloRepository
import kotlinx.coroutines.launch

class ModulosViewModel : ViewModel() {

    private val repository = ModuloRepository()

    var modulos by mutableStateOf<List<Modulo>>(emptyList())
        private set

    var mensagem by mutableStateOf("")
        private set

    var carregando by mutableStateOf(true)
        private set

    fun carregarModulos(idCurso: Int) {
        viewModelScope.launch {
            carregando = true
            mensagem = ""

            try {
                val resposta = repository.buscarModulos(idCurso)

                if (resposta.sucesso) {
                    modulos = resposta.modulos

                    if (modulos.isEmpty()) {
                        mensagem = "Nenhum módulo disponível."
                    }
                } else {
                    mensagem = resposta.mensagem
                }
            } catch (e: Exception) {
                mensagem = "Não foi possível carregar os módulos."
            } finally {
                carregando = false
            }
        }
    }
}