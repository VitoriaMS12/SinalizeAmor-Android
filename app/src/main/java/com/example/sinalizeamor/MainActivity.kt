package com.example.sinalizeamor

// Importa recursos necessários do Android.
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

// Recursos do Jetpack Compose.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// Componentes utilizados para criar a interface do app.
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.sinalizeamor.ui.theme.SinalizeAmorTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.platform.LocalContext


/* Activity principal do app.
   É a primeira tela executada quando o aplicativo é aberto. */
class MainActivity : ComponentActivity() {

    // Função executada quando a tela é criada.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Define o conteúdo visual do aplicativo.
        setContent {
            SinalizeAmorTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                TelaInicial()
            }
        }
    }
}


/* Função responsável por controlar qual tela será exibida. */
@Composable
fun TelaInicial() {

    val context = LocalContext.current

    // Guarda qual tela deve ser exibida.
    var telaAtual by remember {
        mutableStateOf("inicio")
    }

    // Guarda os dados do usuario que fez login.
    var usuarioLogado by remember {
        mutableStateOf<Usuario?>(null)
    }

    // Guarda o ID do curso que o usuario escolheu.
    var cursoSelicionado by remember {
        mutableStateOf<Int?>(null)
    }

    // Guarda o ID do módulo escolhido.
    var moduloSelecionado by remember {
        mutableStateOf<Int?>(null)
    }

// Guarda o título do módulo escolhido.
    var tituloModuloSelecionado by remember {
        mutableStateOf("")
    }

    // Verifica qual tela deve aparecer.
    // Organiza a tela e a barra de navegação.
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            // Verifica qual tela deve aparecer.
            when (telaAtual) {

        // =====================================================
        // TELA INICIAL
        // =====================================================
        "inicio" -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF7F1E8))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Identidade do aplicativo.
                Text(
                    text = "SINALIZE • AMOR",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA68A64)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFEAF7FC)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Nome principal.
                        Text(
                            text = "Sinalize Amor",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6F5B47)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "A comunicação aproxima pessoas.",
                            fontSize = 16.sp,
                            color = Color(0xFF555555)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Aprenda Libras de um jeito leve!",
                            fontSize = 14.sp,
                            color = Color(0xFF777777)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Botão para entrar em uma conta.
                        Button(
                            onClick = { telaAtual = "login" },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7EC8E3),
                                contentColor = Color(0xFF29434E)
                            )
                        ) {
                            Text(
                                text = "Entrar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Botão para criar uma conta.
                        OutlinedButton(
                            onClick = { telaAtual = "cadastro" },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF6F5B47)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                Color(0xFF7EC8E3)
                            )
                        ) {
                            Text(
                                text = "Criar conta",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "LIBRAS • INCLUSÃO • CONEXÃO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6F5B47)
                )
            }
        }


                // =====================================================
// TELA DE LOGIN
// =====================================================
                "login" -> {
                    TelaLogin(
                        onVoltar = {
                            telaAtual = "inicio"
                        },
                        onLoginSucesso = { usuario, token ->
                            usuarioLogado = usuario
                            TokenManager.salvarToken(context, token)
                            TokenManager.salvarIdUsuario(context, usuario.id_usuario)
                            telaAtual = "principal"
                        }
                    )
                }


        // =====================================================
        // TELA DE CADASTRO
        // =====================================================
        "cadastro" -> {

            TelaCadastro(
                onVoltar = {
                    // Volta para a tela inicial.
                    telaAtual = "inicio"
                }
            )
        }


        // =====================================================
        // TELA PRINCIPAL
        // =====================================================
        "principal" -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF7F1E8))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Identidade do aplicativo.
                Text(
                    text = "SINALIZE • AMOR",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA68A64)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFDFB)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Que bom ter você aqui!",
                            fontSize = 15.sp,
                            color = Color(0xFF777777)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Olá, ${usuarioLogado?.nome}!",
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6F5B47)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "@${usuarioLogado?.username}",
                            fontSize = 15.sp,
                            color = Color(0xFF777777)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        androidx.compose.material3.HorizontalDivider(
                            color = Color(0xFFE8DED1),
                            thickness = 1.dp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Seu próximo aprendizado começa aqui.",
                            fontSize = 15.sp,
                            color = Color(0xFF555555)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { telaAtual = "cursos" },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7EC8E3),
                                contentColor = Color(0xFF29434E)
                            )
                        ) {
                            Text(
                                text = "Explorar cursos",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedButton(
                            onClick = {
                                TokenManager.removerToken(context)
                                usuarioLogado = null
                                telaAtual = "inicio"
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF6F5B47)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                Color(0xFF7EC8E3)
                            )
                        ) {
                            Text(
                                text = "Sair da conta",
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "LIBRAS • INCLUSÃO • CONEXÃO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6F5B47)
                )
            }
        }

        "cursos" -> {
            TelaCursos (
                onVoltar = {
                    telaAtual = "principal"
                },
                onCursoSelecionado = {idCurso ->
                    // Guarda o curso escolhido.
                    cursoSelicionado = idCurso

                    // Depois iremos criar a tela de modulos.
                    telaAtual = "modulos"
                }
            )
        }

        "modulos" -> {
            TelaModulos(
                idCurso = cursoSelicionado ?: 0,
                onVoltar = {
                    telaAtual = "cursos"
                },

                onModuloSelecionado = { idModulo, tituloModulo ->
                    // Guarda o módulo escolhido.
                    moduloSelecionado = idModulo
                    tituloModuloSelecionado = tituloModulo

                    // Abre a tela das aulas.
                    telaAtual = "aulas"
                }
            )
        }

        "aulas" -> {
            TelaAulas(
                idModulo = moduloSelecionado ?: 0,
                tituloModulo = tituloModuloSelecionado,
                onVoltar = {
                    telaAtual = "modulos"
                }
            )
        }

        "perfil" -> {
            TelaPerfil(
                usuario = usuarioLogado,
                onVoltar = {
                    telaAtual = "principal"
                },
                onSair = {
                    TokenManager.removerToken(context)
                    usuarioLogado = null
                    telaAtual = "inicio"
                }
            )
        }

        "comunidade" -> {
            TelaComunidade()
        }



    }
}

        // Barra exibida somente nas telas principais.
        if (telaAtual == "principal" ||
            telaAtual == "cursos" ||
            telaAtual == "perfil" ||
            telaAtual == "comunidade"
        ) {
            BarraNavegacao(
                telaAtual = telaAtual,
                onInicio = { telaAtual = "principal" },
                onCursos = { telaAtual = "cursos" },
                onPerfil = { telaAtual = "perfil" },
                onComunidade = { telaAtual = "comunidade" }
            )
        }
    } // Fecha o Column
} // Fecha TelaInicial

@Composable
fun BarraNavegacao(
    telaAtual: String,
    onInicio: () -> Unit,
    onCursos: () -> Unit,
    onPerfil: () -> Unit,
    onComunidade: () -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFFFFFDFB),
        contentColor = Color(0xFF6F5B47)
    ) {
        NavigationBarItem(
            selected = telaAtual == "principal",
            onClick = onInicio,
            icon = {
                Text(
                    text = "⌂",
                    fontSize = 24.sp
                )
            },
            label = {
                Text("Início")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6F5B47),
                selectedTextColor = Color(0xFF6F5B47),
                indicatorColor = Color(0xFFEAF7FC),
                unselectedIconColor = Color(0xFF777777),
                unselectedTextColor = Color(0xFF777777)
            )
        )

        NavigationBarItem(
            selected = telaAtual == "cursos",
            onClick = onCursos,
            icon = {
                Text(
                    text = "▤",
                    fontSize = 22.sp
                )
            },
            label = {
                Text("Cursos")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6F5B47),
                selectedTextColor = Color(0xFF6F5B47),
                indicatorColor = Color(0xFFEAF7FC),
                unselectedIconColor = Color(0xFF777777),
                unselectedTextColor = Color(0xFF777777)
            )
        )

        NavigationBarItem(
            selected = telaAtual == "perfil",
            onClick = onPerfil,
            icon = {
                Text(
                    text = "\uD83D\uDC64",
                    fontSize = 22.sp
                )
            },
            label = {
                Text("Perfil")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6F5B47),
                selectedTextColor = Color(0xFF6F5B47),
                indicatorColor = Color(0xFFEAF7FC),
                unselectedIconColor = Color(0xFF777777),
                unselectedTextColor = Color(0xFF777777)
            )
        )

        NavigationBarItem(
            selected = telaAtual == "comunidade",
            onClick = onComunidade,
            icon = {
                Text(
                    text = "♡",
                    fontSize = 24.sp
                )
            },
            label = {
                Text("Comunidade")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6F5B47),
                selectedTextColor = Color(0xFF6F5B47),
                indicatorColor = Color(0xFFEAF7FC),
                unselectedIconColor = Color(0xFF777777),
                unselectedTextColor = Color(0xFF777777)
            )
        )
    }
}