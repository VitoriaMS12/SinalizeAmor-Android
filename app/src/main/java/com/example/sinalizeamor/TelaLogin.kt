package com.example.sinalizeamor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sinalizeamor.ui.theme.CoresSinalizeAmor
import com.example.sinalizeamor.ui.viewmodel.LoginViewModel

@Composable
fun TelaLogin(
    onVoltar: () -> Unit,
    onLoginSucesso: (Usuario, String) -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    // Ao sair desta tela (voltar, login feito ou sair da conta),
    // e-mail e senha são apagados.
    androidx.compose.runtime.DisposableEffect(Unit) {
        onDispose { loginViewModel.limpar() }
    }
    val estado by loginViewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F1E8))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            fontSize = 32.sp,
            color = CoresSinalizeAmor.Marrom
        )

        OutlinedTextField(
            value = estado.email,
            onValueChange = loginViewModel::atualizarEmail,
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CoresSinalizeAmor.Azul,
                unfocusedBorderColor = CoresSinalizeAmor.Borda,
                focusedLabelColor = CoresSinalizeAmor.Azul,
                unfocusedLabelColor = CoresSinalizeAmor.TextoSecundario,
                cursorColor = CoresSinalizeAmor.Azul
            )
        )

        if (estado.erroEmail.isNotEmpty()) {
            Text(text = estado.erroEmail)
        }

        OutlinedTextField(
            value = estado.senha,
            onValueChange = loginViewModel::atualizarSenha,
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (estado.mostrarSenha) {
                androidx.compose.ui.text.input.VisualTransformation.None
            } else {
                androidx.compose.ui.text.input.PasswordVisualTransformation()
            },
            trailingIcon = {
                TextButton(
                    onClick = loginViewModel::alternarVisibilidadeSenha
                ) {
                    Text(
                        text = if (estado.mostrarSenha) "Ocultar" else "Mostrar",
                        color = CoresSinalizeAmor.Marrom
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CoresSinalizeAmor.Azul,
                unfocusedBorderColor = CoresSinalizeAmor.Borda,
                focusedLabelColor = CoresSinalizeAmor.Azul,
                unfocusedLabelColor = CoresSinalizeAmor.TextoSecundario,
                cursorColor = CoresSinalizeAmor.Azul
            )
        )

        Button(
            onClick = {
                loginViewModel.fazerLogin(onLoginSucesso)
            },
            enabled = !estado.carregando,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CoresSinalizeAmor.Azul,
                contentColor = CoresSinalizeAmor.Branco
            )
        ) {
            Text(if (estado.carregando) "Entrando..." else "Entrar")
        }

        if (estado.mensagem.isNotEmpty()) {
            Text(text = estado.mensagem)
        }

        Button(
            onClick = onVoltar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CoresSinalizeAmor.Azul,
                contentColor = CoresSinalizeAmor.Branco
            )
        ) {
            Text("Voltar")
        }
    }
}