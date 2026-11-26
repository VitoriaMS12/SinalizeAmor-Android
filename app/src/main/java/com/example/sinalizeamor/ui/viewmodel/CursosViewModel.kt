package com.example.sinalizeamor.ui.viewmodel

import com.example.sinalizeamor.data.repository.ComunidadeRepository
import com.example.sinalizeamor.RetrofitClient
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sinalizeamor.Curso
import com.example.sinalizeamor.data.repository.CursoRepository
import kotlinx.coroutines.launch


class CursosViewModel : ViewModel(){

    private val repository = CursoRepository()

    var cursos by mutableStateOf<List<Curso>>(emptyList())
        private set

    var mensagem by mutableStateOf("Carregando cursos...")
        private set

    var carregando by mutableStateOf(true)
        private set

    // Nível do usuário (vem do servidor).
    var nivel by mutableStateOf<String?>(null)
        private set

    fun carregarCursos(token: String?) {
        viewModelScope.launch {
            carregando = true
            mensagem = "Carregando cursos..."

            try {
                val resposta = repository.buscarCursos(token)

                if (resposta.sucesso) {
                    // Mostra somente os cursos ativos (o admin pode desativar pelo site).
                    nivel = resposta.nivel
                    cursos = resposta.cursos.filter { it.ativo == 1 }
                    mensagem = if (cursos.isEmpty()) {
                        "Nenhum curso disponível."
                    } else {
                        ""
                    }
                } else {
                    mensagem = resposta.mensagem
                }
            } catch (e: Exception) {
                mensagem = "Não foi possível carregar os cursos."
            } finally {
                carregando = false
            }
        }
    }
}