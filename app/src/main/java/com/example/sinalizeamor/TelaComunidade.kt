package com.example.sinalizeamor

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sinalizeamor.ui.viewmodel.ComunidadeViewModel
import androidx.compose.foundation.layout.PaddingValues
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.heightIn
import android.net.Uri
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.TextButton
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import java.text.SimpleDateFormat
import java.util.Locale

// ==========================================
// PALETA OFICIAL — SINALIZE AMOR
// ==========================================

private val FundoComunidade = Color(0xFFF7F1E8)
private val AzulComunidade = Color(0xFFEAF7FC)
private val AzulPrincipal = Color(0xFF7EC8E3)
private val AzulHover = Color(0xFF68B8D8)

private val MarromComunidade = Color(0xFF6F5B47)
private val BegeComunidade = Color(0xFFA68A64)
private val BegeClaro = Color(0xFFCDB79E)

private val TextoPrincipal = Color(0xFF555555)
private val TextoSecundario = Color(0xFF777777)

private val BordaSuave = Color(0xFFD7D7D7)
private val AzulEscuroResposta = Color(0xFF397F9B)
private val Branco = Color(0xFFFFFFFF)
private val FundoCartao = Color(0xFFFFFDFB)

// Compatibilidade com os nomes usados no layout
private val RosaClaro = AzulComunidade
private val RosaEscuro = MarromComunidade
private val RosaPrincipal = AzulPrincipal

// ==========================================
// FOTO DE PERFIL
// ==========================================

@Composable
private fun FotoPerfilComunidade(
    fotoPerfil: String?,
    nome: String,
    tamanho: Int = 48
) {
    val baseUrl =
        Config.BASE_HTTP + Config.PASTA_SITE + "uploads/perfils/"

    val urlFoto = fotoPerfil
        ?.takeIf { it.isNotBlank() }
        ?.let { foto ->
            if (foto.startsWith("http://") ||
                foto.startsWith("https://")
            ) {
                foto
            } else {
                baseUrl + foto
            }
        }

    var imagemFalhou by remember(urlFoto) {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .size(tamanho.dp)
            .clip(CircleShape)
            .background(RosaClaro),
        contentAlignment = Alignment.Center
    ) {
        if (urlFoto != null && !imagemFalhou) {
            AsyncImage(
                model = urlFoto,
                contentDescription = "Foto de perfil de $nome",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                onError = { erro ->
                    imagemFalhou = true

                    Log.e(
                        "FotoPerfilComunidade",
                        "Erro ao carregar: $urlFoto",
                        erro.result.throwable
                    )
                }
            )
        }

        if (urlFoto == null || imagemFalhou) {
            Text(
                text = nome.firstOrNull()?.uppercase() ?: "?",
                color = RosaEscuro,
                fontSize = (tamanho / 2.2f).sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CartaoNovaPublicacao(
    texto: String,
    aoAlterarTexto: (String) -> Unit,
    aoPublicar: () -> Unit,
    publicando: Boolean,
    mensagem: String?,
    imagemSelecionada: Uri?,
    aoSelecionarImagem: () -> Unit,
    aoRemoverImagem: () -> Unit
) {
    val context = LocalContext.current

    val imagemBitmap = remember(imagemSelecionada) {
        imagemSelecionada?.let { uri ->
            try {
                context.contentResolver.openInputStream(uri)?.use { input ->
                    BitmapFactory.decodeStream(input)?.asImageBitmap()
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Branco
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Compartilhe algo com a comunidade ♡",
                color = MarromComunidade,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = texto,
                onValueChange = aoAlterarTexto,
                modifier = Modifier.fillMaxWidth(),
                enabled = !publicando,
                label = {
                    Text("Sua publicação")
                },
                placeholder = {
                    Text("O que você gostaria de compartilhar?")
                },
                minLines = 3,
                maxLines = 6,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulPrincipal,
                    unfocusedBorderColor = BordaSuave,
                    focusedLabelColor = MarromComunidade,
                    unfocusedLabelColor = TextoSecundario,
                    cursorColor = MarromComunidade
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (imagemSelecionada != null) {
                if (imagemBitmap != null) {
                    Image(
                        bitmap = imagemBitmap,
                        contentDescription = "Imagem selecionada para publicação",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 280.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Text(
                        text = "Não foi possível visualizar a imagem selecionada.",
                        color = MarromComunidade,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = aoRemoverImagem,
                    enabled = !publicando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Remover imagem")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedButton(
                onClick = aoSelecionarImagem,
                enabled = !publicando,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = if (imagemSelecionada == null) {
                        "Selecionar imagem"
                    } else {
                        "Trocar imagem"
                    }
                )
            }

            if (mensagem != null) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = mensagem,
                    color = MarromComunidade,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = aoPublicar,
                modifier = Modifier.fillMaxWidth(),
                enabled = !publicando &&
                        (texto.isNotBlank() || imagemSelecionada != null),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulPrincipal,
                    contentColor = MarromComunidade
                )
            ) {
                if (publicando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = MarromComunidade,
                        strokeWidth = 2.dp
                    )

                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Publicando...")
                } else {
                    Text(
                        text = "Publicar ♡",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

}

private fun formatarDataPublicacao(data: String): String {
    return try {
        val formatoEntrada = SimpleDateFormat(
            "yyyy-MM-dd HH:mm",
            Locale.getDefault()
        )

        formatoEntrada.isLenient = false

        val dataConvertida = formatoEntrada.parse(data)
            ?: return data

        val formatoSaida = SimpleDateFormat(
            "dd MMM yyyy • HH:mm",
            Locale("pt", "BR")
        )

        formatoSaida.format(dataConvertida)
    } catch (e: Exception) {
        data
    }

}


// ==========================================
// TELA DA COMUNIDADE
// ==========================================

@Composable
fun TelaComunidade(
    viewModel: ComunidadeViewModel = viewModel()
) {
    val estado = viewModel.estado
    val context = LocalContext.current
    var mostrarFormulario by remember { mutableStateOf(false) }

    // Botão voltar do celular fecha o formulário de nova publicação.
    androidx.activity.compose.BackHandler(enabled = mostrarFormulario) {
        mostrarFormulario = false
    }
    val estadoRolagem = rememberLazyListState()
    var publicacaoParaExcluir by remember { mutableStateOf<Int?>(null) }
    var publicacaoParaDenunciar by remember { mutableStateOf<Int?>(null) }
    var motivoDenuncia by remember { mutableStateOf("") }
    val idUsuarioLogado = TokenManager.obterIdUsuario(context)
    Log.d("ComunidadeDebug", "ID do usuário logado: $idUsuarioLogado")

    val seletorImagem = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            viewModel.atualizarImagemNovaPublicacao(uri)
        }
    }

    LaunchedEffect(estado.publicacaoConcluida) {
        if (estado.publicacaoConcluida) {
            mostrarFormulario = false
        }
    }

    LaunchedEffect(Unit) {
        Log.d("ComunidadeDebug", "Chamando carregamento das publicações")
        viewModel.carregarPublicacoes(context)
    }

    if (mostrarFormulario) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FundoComunidade)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FundoCartao)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { mostrarFormulario = false }
                ) {
                    Text(
                        text = "← Voltar",
                        color = MarromComunidade,
                        fontSize = 16.sp
                    )
                }

                Text(
                    text = "Nova publicação",
                    color = MarromComunidade,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(color = BegeClaro)

            CartaoNovaPublicacao(
                texto = estado.textoNovaPublicacao,
                aoAlterarTexto = { texto ->
                    viewModel.atualizarTextoNovaPublicacao(texto)
                },
                aoPublicar = {
                    viewModel.criarPublicacao(context)
                },
                publicando = estado.publicando,
                mensagem = estado.mensagemPublicacao,
                imagemSelecionada = estado.imagemNovaPublicacao,
                aoSelecionarImagem = {
                    seletorImagem.launch("image/*")
                },
                aoRemoverImagem = {
                    viewModel.atualizarImagemNovaPublicacao(null)
                }
            )
        }

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoComunidade)
    ) {

        // Cabeçalho
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(FundoCartao)
                .padding(
                    horizontal = 22.dp,
                    vertical = 20.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(17.dp))
                        .background(RosaClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "♡",
                        color = AzulPrincipal,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 13.dp)
                ) {
                    Text(
                        text = "Nossa comunidade",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MarromComunidade
                    )

                    Text(
                        text = "Sinalize Amor",
                        fontSize = 14.sp,
                        color = RosaEscuro,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Compartilhe experiências, faça amizades e conecte-se através da inclusão.",
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = TextoSecundario
            )
        }

        HorizontalDivider(
            color = BegeClaro.copy(alpha = 0.65f),
            thickness = 1.dp
        )


        // Área de criação e publicações
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    viewModel.atualizarTextoNovaPublicacao("")
                    viewModel.atualizarImagemNovaPublicacao(null)
                    mostrarFormulario = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 14.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulPrincipal,
                    contentColor = MarromComunidade
                )
            ) {
                Text(
                    text = "＋ Criar publicação",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))


            // Conteúdo principal
            when {
                estado.carregando -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = AzulPrincipal
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Preparando nossa comunidade...",
                                color = TextoSecundario,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                estado.mensagem != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Branco
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Ops!",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MarromComunidade
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = estado.mensagem
                                        ?: "Não foi possível carregar as publicações.",
                                    color = TextoSecundario,
                                    fontSize = 14.sp,
                                    lineHeight = 21.sp
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                Button(
                                    onClick = {
                                        viewModel.carregarPublicacoes(context)
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AzulPrincipal,
                                        contentColor = MarromComunidade
                                    )
                                ) {
                                    Text("Tentar novamente")
                                }
                            }
                        }
                    }
                }

                estado.publicacoes.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Branco
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "♡",
                                    fontSize = 54.sp,
                                    color = AzulPrincipal
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Um espaço esperando por você!",
                                    color = MarromComunidade,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Ainda não há publicações disponíveis. Volte em breve para conferir as novidades.",
                                    color = TextoSecundario,
                                    fontSize = 14.sp,
                                    lineHeight = 21.sp
                                )
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        state = estadoRolagem,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 16.dp,
                            vertical = 18.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        items(
                            items = estado.publicacoes,
                            key = { it.idPost }
                        ) { publicacao ->

                            // Cartão da publicação
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Branco
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 3.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(18.dp)
                                ) {

                                    // Cabeçalho da publicação
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        FotoPerfilComunidade(
                                            fotoPerfil = publicacao.fotoPerfil,
                                            nome = publicacao.nome,
                                            tamanho = 48
                                        )

                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = 12.dp)
                                        ) {
                                            Text(
                                                text = "${publicacao.nome} ${publicacao.sobrenome}",
                                                color = MarromComunidade,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp
                                            )

                                            Spacer(modifier = Modifier.height(3.dp))

                                            Text(
                                                text = "@${publicacao.username}",
                                                color = TextoSecundario,
                                                fontSize = 12.sp
                                            )
                                        }

                                        Surface(
                                            shape = CircleShape,
                                            color = AzulComunidade
                                        ) {
                                            Text(
                                                text = "♡",
                                                color = AzulPrincipal,
                                                fontSize = 22.sp,
                                                modifier = Modifier.padding(
                                                    horizontal = 10.dp,
                                                    vertical = 6.dp
                                                )
                                            )
                                        }
                                    }

                                    // Denúncia: disponível para publicações de outros usuários
                                    if (publicacao.idUsuario != idUsuarioLogado) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            TextButton(
                                                onClick = {
                                                    motivoDenuncia = ""
                                                    publicacaoParaDenunciar = publicacao.idPost
                                                },
                                                contentPadding = PaddingValues(
                                                    horizontal = 8.dp,
                                                    vertical = 2.dp
                                                )
                                            ) {
                                                Text(
                                                    text = "⚑ Denunciar",
                                                    color = TextoSecundario,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }

                                    // Ação disponível somente para o autor
                                    if (publicacao.idUsuario == idUsuarioLogado) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            TextButton(
                                                onClick = {
                                                    publicacaoParaExcluir = publicacao.idPost
                                                },
                                                contentPadding = PaddingValues(
                                                    horizontal = 8.dp,
                                                    vertical = 2.dp
                                                )
                                            ) {
                                                Text(
                                                    text = "Excluir publicação",
                                                    color = Color(0xFFB3261E),
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    // Texto da publicação
                                    if (!publicacao.texto.isNullOrBlank()) {
                                        Text(
                                            text = publicacao.texto.orEmpty(),
                                            color = TextoPrincipal,
                                            fontSize = 15.sp,
                                            lineHeight = 24.sp
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))
                                    }

// Imagens da publicação
                                    publicacao.midias
                                        .filter { it.tipo == "imagem" }
                                        .forEach { midia ->

                                            val urlImagem = remember(midia.arquivo) {
                                                Config.BASE_HTTP + Config.PASTA_SITE +
                                                        midia.arquivo.trimStart('/')
                                            }

                                            AsyncImage(
                                                model = urlImagem,
                                                contentDescription = "Imagem da publicação",
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(max = 400.dp)
                                                    .clip(RoundedCornerShape(16.dp)),
                                                contentScale = ContentScale.Fit
                                            )

                                            Spacer(modifier = Modifier.height(12.dp))
                                        }
                                    // Data da publicação
                                    Text(
                                        text = formatarDataPublicacao(publicacao.dataPostagem),
                                        color = TextoSecundario,
                                        fontSize = 12.sp
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    HorizontalDivider(
                                        color = BegeClaro.copy(alpha = 0.65f),
                                        thickness = 1.dp
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Resumo de interações
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "♥ ${publicacao.totalCurtidas} curtidas",
                                            color = MarromComunidade,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )

                                        Text(
                                            text = "💬 ${publicacao.totalComentarios} comentários",
                                            color = TextoSecundario,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Botões de interação
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                viewModel.curtirPublicacao(
                                                    context = context,
                                                    idPost = publicacao.idPost
                                                )
                                            },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(14.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (publicacao.curtidoPeloUsuario) {
                                                    AzulPrincipal
                                                } else {
                                                    AzulComunidade
                                                },
                                                contentColor = MarromComunidade
                                            )
                                        ) {
                                            Text(
                                                text = if (publicacao.curtidoPeloUsuario) {
                                                    "♥ Curtido"
                                                } else {
                                                    "♡ Curtir"
                                                },
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 13.sp
                                            )
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                viewModel.alternarComentarios(
                                                    context = context,
                                                    idPost = publicacao.idPost
                                                )
                                            },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(14.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = MarromComunidade
                                            )
                                        ) {
                                            Text(
                                                text = if (
                                                    publicacao.idPost in
                                                    estado.comentariosExpandidos
                                                ) {
                                                    "Ocultar"
                                                } else {
                                                    "💬 Comentar"
                                                },
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }

                                    // Área de comentários
                                    if (
                                        publicacao.idPost in
                                        estado.comentariosExpandidos
                                    ) {
                                        Spacer(modifier = Modifier.height(16.dp))

                                        HorizontalDivider(
                                            color = BegeClaro.copy(alpha = 0.65f),
                                            thickness = 1.dp
                                        )

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Text(
                                            text = "Comentários",
                                            color = MarromComunidade,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.sp
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        val comentarios =
                                            estado.comentariosPorPost[publicacao.idPost]
                                                ?: emptyList()

                                        if (comentarios.isEmpty()) {
                                            Surface(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(14.dp),
                                                color = AzulComunidade
                                            ) {
                                                Text(
                                                    text = "Ainda não há comentários. Que tal começar a conversa? 💗",
                                                    modifier = Modifier.padding(14.dp),
                                                    color = TextoSecundario,
                                                    fontSize = 13.sp,
                                                    lineHeight = 19.sp
                                                )
                                            }
                                        } else {
                                            comentarios.forEach { comentario ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 6.dp),
                                                    verticalAlignment = Alignment.Top
                                                ) {
                                                    FotoPerfilComunidade(
                                                        fotoPerfil = comentario.fotoPerfil,
                                                        nome = comentario.nome,
                                                        tamanho = 36
                                                    )

                                                    Column(
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .padding(start = 10.dp)
                                                            .clip(
                                                                RoundedCornerShape(14.dp)
                                                            )
                                                            .background(AzulComunidade)
                                                            .padding(12.dp)
                                                    ) {
                                                        Text(
                                                            text = "${comentario.nome} ${comentario.sobrenome}",
                                                            color = MarromComunidade,
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )

                                                        Text(
                                                            text = "@${comentario.username}",
                                                            color = TextoSecundario,
                                                            fontSize = 11.sp
                                                        )

                                                        Spacer(
                                                            modifier = Modifier.height(6.dp)
                                                        )

                                                        Text(
                                                            text = comentario.comentario,
                                                            color = TextoPrincipal,
                                                            fontSize = 14.sp,
                                                            lineHeight = 20.sp
                                                        )

                                                        // Respostas a este comentário
                                                        (comentario.respostas ?: emptyList()).forEach { resp ->
                                                            Spacer(modifier = Modifier.height(10.dp))

                                                            Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                verticalAlignment = Alignment.Top
                                                            ) {
                                                                FotoPerfilComunidade(
                                                                    fotoPerfil = resp.fotoPerfil,
                                                                    nome = resp.nome,
                                                                    tamanho = 28
                                                                )

                                                                Column(
                                                                    modifier = Modifier
                                                                        .weight(1f)
                                                                        .padding(start = 8.dp)
                                                                        .clip(RoundedCornerShape(12.dp))
                                                                        .background(Color(0xFFFFFDFB))
                                                                        .padding(10.dp)
                                                                ) {
                                                                    Text(
                                                                        text = "${resp.nome} ${resp.sobrenome}",
                                                                        color = MarromComunidade,
                                                                        fontSize = 12.sp,
                                                                        fontWeight = FontWeight.Bold
                                                                    )

                                                                    Text(
                                                                        text = resp.resposta,
                                                                        color = TextoPrincipal,
                                                                        fontSize = 13.sp,
                                                                        lineHeight = 18.sp
                                                                    )
                                                                }
                                                            }
                                                        }

                                                        TextButton(
                                                            onClick = {
                                                                if (estado.respondendoComentario == comentario.idComentario) {
                                                                    viewModel.cancelarResposta()
                                                                } else {
                                                                    viewModel.iniciarResposta(comentario.idComentario)
                                                                }
                                                            },
                                                            contentPadding = PaddingValues(
                                                                horizontal = 0.dp,
                                                                vertical = 2.dp
                                                            )
                                                        ) {
                                                            Text(
                                                                text = "↩ Responder",
                                                                color = AzulEscuroResposta,
                                                                fontSize = 12.sp,
                                                                fontWeight = FontWeight.SemiBold
                                                            )
                                                        }

                                                        // Campo de resposta (aparece ao tocar em "Responder")
                                                        if (estado.respondendoComentario == comentario.idComentario) {
                                                            OutlinedTextField(
                                                                value = estado.textoResposta,
                                                                onValueChange = { viewModel.atualizarTextoResposta(it) },
                                                                modifier = Modifier.fillMaxWidth(),
                                                                enabled = !estado.enviandoResposta,
                                                                placeholder = {
                                                                    Text("Escreva sua resposta...")
                                                                },
                                                                shape = RoundedCornerShape(14.dp),
                                                                colors = OutlinedTextFieldDefaults.colors(
                                                                    focusedBorderColor = AzulPrincipal,
                                                                    unfocusedBorderColor = BordaSuave,
                                                                    cursorColor = MarromComunidade
                                                                )
                                                            )

                                                            Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                horizontalArrangement = Arrangement.End
                                                            ) {
                                                                TextButton(
                                                                    onClick = { viewModel.cancelarResposta() },
                                                                    enabled = !estado.enviandoResposta
                                                                ) {
                                                                    Text("Cancelar", color = TextoSecundario)
                                                                }

                                                                Button(
                                                                    onClick = {
                                                                        viewModel.enviarResposta(
                                                                            context = context,
                                                                            idPost = publicacao.idPost,
                                                                            idComentario = comentario.idComentario
                                                                        )
                                                                    },
                                                                    enabled = !estado.enviandoResposta,
                                                                    shape = RoundedCornerShape(12.dp),
                                                                    colors = ButtonDefaults.buttonColors(
                                                                        containerColor = AzulPrincipal,
                                                                        contentColor = MarromComunidade
                                                                    )
                                                                ) {
                                                                    Text(
                                                                        text = if (estado.enviandoResposta) "Enviando..." else "Responder",
                                                                        fontWeight = FontWeight.SemiBold
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Campo para escrever comentário
                                        OutlinedTextField(
                                            value = estado.textosComentario[
                                                publicacao.idPost
                                            ] ?: "",
                                            onValueChange = { texto ->
                                                viewModel.atualizarTextoComentario(
                                                    idPost = publicacao.idPost,
                                                    texto = texto
                                                )
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            enabled = publicacao.idPost !in
                                                    estado.enviandoComentario,
                                            label = {
                                                Text("Escreva um comentário")
                                            },
                                            placeholder = {
                                                Text("Compartilhe sua opinião...")
                                            },
                                            shape = RoundedCornerShape(16.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = AzulPrincipal,
                                                unfocusedBorderColor = BordaSuave,
                                                focusedLabelColor = MarromComunidade,
                                                unfocusedLabelColor = TextoSecundario,
                                                cursorColor = MarromComunidade
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        // Enviar comentário
                                        Button(
                                            onClick = {
                                                viewModel.enviarComentario(
                                                    context = context,
                                                    idPost = publicacao.idPost
                                                )
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            enabled = publicacao.idPost !in
                                                    estado.enviandoComentario,
                                            shape = RoundedCornerShape(14.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = AzulPrincipal,
                                                contentColor = MarromComunidade
                                            )
                                        ) {
                                            Text(
                                                text = if (
                                                    publicacao.idPost in
                                                    estado.enviandoComentario
                                                ) {
                                                    "Enviando..."
                                                } else {
                                                    "Enviar comentário  ♡"
                                                },
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (publicacaoParaDenunciar != null) {
        AlertDialog(
            onDismissRequest = { publicacaoParaDenunciar = null },
            title = { Text("Denunciar publicação") },
            text = {
                Column {
                    Text(
                        text = "Conte o motivo. Um administrador vai analisar a denúncia.",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    listOf(
                        "Conteúdo ofensivo",
                        "Spam",
                        "Informação falsa",
                        "Outro motivo"
                    ).forEach { opcao ->
                        TextButton(
                            onClick = { motivoDenuncia = opcao },
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = if (motivoDenuncia == opcao) "● $opcao" else "○ $opcao",
                                color = MarromComunidade
                            )
                        }
                    }

                    OutlinedTextField(
                        value = motivoDenuncia,
                        onValueChange = { motivoDenuncia = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Motivo") },
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = motivoDenuncia.isNotBlank(),
                    onClick = {
                        val idPost = publicacaoParaDenunciar

                        if (idPost != null) {
                            viewModel.denunciarPublicacao(context, idPost, motivoDenuncia)
                        }

                        publicacaoParaDenunciar = null
                    }
                ) {
                    Text("Enviar denúncia")
                }
            },
            dismissButton = {
                TextButton(onClick = { publicacaoParaDenunciar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (publicacaoParaExcluir != null) {
        AlertDialog(
            onDismissRequest = {
                publicacaoParaExcluir = null
            },
            title = {
                Text("Excluir publicação?")
            },
            text = {
                Text("Tem certeza de que deseja excluir esta publicação? Essa ação não poderá ser desfeita.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val idPost = publicacaoParaExcluir

                        if (idPost != null) {
                            viewModel.excluirPublicacao(context, idPost)
                        }

                        publicacaoParaExcluir = null
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        publicacaoParaExcluir = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}