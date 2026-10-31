package com.example.sinalizeamor.data.repository

import com.example.sinalizeamor.AulaResponse
import com.example.sinalizeamor.RetrofitClient

class AulaRepository {

    suspend fun buscarAulas(idModulo: Int): AulaResponse {
        return RetrofitClient.api.buscarAulas(idModulo)
    }
}