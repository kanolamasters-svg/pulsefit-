package com.example.macfitness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val Black = Color(0xFF0B0B0D)
private val Panel = Color(0xFF19191D)
private val Panel2 = Color(0xFF27272D)
private val Red = Color(0xFFE32636)
private val Muted = Color(0xFF9B9BA4)
private val Snow = Color(0xFFF7F7F8)

private data class Exercise(
    val name: String, val group: String, val equipment: String,
    val sets: Int, val reps: String, val description: String,
    val instructions: List<String>, val symbol: String
)

private val exerciseList = listOf(
    Exercise("Supino reto", "Peitoral", "Barra", 4, "8–12", "Exercício fundamental para desenvolver o peitoral.", listOf("Deite no banco com os pés firmes no chão.", "Segure a barra um pouco além da largura dos ombros.", "Desça com controle até próximo ao peito.", "Empurre para cima mantendo a estabilidade."), "🏋️"),
    Exercise("Supino inclinado", "Peitoral", "Halteres", 3, "10–12", "Ênfase na porção superior do peitoral.", listOf("Ajuste o banco entre 30° e 45°.", "Mantenha as escápulas apoiadas.", "Desça os halteres com controle.", "Suba sem bater os halteres."), "💪"),
    Exercise("Crucifixo máquina", "Peitoral", "Máquina", 3, "12–15", "Movimento de adução horizontal para o peitoral.", listOf("Ajuste o banco.", "Mantenha leve flexão nos cotovelos.", "Aproxime os braços à frente.", "Retorne lentamente."), "🎯"),
    Exercise("Puxada frontal", "Costas", "Polia", 4, "8–12", "Trabalha principalmente a largura das costas.", listOf("Segure a barra além dos ombros.", "Incline levemente o tronco.", "Puxe em direção à parte alta do peito.", "Retorne controlando a subida."), "🧗"),
    Exercise("Remada baixa", "Costas", "Cabo", 3, "10–12", "Desenvolve espessura e controle das costas.", listOf("Sente com o tronco firme.", "Puxe em direção ao abdômen.", "Aproxime as escápulas.", "Estenda os braços com controle."), "🚣"),
    Exercise("Remada unilateral", "Costas", "Halter", 3, "10–12", "Trabalha cada lado individualmente.", listOf("Apoie uma mão e um joelho no banco.", "Mantenha a coluna neutra.", "Puxe o halter em direção ao quadril.", "Desça e troque o lado."), "💪"),
    Exercise("Desenvolvimento", "Ombros", "Halteres", 3, "8–12", "Movimento composto para os deltoides.", listOf("Sente com as costas apoiadas.", "Posicione os halteres na altura dos ombros.", "Empurre para cima.", "Desça com controle."), "🏋️"),
    Exercise("Elevação lateral", "Ombros", "Halteres", 3, "12–15", "Ênfase na porção lateral dos deltoides.", listOf("Fique em pé com halteres ao lado do corpo.", "Eleve os braços até a linha dos ombros.", "Mantenha os cotovelos levemente flexionados.", "Desça lentamente."), "🪽"),
    Exercise("Rosca direta", "Bíceps", "Barra", 3, "10–12", "Exercício clássico para os flexores do cotovelo.", listOf("Segure a barra com as palmas para cima.", "Mantenha os cotovelos próximos ao tronco.", "Flexione sem balançar o corpo.", "Desça controlando."), "💪"),
    Exercise("Tríceps na polia", "Tríceps", "Corda", 3, "10–15", "Isola o tríceps com tensão constante.", listOf("Mantenha os cotovelos junto ao corpo.", "Empurre a corda para baixo.", "Estenda os cotovelos.", "Retorne lentamente."), "🔥"),
    Exercise("Agachamento", "Pernas", "Barra", 4, "8–12", "Movimento multiarticular para membros inferiores.", listOf("Posicione a barra com segurança.", "Afaste os pés na largura dos ombros.", "Flexione quadris e joelhos mantendo o tronco firme.", "Suba empurrando o chão."), "🦵"),
    Exercise("Leg press", "Pernas", "Máquina", 4, "10–15", "Trabalha quadríceps e glúteos com estabilidade.", listOf("Apoie as costas no encosto.", "Posicione os pés na plataforma.", "Desça sem tirar o quadril do banco.", "Empurre sem travar os joelhos."), "⚡"),
    Exercise("Mesa flexora", "Pernas", "Máquina", 3, "12–15", "Ênfase nos músculos posteriores da coxa.", listOf("Ajuste o rolo acima dos calcanhares.", "Mantenha o quadril apoiado.", "Flexione os joelhos.", "Retorne controlando a carga."), "🎯"),
    Exercise("Elevação pélvica", "Glúteos", "Barra", 4, "10–12", "Extensão de quadril com foco nos glúteos.", listOf("Apoie as costas em um banco.", "Posicione a carga sobre o quadril.", "Eleve até alinhar tronco e coxas.", "Contraia e desça com controle."), "🍑"),
    Exercise("Prancha", "Abdômen", "Peso corporal", 3, "30–45 s", "Trabalho isométrico de estabilidade do tronco.", listOf("Apoie antebraços e pontas dos pés.", "Mantenha o corpo alinhado.", "Contraia abdômen e glúteos.", "Respire sem deixar o quadril cair."), "🔥"),
    Exercise("Abdominal na polia", "Abdômen", "Cabo", 3, "12–15", "Flexão de tronco com resistência ajustável.", listOf("Ajoelhe-se diante da polia.", "Segure a corda próxima à cabeça.", "Flexione o tronco contraindo o abdômen.", "Retorne sem perder a tensão."), "🎯")
)

private val categories = listOf("Todos", "Peitoral", "Costas", "Ombros", "Bíceps", "Tríceps", "Pernas", "Glúteos", "Abdômen")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MACFitnessApp() }
    }
}

@Composable
private fun MACFitnessApp() {
    var page by remember { mutableIntStateOf(0) }
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    var search by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Todos") }

    MaterialTheme(colorScheme = darkColorScheme(primary = Red, background = Black, surface = Panel, onSurface = Snow)) {
        Surface(Modifier.fillMaxSize(), color = Black) {
            val current = selectedExercise
            if (current != null) {
                ExerciseDetail(current) { selectedExercise = null }
            } else {
                Column(Modifier.fillMaxSize()) {
                    Box(Modifier.weight(1f)) {
                        when (page) {
                            0 -> HomeScreen(
                                onExplore = { page = 1 },
                                onSelect = { selectedExercise = it }
                            )
                            1 -> LibraryScreen(search, { search = it }, category, { category = it }) {
                                selectedExercise = it
                            }
                            else -> TrainingScreen { selectedExercise = it }
                        }
                    }
                    BottomNavigation(page) { page = it }
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(onExplore: () -> Unit, onSelect: (Exercise) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(19.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("M.A.C. FITNESS", color = Red, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                    Spacer(Modifier.height(6.dp))
                    Text("Vamos conquistar.", color = Snow, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Seu próximo nível começa hoje.", color = Muted, fontSize = 13.sp)
                }
                Box(Modifier.size(46.dp).clip(CircleShape).background(Panel2), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, null, tint = Snow)
                }
            }
        }
        item {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF941A28), Color(0xFF42151D), Color(0xFF211216))))
                    .padding(22.dp)
            ) {
                Text("FOCO • DISCIPLINA • RESULTADO", color = Color(0xFFFFC7CC), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(Modifier.height(12.dp))
                Text("MOVIMENTE ATÉ\nCONQUISTAR.", color = Snow, fontSize = 29.sp, lineHeight = 33.sp, fontWeight = FontWeight.Black)
                Spacer(Modifier.height(10.dp))
                Text("Treine com propósito. Evolua todos os dias.", color = Snow.copy(alpha = .85f), fontSize = 12.sp)
                Spacer(Modifier.height(18.dp))
                Button(onClick = onExplore, colors = ButtonDefaults.buttonColors(containerColor = Snow, contentColor = Black), shape = RoundedCornerShape(12.dp)) {
                    Text("EXPLORAR EXERCÍCIOS", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
                    Icon(Icons.Default.ArrowForward, null, modifier = Modifier.padding(start = 7.dp).size(16.dp))
                }
            }
        }
        item {
            Text("SEU RESUMO", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                StatCard("Treinos", "05", "esta semana", Icons.Default.FitnessCenter, Modifier.weight(1f))
                StatCard("Sequência", "03", "dias ativos", Icons.Default.LocalFireDepartment, Modifier.weight(1f))
                StatCard("Tempo", "4h20", "total", Icons.Default.Timer, Modifier.weight(1f))
            }
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("EXERCÍCIOS EM DESTAQUE", color = Snow, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("VER TODOS", color = Red, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onExplore() })
            }
        }
        items(exerciseList.take(3)) { ex -> ExerciseRow(ex) { onSelect(ex) } }
        item {
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(17.dp)).background(Panel).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.EmojiEvents, null, tint = Red, modifier = Modifier.size(29.dp))
                Column(Modifier.padding(start = 12.dp)) {
                    Text("A consistência vence a intensidade.", color = Snow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Pequenos avanços constroem resultados.", color = Muted, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, caption: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier) {
    Column(modifier.clip(RoundedCornerShape(15.dp)).background(Panel).padding(11.dp)) {
        Icon(icon, null, tint = Red, modifier = Modifier.size(17.dp))
        Spacer(Modifier.height(10.dp))
        Text(value, color = Snow, fontSize = 19.sp, fontWeight = FontWeight.Bold)
        Text(title, color = Snow, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        Text(caption, color = Muted, fontSize = 9.sp, maxLines = 1)
    }
}

@Composable
private fun LibraryScreen(search: String, onSearch: (String) -> Unit, category: String, onCategory: (String) -> Unit, onSelect: (Exercise) -> Unit) {
    val filtered = exerciseList.filter {
        (category == "Todos" || it.group == category) &&
            (it.name.contains(search, ignoreCase = true) || it.group.contains(search, ignoreCase = true))
    }
    Column(Modifier.fillMaxSize().padding(top = 22.dp)) {
        Text("Biblioteca", color = Snow, fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp))
        Text("Encontre o exercício certo para sua evolução.", color = Muted, fontSize = 12.sp, modifier = Modifier.padding(start = 20.dp, top = 4.dp, bottom = 15.dp))
        OutlinedTextField(
            value = search, onValueChange = onSearch,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            placeholder = { Text("Buscar exercício ou músculo...", color = Muted, fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = Muted) },
            singleLine = true, shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Red, unfocusedBorderColor = Panel2,
                focusedTextColor = Snow, unfocusedTextColor = Snow, cursorColor = Red
            )
        )
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            categories.forEach { group ->
                FilterChip(
                    selected = category == group, onClick = { onCategory(group) },
                    label = { Text(group, fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Red, selectedLabelColor = Snow,
                        containerColor = Panel, labelColor = Muted
                    )
                )
            }
        }
        Text(filtered.size.toString() + " EXERCÍCIOS", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, modifier = Modifier.padding(horizontal = 20.dp, vertical = 3.dp))
        LazyColumn(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            items(filtered) { ex -> ExerciseRow(ex) { onSelect(ex) } }
        }
    }
}

@Composable
private fun ExerciseRow(ex: Exercise, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Panel)
            .clickable(onClick = onClick).padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(53.dp).clip(RoundedCornerShape(13.dp)).background(Panel2), contentAlignment = Alignment.Center) {
            Text(ex.symbol, fontSize = 24.sp)
        }
        Column(Modifier.weight(1f).padding(start = 12.dp)) {
            Text(ex.name, color = Snow, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(Modifier.height(3.dp))
            Text(ex.group + " • " + ex.equipment, color = Muted, fontSize = 10.sp)
            Text(ex.sets.toString() + " séries  |  " + ex.reps + " reps", color = Red, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Icon(Icons.Default.ChevronRight, null, tint = Muted)
    }
}

@Composable
private fun TrainingScreen(onSelect: (Exercise) -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        item {
            Text("Meu treino", color = Snow, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Organize sua sessão e acompanhe cada série.", color = Muted, fontSize = 12.sp)
        }
        item {
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Panel).padding(17.dp)) {
                Text("PLANO DE TREINO", color = Red, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text("Divisão semanal", color = Snow, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 7.dp))
                Text("Sugestão inicial para organizar os grupos musculares.", color = Muted, fontSize = 11.sp)
                Spacer(Modifier.height(12.dp))
                val plan = listOf("SEG" to "Peito + tríceps", "TER" to "Costas + bíceps", "QUA" to "Pernas", "QUI" to "Ombros + abdômen", "SEX" to "Glúteos + complementos")
                plan.forEach { pair ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.width(40.dp).clip(RoundedCornerShape(7.dp)).background(Panel2).padding(vertical = 7.dp), contentAlignment = Alignment.Center) {
                            Text(pair.first, color = Red, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                        }
                        Text(pair.second, color = Snow, fontSize = 12.sp, modifier = Modifier.padding(start = 12.dp))
                    }
                }
            }
        }
        item { Text("ESCOLHA UM EXERCÍCIO", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
        items(exerciseList.take(6)) { ex -> ExerciseRow(ex) { onSelect(ex) } }
    }
}

@Composable
private fun ExerciseDetail(ex: Exercise, onBack: () -> Unit) {
    var completed by remember { mutableIntStateOf(0) }
    var seconds by remember { mutableIntStateOf(60) }
    var timerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(timerRunning, seconds) {
        if (timerRunning && seconds > 0) {
            delay(1000)
            seconds -= 1
        } else if (seconds == 0) {
            timerRunning = false
        }
    }

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Voltar", tint = Snow) }
            Text("DETALHES DO EXERCÍCIO", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 7.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            item {
                Box(Modifier.fillMaxWidth().height(155.dp).clip(RoundedCornerShape(22.dp)).background(Brush.linearGradient(listOf(Color(0xFF701722), Panel2))), contentAlignment = Alignment.Center) {
                    Text(ex.symbol, fontSize = 70.sp)
                }
            }
            item {
                Text(ex.group.uppercase(), color = Red, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp)
                Text(ex.name, color = Snow, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                Text(ex.description, color = Muted, fontSize = 12.sp)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Metric("SÉRIES", ex.sets.toString(), Modifier.weight(1f))
                    Metric("REPS", ex.reps, Modifier.weight(1f))
                    Metric("EQUIPAMENTO", ex.equipment, Modifier.weight(1f))
                }
            }
            item {
                Text("COMO EXECUTAR", color = Snow, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                ex.instructions.forEachIndexed { index, instruction ->
                    Row(Modifier.padding(top = 11.dp), verticalAlignment = Alignment.Top) {
                        Box(Modifier.size(25.dp).clip(CircleShape).background(Color(0xFF3A171D)), contentAlignment = Alignment.Center) {
                            Text((index + 1).toString(), color = Red, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(instruction, color = Muted, fontSize = 12.sp, modifier = Modifier.padding(start = 10.dp).weight(1f))
                    }
                }
            }
            item {
                Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Panel).padding(15.dp)) {
                    Text("CONTROLE DE SÉRIES", color = Snow, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Row(Modifier.padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("Séries concluídas", color = Muted, fontSize = 10.sp)
                            Text(completed.toString() + " / " + ex.sets.toString(), color = Snow, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(onClick = { if (completed < ex.sets) completed++ }, enabled = completed < ex.sets, colors = ButtonDefaults.buttonColors(containerColor = Red)) {
                            Text("FINALIZAR SÉRIE", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            item {
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Panel).padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("DESCANSO", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(String.format("%02d:%02d", seconds / 60, seconds % 60), color = Snow, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(onClick = { seconds = 60; timerRunning = false }) { Text("RESET", color = Snow) }
                    Spacer(Modifier.width(5.dp))
                    Button(onClick = { if (seconds == 0) seconds = 60; timerRunning = !timerRunning }, colors = ButtonDefaults.buttonColors(containerColor = Red)) {
                        Text(if (timerRunning) "PAUSAR" else "INICIAR", fontSize = 9.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun Metric(label: String, value: String, modifier: Modifier) {
    Column(modifier.clip(RoundedCornerShape(12.dp)).background(Panel).padding(10.dp)) {
        Text(label, color = Muted, fontSize = 8.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(5.dp))
        Text(value, color = Snow, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

@Composable
private fun BottomNavigation(selected: Int, onSelect: (Int) -> Unit) {
    NavigationBar(containerColor = Color(0xFF141417), contentColor = Snow) {
        val navItems = listOf(
            Triple("Início", Icons.Default.Home, 0),
            Triple("Exercícios", Icons.Default.FitnessCenter, 1),
            Triple("Meu treino", Icons.Default.CalendarMonth, 2)
        )
        navItems.forEach { item ->
            NavigationBarItem(
                selected = selected == item.third,
                onClick = { onSelect(item.third) },
                icon = { Icon(item.second, contentDescription = item.first) },
                label = { Text(item.first, fontSize = 9.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Red, selectedTextColor = Red,
                    indicatorColor = Color(0xFF35171C),
                    unselectedIconColor = Muted, unselectedTextColor = Muted
                )
            )
        }
    }
}
