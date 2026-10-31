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
import com.example.sinalizeamor.ui.theme.CoresSinalizeAmor
import com.example.sinalizeamor.ui.viewmodel.ModulosViewModel

@Composable
fun TelaModulos(
    idCurso: Int,
    onVoltar: () -> Unit,
    onModuloSelecionado: (Int, String) -> Unit,
    viewModel: ModulosViewModel = viewModel()
) {
    val modulos = viewModel.modulos
    val mensagem = viewModel.mensagem
    val carregando = viewModel.carregando

    LaunchedEffect(idCurso) {
        viewModel.carregarModulos(idCurso)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoresSinalizeAmor.Creme)
            .padding(24.dp)
    ) {
        Text(
            text = "SINALIZE • AMOR",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = CoresSinalizeAmor.Bege
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Módulos",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = CoresSinalizeAmor.Marrom
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Escolha um módulo e continue seu aprendizado!",
            fontSize = 16.sp,
            color = CoresSinalizeAmor.TextoSecundario
        )

        Spacer(modifier = Modifier.height(24.dp))

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
                            color = CoresSinalizeAmor.Marrom
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Carregando módulos...",
                            fontSize = 15.sp,
                            color = CoresSinalizeAmor.Texto
                        )
                    }
                }
            } else if (mensagem.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CoresSinalizeAmor.FundoCartao
                        )
                    ) {
                        Text(
                            text = mensagem,
                            modifier = Modifier.padding(20.dp),
                            fontSize = 15.sp,
                            color = CoresSinalizeAmor.Texto
                        )
                    }
                }
            } else if (modulos.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CoresSinalizeAmor.AzulClaro
                        )
                    ) {
                        Text(
                            text = "Ainda não há módulos disponíveis.",
                            modifier = Modifier.padding(20.dp),
                            fontSize = 15.sp,
                            color = CoresSinalizeAmor.Texto
                        )
                    }
                }
            } else {
                items(modulos) { modulo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onModuloSelecionado(
                                    modulo.id_modulo,
                                    modulo.titulo
                                )
                            },
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CoresSinalizeAmor.AzulClaro
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
                                text = modulo.titulo,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = CoresSinalizeAmor.Marrom
                            )

                            Text(
                                text = modulo.descricao,
                                fontSize = 15.sp,
                                color = CoresSinalizeAmor.Texto,
                                lineHeight = 22.sp
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Acessar módulo  →",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CoresSinalizeAmor.AzulHover
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
                containerColor = CoresSinalizeAmor.Azul,
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
