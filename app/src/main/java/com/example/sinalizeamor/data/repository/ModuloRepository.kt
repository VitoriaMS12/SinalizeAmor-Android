package com.example.sinalizeamor.data.repository
// Acessa a API

import com.example.sinalizeamor.ModuloResponse
import com.example.sinalizeamor.RetrofitClient

class ModuloRepository { // centraliza o acesso aos módulos

    suspend fun buscarModulos(idCurso: Int): ModuloResponse {  // recebe o ID do curso selecionado.
        return RetrofitClient.api.buscarModulos(idCurso) // faz a chamada ao PHP e devolve a resposta.
    }
}