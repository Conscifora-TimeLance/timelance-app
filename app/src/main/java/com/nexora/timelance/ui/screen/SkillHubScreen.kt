package com.nexora.timelance.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nexora.timelance.R
import com.nexora.timelance.data.dto.SkillDto
import com.nexora.timelance.data.service.SkillServiceImpl
import com.nexora.timelance.domain.model.entity.Skill
import com.nexora.timelance.domain.service.SkillService
import com.nexora.timelance.ui.components.SkillItem
import com.nexora.timelance.ui.components.button.ButtonPrimary
import com.nexora.timelance.ui.components.navigation.AppDestinations
import com.nexora.timelance.ui.theme.ButtonBackColorLight
import com.nexora.timelance.ui.theme.ButtonTextColorLight
import com.nexora.timelance.ui.theme.PrimaryAccentColorLight
import com.nexora.timelance.ui.theme.SecondColorLight
import com.nexora.timelance.ui.theme.TimelanceTheme
import kotlinx.coroutines.launch
import java.util.UUID


@Preview(showBackground = true)
@Composable
private fun PreviewScreen() {
    TimelanceTheme {
        val skillService = SkillServiceImpl()
//        skillService.createSkill(Skill(UUID.randomUUID().toString(),
//            name = "Java",
//            timeTotalSeconds = 0))
        SkillHubScreen(
            rememberNavController(),
            onClickItemSkillNavigationToDetails = { println("Preview: called") },
            skillService
        )
    }
}

data class SkillHubState(
    val skills: List<Skill> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@Composable
fun SkillHubScreen(
    navController: NavHostController,
    onClickItemSkillNavigationToDetails: (skillId: String) -> Unit,
    skillService: SkillService,
) {
    var state by remember { mutableStateOf(SkillHubState()) }
    val scope = rememberCoroutineScope()
    val skillDtoItems = skillService.getAllSkills()

    LaunchedEffect(key1 = skillService) {
        scope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                val skills = skillService.getAllSkills()
                state = state.copy(skills = skills, isLoading = false)
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = "Ошибка загрузки: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            ButtonPrimary(
                containerColor = ButtonBackColorLight,
                contentColor = ButtonTextColorLight,
                contentDescription = "",
                onClick = { navController.navigate(AppDestinations.ROUTE_SKILL_ADD_SCREEN) },
                icon = R.drawable.add,
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            FilterSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                state.isLoading -> {
                    LoadingState(modifier = Modifier.weight(1f)) // weight(1f) чтобы занять оставшееся место
                }
                state.error != null -> {
                    ErrorState(
                        errorMessage = state.error!!,
                        onRetry = {
                            scope.launch {
                                state = state.copy(isLoading = true, error = null)
                                try {
                                    val skills = skillService.getAllSkills()
                                    state = state.copy(skills = skills, isLoading = false)
                                } catch (e: Exception) {
                                    state = state.copy(
                                        isLoading = false,
                                        error = "Ошибка загрузки: ${e.message}"
                                    )
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                state.skills.isEmpty() && !state.isLoading -> {
                    EmptyState(
                        onAddSkillClick = {  },
                        modifier = Modifier.weight(1f)
                    )
                }
                else -> {
                    SkillList(
                        skills = state.skills,
                        onSkillItemClick = { skillId -> onClickItemSkillNavigationToDetails(skillId) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
@Composable
private fun FilterSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Filter by something", // "Filter by something: " - в ресурсы
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.width(8.dp))
        // TODO: Реализовать UI фильтров (чипы, дропдауны и т.д.)
        Image( // Это пока просто заглушка, как я понимаю
            painter = painterResource(id = R.drawable.skill_hub), // Убедитесь, что ресурс существует
            contentDescription = null, // Добавьте описание, если оно не чисто декоративное
            modifier = Modifier
                .size(32.dp) // Уменьшил размер для строки фильтра
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun EmptyState(onAddSkillClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No skills available",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SkillList(
    skills: List<Skill>,
    onSkillItemClick: (skillId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(), // Занимает все доступное пространство (внутри Column с weight)
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), // Отступы для элементов списка
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(skills, key = { it.id}) { skill ->
            SkillItem(
                name = skill.name,
                timeTotalSeconds = skill.timeTotalSeconds,
                onClickNavigationToDetails = {
                    onSkillItemClick(skill.id)
                },
                groupTags = emptyList()
            )
        }
    }
}

