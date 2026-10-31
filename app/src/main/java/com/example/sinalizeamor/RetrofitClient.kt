// Configura a conexão.

package com.example.sinalizeamor

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Endereço do PC onde o PHP está rodando.
    private const val BASE_URL = "https://192.168.1.5/"

    // Cliente que faz a comunicação pela internet/rede.
    val httpClient = OkHttpClient.Builder()

        // O certificado do Apache foi criado para "localhost",
        // mas estamos acessando o PC pelo IP 192.168.1.5.
        // Esta configuração permite essa conexão durante o desenvolvimento.
        .hostnameVerifier { _, _ -> true }
        .build()

    // Cria a conexão com o PHP.
    val api: ApiService by lazy {

        Retrofit.Builder()

            // Define o endereço do servidor.
            .baseUrl(BASE_URL)

            // Usa o cliente HTTP que configuramos acima.
            .client(httpClient)

            // Converte o JSON do PHP para nossas classes Kotlin.
            .addConverterFactory(GsonConverterFactory.create())

            .build()

            // Cria a implementação do ApiService.
            .create(ApiService::class.java)
    }
}