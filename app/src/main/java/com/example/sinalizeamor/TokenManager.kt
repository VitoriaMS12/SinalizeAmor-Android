package com.example.sinalizeamor

import android.content.Context
import com.google.gson.Gson

object TokenManager {

    private const val PREFS_NAME = "sinalize_amor_prefs"
    private const val TOKEN_KEY = "token_autenticacao"
    private const val ID_USUARIO_KEY = "id_usuario"
    private const val USUARIO_KEY = "usuario_json"

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

    // Salva os dados do usuário (para continuar logado ao reabrir o app).
    fun salvarUsuario(context: Context, usuario: Usuario) {
        val preferencias = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        preferencias.edit()
            .putString(USUARIO_KEY, Gson().toJson(usuario))
            .apply()
    }

    // Recupera os dados do usuário salvos. Retorna null se não houver.
    fun obterUsuario(context: Context): Usuario? {
        val preferencias = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val json = preferencias.getString(USUARIO_KEY, null) ?: return null

        return try {
            Gson().fromJson(json, Usuario::class.java)
        } catch (e: Exception) {
            null
        }
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
            .remove(USUARIO_KEY)
            .apply()
    }

}