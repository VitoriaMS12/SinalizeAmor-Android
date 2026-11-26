package com.example.sinalizeamor.data.repository

import com.example.sinalizeamor.AulaResponse
import com.example.sinalizeamor.RetrofitClient

class AulaRepository {

    suspend fun buscarAulas(idModulo: Int, token: String?): AulaResponse {
        return RetrofitClient.api.buscarAulas(idModulo, token)
    }
}