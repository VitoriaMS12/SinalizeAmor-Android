package com.example.sinalizeamor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sinalizeamor.ui.viewmodel.CursosViewModel

private val FundoCursos = Color(0xFFF7F1E8)
private val AzulCartaoCursos = Color(0xFFEAF7FC)
private val AzulBotaoCursos = Color(0xFF7EC8E3)
private val MarromCursos = Color(0xFF6F5B47)
private val TextoCursos = Color(0xFF555555)

@Composable
fun TelaCursos(
    onVoltar: () -> Unit,
    onCursoSelecionado: (Int) -> Unit,
    viewModel: CursosViewModel = viewModel()
) {
    val cursos = viewModel.cursos
    val mensagem = viewModel.mensagem
    val carregando = viewModel.carregando

    LaunchedEffect(Unit) {
        viewModel.carregarCursos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoCursos)
            .padding(24.dp)
    ) {
        Text(
            text = "SINALIZE • AMOR",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFA68A64)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Cursos",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MarromCursos
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Escolha um curso e comece a aprender Libras!",
            fontSize = 16.sp,
            color = TextoCursos
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Área rolável dos cursos e estados de carregamento.
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (carregando) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = MarromCursos
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Carregando cursos...",
                            color = TextoCursos,
                            fontSize = 15.sp
                        )
                    }
                }
            } else if (mensagem.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFFDFB)
                        )
                    ) {
                        Text(
                            text = mensagem,
                            modifier = Modifier.padding(20.dp),
                            color = TextoCursos,
                            fontSize = 15.sp
                        )
                    }
                }
            } else if (cursos.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AzulCartaoCursos
                        )
                    ) {
                        Text(
                            text = "Ainda não há cursos disponíveis.",
                            modifier = Modifier.padding(20.dp),
                            color = TextoCursos,
                            fontSize = 15.sp
                        )
                    }
                }
            } else {
                items(cursos) { curso ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onCursoSelecionado(curso.id_curso)
                            },
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AzulCartaoCursos
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(22.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = curso.titulo,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = MarromCursos
                            )

                            Text(
                                text = curso.descricao,
                                fontSize = 15.sp,
                                color = TextoCursos,
                                lineHeight = 22.sp
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Acessar curso  →",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF397F9B)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onVoltar,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulBotaoCursos,
                contentColor = Color(0xFF29434E)
            )
        ) {
            Text(
                text = "Voltar",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
