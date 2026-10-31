package com.example.sinalizeamor.data.repository

import com.example.sinalizeamor.ApiService
import com.example.sinalizeamor.ComunidadeResponse
import com.example.sinalizeamor.CurtidaResponse
import com.example.sinalizeamor.PublicarResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.example.sinalizeamor.ui.viewmodel.ExclusaoPublicacaoResponse

class ComunidadeRepository(
    private val apiService: ApiService
) {
    suspend fun buscarPublicacoes(
        token: String
    ): ComunidadeResponse {
        return apiService.buscarPublicacoes(
            token = token
        )
    }

    suspend fun curtirPublicacao(
        token: String,
        idPost: Int
    ): CurtidaResponse {
        return apiService.curtirPublicacao(
            token = token,
            idPost = idPost
        )
    }

    suspend fun criarPublicacao(
        token: String,
        texto: RequestBody,
        imagem: MultipartBody.Part?
    ): PublicarResponse {
        return apiService.criarPublicacao(
            token = token,
            texto = texto,
            imagem = imagem
        )
    }

    suspend fun excluirPublicacao(
        token: String,
        idPost: Int
    ): ExclusaoPublicacaoResponse {
        return apiService.excluirPublicacao(
            token = token,
            idPost = idPost
        )
    }
}