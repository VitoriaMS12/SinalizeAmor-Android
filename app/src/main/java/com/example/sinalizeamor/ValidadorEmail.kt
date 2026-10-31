package com.example.sinalizeamor

import android.util.Patterns

object ValidadorEmail {

    fun eValido(email: String): Boolean {
        val valor = email.trim()

        if (valor.isEmpty() || valor.length > 254) {
            return false
        }

        if (valor.contains("..")) {
            return false
        }

        val partes = valor.split("@")

        if (partes.size != 2) {
            return false
        }

        val nome = partes[0]
        val dominio = partes[1]

        if (nome.isEmpty() || nome.length > 64) {
            return false
        }

        if (nome.startsWith(".") || nome.endsWith(".")) {
            return false
        }

        if (dominio.split(".").any {
                it.isEmpty() ||
                        it.startsWith("-") ||
                        it.endsWith("-")
            }) {
            return false
        }

        return Patterns.EMAIL_ADDRESS
            .matcher(valor)
            .matches()
    }
}