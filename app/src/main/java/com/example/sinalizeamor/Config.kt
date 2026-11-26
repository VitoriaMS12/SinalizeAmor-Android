package com.example.sinalizeamor

// Endereço do servidor (PC onde o Apache/PHP está rodando).
// Se o IP do computador mudar (ex.: outra rede Wi-Fi), troque SOMENTE aqui
// e também o IP em res/xml/network_security_config.xml.
object Config {
    const val HOST = "192.168.1.5"

    const val BASE_HTTPS = "https://$HOST/"
    const val BASE_HTTP = "http://$HOST/"

    // Pasta do site dentro do htdocs.
    const val PASTA_SITE = "tcc%20(2)/tcc/"
}
