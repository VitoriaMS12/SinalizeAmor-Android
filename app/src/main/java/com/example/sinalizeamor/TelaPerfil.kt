package com.example.sinalizeamor

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.sinalizeamor.ui.theme.CoresSinalizeAmor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import androidx.compose.runtime.LaunchedEffect

@Composable
fun TelaPerfil(
    usuario: Usuario?,
    onVoltar: () -> Unit,
    onSair: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var fotoServidor by remember(usuario?.foto_perfil) {
        mutableStateOf(usuario?.foto_perfil)
    }

    var erroFotoServidor by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(fotoServidor) {
        erroFotoServidor = false
    }

    var fotoPerfil by remember(usuario?.foto_perfil) {
        mutableStateOf<Uri?>(null)
    }

    var enviandoFoto by remember { mutableStateOf(false) }
    var mensagemFoto by remember { mutableStateOf("") }

    val seletorImagem = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            fotoPerfil = uri
            mensagemFoto = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoresSinalizeAmor.Creme)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SINALIZE • AMOR",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = CoresSinalizeAmor.Bege
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Meu perfil",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = CoresSinalizeAmor.Marrom
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Suas informações pessoais",
            fontSize = 14.sp,
            color = CoresSinalizeAmor.TextoSecundario
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = CoresSinalizeAmor.FundoCartao
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(124.dp)
                        .border(
                            width = 3.dp,
                            color = CoresSinalizeAmor.Azul,
                            shape = CircleShape
                        )
                        .padding(5.dp)
                        .clip(CircleShape)
                        .background(CoresSinalizeAmor.AzulClaro),
                    contentAlignment = Alignment.Center
                ) {
                    if (fotoPerfil != null) {
                        // Mostra a foto escolhida no celular.
                        AsyncImage(
                            model = fotoPerfil,
                            contentDescription = "Prévia da foto de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else if (!fotoServidor.isNullOrBlank() && !erroFotoServidor) {
                        val urlFotoPerfil = if (
                            fotoServidor!!.startsWith("http://") ||
                            fotoServidor!!.startsWith("https://")
                        ) {
                            fotoServidor!!
                        } else {
                            "http://192.168.1.5/tcc%20(2)/tcc/uploads/perfils/$fotoServidor"
                        }

                        AsyncImage(
                            model = urlFotoPerfil,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            onError = { erroFotoServidor = true }
                        )
                    } else {
                        Text(
                            text = "👤",
                            fontSize = 52.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = usuario?.nome
                        ?.takeIf { it.isNotBlank() }
                        ?: "Estudante",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoresSinalizeAmor.Marrom
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "@${usuario?.username ?: "usuario"}",
                    fontSize = 15.sp,
                    color = CoresSinalizeAmor.TextoSecundario
                )

                Spacer(modifier = Modifier.height(18.dp))

                OutlinedButton(
                    onClick = {
                        if (!enviandoFoto) {
                            seletorImagem.launch("image/*")
                        }
                    },
                    enabled = !enviandoFoto,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "📷 Escolher foto",
                        fontWeight = FontWeight.SemiBold,
                        color = CoresSinalizeAmor.Marrom
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val imagemSelecionada = fotoPerfil
                        val token = TokenManager.obterToken(context)

                        if (imagemSelecionada == null) {
                            mensagemFoto = "Escolha uma foto primeiro."
                            return@Button
                        }

                        if (token.isNullOrBlank()) {
                            mensagemFoto =
                                "Sua sessão expirou. Entre novamente."
                            return@Button
                        }

                        scope.launch {
                            enviandoFoto = true
                            mensagemFoto = ""

                            try {
                                val resposta = withContext(Dispatchers.IO) {
                                    enviarFotoParaServidor(
                                        context = context,
                                        uri = imagemSelecionada,
                                        token = token
                                    )
                                }

                                if (resposta.sucesso) {
                                    mensagemFoto =
                                        resposta.mensagem
                                            .ifBlank {
                                                "Foto enviada com sucesso!"
                                            }

                                    // Atualiza o nome da foto retornado pelo PHP.
                                    fotoServidor = resposta.foto_perfil

                                    // Remove a prévia local para exibir a foto do servidor.
                                    fotoPerfil = null
                                } else {
                                    mensagemFoto =
                                        resposta.mensagem
                                            .ifBlank {
                                                "Não foi possível enviar a foto."
                                            }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()

                                mensagemFoto = when (e) {
                                    is retrofit2.HttpException ->
                                        "Erro HTTP ${e.code()}: ${e.response()?.errorBody()?.string() ?: e.message()}"

                                    else ->
                                        "Erro: ${e.javaClass.simpleName}: ${e.message}"
                                }
                            } finally {
                                enviandoFoto = false
                            }
                        }
                    },
                    enabled = !enviandoFoto,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CoresSinalizeAmor.Azul,
                        contentColor = Color(0xFF29434E)
                    )
                ) {
                    if (enviandoFoto) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Salvar foto",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (mensagemFoto.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = mensagemFoto,
                        color = CoresSinalizeAmor.TextoSecundario,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                androidx.compose.material3.HorizontalDivider(
                    color = Color(0xFFE8DED1)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Dados da conta",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoresSinalizeAmor.Marrom
                )

                Spacer(modifier = Modifier.height(16.dp))

                CampoPerfil(
                    titulo = "Nome",
                    valor = usuario?.nome
                        ?.takeIf { it.isNotBlank() }
                        ?: "Não informado"
                )

                Spacer(modifier = Modifier.height(12.dp))

                CampoPerfil(
                    titulo = "Nome de usuário",
                    valor = usuario?.username
                        ?.takeIf { it.isNotBlank() }
                        ?: "Não informado"
                )

                Spacer(modifier = Modifier.height(12.dp))

                CampoPerfil(
                    titulo = "E-mail",
                    valor = usuario?.email
                        ?.takeIf { it.isNotBlank() }
                        ?: "Não informado"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onVoltar,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CoresSinalizeAmor.Azul,
                contentColor = Color(0xFF29434E)
            )
        ) {
            Text(
                text = "Voltar ao início",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onSair,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Sair da conta",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = CoresSinalizeAmor.Marrom
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

private suspend fun enviarFotoParaServidor(
    context: Context,
    uri: Uri,
    token: String
): FotoPerfilResponse {
    val arquivoTemporario = withContext(Dispatchers.IO) {
        val arquivo = File.createTempFile(
            "foto_perfil_",
            ".img",
            context.cacheDir
        )

        try {
            val input = context.contentResolver.openInputStream(uri)
                ?: throw IOException("Não foi possível abrir a imagem.")

            input.use { origem ->
                arquivo.outputStream().use { destino ->
                    origem.copyTo(destino)
                }
            }

            arquivo
        } catch (e: Exception) {
            arquivo.delete()
            throw e
        }
    }

    try {
        val tipoMime = context.contentResolver.getType(uri)
            ?: "application/octet-stream"

        val corpoArquivo = arquivoTemporario.asRequestBody(
            tipoMime.toMediaTypeOrNull()
        )

        val parteFoto = MultipartBody.Part.createFormData(
            name = "foto",
            filename = "foto_perfil",
            body = corpoArquivo
        )

        return RetrofitClient.api.trocarFotoPerfil(
            token = "Bearer $token",
            foto = parteFoto
        )
    } finally {
        arquivoTemporario.delete()
    }
}

@Composable
private fun CampoPerfil(
    titulo: String,
    valor: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CoresSinalizeAmor.Creme)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = titulo,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = CoresSinalizeAmor.TextoSecundario
        )

        Text(
            text = valor,
            fontSize = 15.sp,
            color = CoresSinalizeAmor.Texto
        )
    }
}