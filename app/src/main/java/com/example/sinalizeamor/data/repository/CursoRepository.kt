package com.example.sinalizeamor.data.repository

import com.example.sinalizeamor.CursoResponse
import com.example.sinalizeamor.RetrofitClient


class CursoRepository {

    // Busca os cursos na API PHP.
    suspend fun buscarCursos(): CursoResponse {
        return RetrofitClient.api.buscarCursos()
    }
}