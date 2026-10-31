package com.example.sinalizeamor

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sinalizeamor.ui.theme.CoresSinalizeAmor
import com.example.sinalizeamor.ui.viewmodel.AulasViewModel

@Composable
fun TelaAulas(
    idModulo: Int,
    tituloModulo: String,
    onVoltar: () -> Unit,
    viewModel: AulasViewModel = viewModel()
) {
    val aulas = viewModel.aulas
    val mensagem = viewModel.mensagem
    val carregando = viewModel.carregando
    val contexto = LocalContext.current

    LaunchedEffect(idModulo) {
        viewModel.carregarAulas(idModulo)
    }

    // Agora toda a tela está dentro da mesma lista rolável.
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CoresSinalizeAmor.Creme),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Cabeçalho
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onVoltar,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CoresSinalizeAmor.AzulClaro,
                        contentColor = CoresSinalizeAmor.Marrom
                    ),
                    modifier = Modifier.size(48.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "‹",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column {
                    Text(
                        text = "SINALIZE • AMOR",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoresSinalizeAmor.Bege,
                        letterSpacing = 2.sp
                    )

                    Text(
                        text = "Sua jornada em Libras",
                        fontSize = 14.sp,
                        color = CoresSinalizeAmor.TextoSecundario
                    )
                }
            }
        }

        // Cartão de apresentação
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CoresSinalizeAmor.Azul
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "📚  ÁREA DE APRENDIZAGEM",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoresSinalizeAmor.Creme,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = tituloModulo.ifBlank {
                            "Vamos aprender?"
                        },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CoresSinalizeAmor.Creme
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Escolha uma aula, assista ao vídeo e utilize a apostila para praticar.",
                        fontSize = 15.sp,
                        color = CoresSinalizeAmor.Creme,
                        lineHeight = 23.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Box(
                        modifier = Modifier
                            .background(
                                color = CoresSinalizeAmor.Creme,
                                shape = RoundedCornerShape(50)
                            )
                            .padding(
                                horizontal = 14.dp,
                                vertical = 8.dp
                            )
                    ) {
                        Text(
                            text = if (carregando) {
                                "Carregando aulas..."
                            } else {
                                "${aulas.size} aulas disponíveis"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoresSinalizeAmor.Marrom
                        )
                    }
                }
            }
        }

        // Título da lista
        item {
            Column {
                Text(
                    text = "Suas aulas",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoresSinalizeAmor.Marrom
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Aprenda no seu ritmo, uma aula de cada vez.",
                    fontSize = 14.sp,
                    color = CoresSinalizeAmor.TextoSecundario
                )
            }
        }

        // Carregamento
        if (carregando) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = CoresSinalizeAmor.Marrom
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Preparando suas aulas...",
                        color = CoresSinalizeAmor.TextoSecundario,
                        fontSize = 15.sp
                    )
                }
            }
        } else if (mensagem.isNotEmpty()) {
            // Mensagem de erro ou ausência de aulas
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CoresSinalizeAmor.AzulClaro
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "📖  Ops!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoresSinalizeAmor.Marrom
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = mensagem,
                            fontSize = 15.sp,
                            color = CoresSinalizeAmor.Texto
                        )
                    }
                }
            }
        } else {
            // Cartões das aulas
            items(
                items = aulas,
                key = { it.id_aula }
            ) { aula ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CoresSinalizeAmor.AzulClaro
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = CoresSinalizeAmor.Azul,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = aula.ordem.toString(),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CoresSinalizeAmor.Creme
                                )
                            }

                            Spacer(modifier = Modifier.size(14.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "AULA ${aula.ordem}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CoresSinalizeAmor.Bege,
                                    letterSpacing = 1.5.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = aula.titulo,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CoresSinalizeAmor.Marrom,
                                    lineHeight = 25.sp
                                )
                            }
                        }

                        HorizontalDivider(
                            color = CoresSinalizeAmor.Creme,
                            thickness = 1.dp
                        )

                        Text(
                            text = aula.descricao,
                            fontSize = 15.sp,
                            color = CoresSinalizeAmor.Texto,
                            lineHeight = 22.sp
                        )

                        aula.conteudo
                            ?.takeIf { it.isNotBlank() }
                            ?.let { conteudo ->
                                Text(
                                    text = conteudo,
                                    fontSize = 14.sp,
                                    color = CoresSinalizeAmor.TextoSecundario,
                                    lineHeight = 21.sp
                                )
                            }

                        // Vídeo
                        aula.video
                            ?.takeIf { it.isNotBlank() }
                            ?.let { video ->
                                Button(
                                    onClick = {
                                        abrirLink(contexto, video)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CoresSinalizeAmor.Azul
                                    )
                                ) {
                                    Text(
                                        text = "▶  Assistir ao vídeo",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                        // Apostilas em PDF
                        aula.materiais
                            .filter {
                                it.tipo.equals("pdf", ignoreCase = true)
                            }
                            .forEach { material ->
                                Button(
                                    onClick = {
                                        abrirLink(
                                            contexto,
                                            material.arquivo
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CoresSinalizeAmor.Bege
                                    )
                                ) {
                                    Text(
                                        text = "📄  Abrir apostila PDF",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                    }
                }
            }
        }

        // Espaço ao final da lista
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private fun abrirLink(
    contexto: Context,
    endereco: String
) {
    val url = if (
        endereco.startsWith("http://") ||
        endereco.startsWith("https://")
    ) {
        endereco
    } else {
        "https://192.168.1.5/tcc%20(2)/tcc/" +
                endereco.trimStart('/')
    }

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    contexto.startActivity(intent)
}