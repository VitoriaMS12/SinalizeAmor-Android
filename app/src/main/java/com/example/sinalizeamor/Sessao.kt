package com.example.sinalizeamor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Avisa o app quando o servidor recusa o token (sessão expirada ou inválida).
object Sessao {
    var expirada by mutableStateOf(false)
}
