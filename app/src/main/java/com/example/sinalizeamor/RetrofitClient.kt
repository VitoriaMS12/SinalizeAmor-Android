// Configura a conexão.

package com.example.sinalizeamor

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Endereço do PC onde o PHP está rodando.
    private const val BASE_URL = Config.BASE_HTTPS

    // Cliente que faz a comunicação pela internet/rede.
    val httpClient = OkHttpClient.Builder()

        // O certificado do Apache foi criado para "localhost",
        // mas estamos acessando o PC pelo IP 192.168.1.5.
        // Esta configuração permite essa conexão durante o desenvolvimento.
        .hostnameVerifier { _, _ -> true }

        // Se o servidor responder 401 (token inválido ou expirado),
        // avisa o app para voltar à tela de login.
        .addInterceptor(object : okhttp3.Interceptor {
            override fun intercept(
                chain: okhttp3.Interceptor.Chain
            ): okhttp3.Response {
                val resposta = chain.proceed(chain.request())

                if (resposta.code == 401) {
                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                        Sessao.expirada = true
                    }
                }

                return resposta
            }
        })
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