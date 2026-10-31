package com.example.sinalizeamor

import android.content.Context

object TokenManager {

    private const val PREFS_NAME = "sinalize_amor_prefs"
    private const val TOKEN_KEY = "token_autenticacao"
    private const val ID_USUARIO_KEY = "id_usuario"

    // Salva o token recebido do servidor.
    fun salvarToken(context: Context, token: String) {
        val preferencias = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        preferencias.edit()
            .putString(TOKEN_KEY, token)
            .apply()
    }

    // Recupera o token salvo.
    fun obterToken(context: Context): String? {
        val preferencias = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        return preferencias.getString(TOKEN_KEY, null)
    }

    // Salva o ID do usuário conectado.
    fun salvarIdUsuario(context: Context, idUsuario: Int) {
        val preferencias = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        preferencias.edit()
            .putInt(ID_USUARIO_KEY, idUsuario)
            .apply()
    }

    // Recupera o ID do usuário conectado.
    fun obterIdUsuario(context: Context): Int {
        val preferencias = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        return preferencias.getInt(ID_USUARIO_KEY, -1)
    }

    // Remove o token e o ID ao sair da conta.
    fun removerToken(context: Context) {
        val preferencias = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        preferencias.edit()
            .remove(TOKEN_KEY)
            .remove(ID_USUARIO_KEY)
            .apply()
    }

}