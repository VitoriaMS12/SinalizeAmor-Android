package com.example.sinalizeamor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sinalizeamor.ui.theme.CoresSinalizeAmor
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sinalizeamor.ui.viewmodel.CadastroViewModel


@Composable
fun TelaCadastro(
    onVoltar: () -> Unit,
    cadastroViewModel: CadastroViewModel = viewModel()
) {

    val estado by cadastroViewModel.uiState

    val nome = estado.nome
    val sobrenome = estado.sobrenome
    val username = estado.username
    val email = estado.email
    val senha = estado.senha
    val mostrarSenha = estado.mostrarSenha
    val mensagem = estado.mensagem
    val erroEmail = estado.erroEmail
    val carregando = estado.carregando

    val coresCampo = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = CoresSinalizeAmor.Azul,
        unfocusedBorderColor = CoresSinalizeAmor.Borda,
        focusedLabelColor = CoresSinalizeAmor.Marrom,
        unfocusedLabelColor = CoresSinalizeAmor.TextoSecundario,
        focusedTextColor = CoresSinalizeAmor.Texto,
        unfocusedTextColor = CoresSinalizeAmor.Texto,
        cursorColor = CoresSinalizeAmor.Azul
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoresSinalizeAmor.Creme)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sinalize Amor",
            fontSize = 18.sp,
            color = CoresSinalizeAmor.Bege,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Crie sua conta",
            fontSize = 30.sp,
            color = CoresSinalizeAmor.Marrom,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Vamos começar essa jornada juntos!",
            fontSize = 14.sp,
            color = CoresSinalizeAmor.TextoSecundario
        )

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = CoresSinalizeAmor.FundoCartao,
            shadowElevation = 3.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = nome,
                    onValueChange = cadastroViewModel::atualizarNome,
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = coresCampo,
                    singleLine = true
                )

                OutlinedTextField(
                    value = sobrenome,
                    onValueChange = cadastroViewModel::atualizarSobrenome,
                    label = { Text("Sobrenome") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = coresCampo,
                    singleLine = true
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = cadastroViewModel::atualizarUsername,
                    label = { Text("Nome de usuário") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = coresCampo,
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = cadastroViewModel::atualizarEmail,
                    label = { Text("E-mail") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = coresCampo,
                    singleLine = true,
                    isError = erroEmail
                )

                if (erroEmail) {
                    Text(
                        text = "Digite um endereço de e-mail válido.",
                        color = CoresSinalizeAmor.Marrom,
                        fontSize = 12.sp
                    )
                }

                OutlinedTextField(
                    value = senha,
                    onValueChange = cadastroViewModel::atualizarSenha,
                    label = { Text("Senha") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = coresCampo,
                    singleLine = true,
                    visualTransformation = if (mostrarSenha) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        TextButton(
                            onClick = { cadastroViewModel.alternarVisibilidadeSenha() }
                        ) {
                            Text(
                                text = if (mostrarSenha) "Ocultar" else "Mostrar",
                                color = CoresSinalizeAmor.Marrom
                            )
                        }
                    }
                )

                if (mensagem.isNotEmpty()) {
                    Text(
                        text = mensagem,
                        color = CoresSinalizeAmor.Marrom,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        cadastroViewModel.fazerCadastro()
                    },
                    enabled = !carregando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CoresSinalizeAmor.Azul,
                        contentColor = CoresSinalizeAmor.Branco
                    )
                ) {
                    Text(
                        text = if (carregando) {
                            "Criando conta..."
                        } else {
                            "Criar conta"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                TextButton(
                    onClick = onVoltar,
                    enabled = !carregando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Voltar",
                        color = CoresSinalizeAmor.Marrom
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
