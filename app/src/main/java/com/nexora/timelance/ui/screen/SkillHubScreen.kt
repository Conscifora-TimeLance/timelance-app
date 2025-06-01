package com.nexora.timelance.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nexora.timelance.R
import com.nexora.timelance.data.service.SkillServiceImpl
import com.nexora.timelance.domain.model.entity.Skill
import com.nexora.timelance.domain.service.SkillService
import com.nexora.timelance.ui.components.SkillItem
import com.nexora.timelance.ui.components.button.ButtonPrimary
import com.nexora.timelance.ui.components.navigation.AppDestinations
import com.nexora.timelance.ui.theme.PrimaryAccentColorLight
import com.nexora.timelance.ui.theme.SecondColorLight
import com.nexora.timelance.ui.theme.TimelanceTheme
import kotlinx.coroutines.launch


@Preview(showBackground = true)
@Composable
private fun PreviewScreen() {
    TimelanceTheme {
        val skillService = SkillServiceImpl()
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

//        var skillItems by remember { mutableStateOf<List<SkillData>?>(null) } // Start with null to indicate loading
//
//        val coroutineScope = rememberCoroutineScope()
//
//        // Load data when the composable is first displayed
//        LaunchedEffect(Unit) {
//            coroutineScope.launch(Dispatchers.Default) {
//                // Simulate a delay to mimic a long-running task
//                delay(1000)
//                TODO HERE IS LOAD DATA
//                skillItems = data
//            }
//        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {

        Text(
            text = "Skill Hub"
        )

        // TODO FILTER OF SKILLS
        // BY TIME
        // BY GROUP

        Row(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = "Filter by something: ",
                style = MaterialTheme.typography.bodySmall
            )

            Image(
                painter = painterResource(id = R.drawable.skill_hub),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            ButtonPrimary(
                navController, AppDestinations.ROUTE_SKILL_ADD_SCREEN,
                containerColor = SecondColorLight,
                contentColor = PrimaryAccentColorLight,
                contentDescription = "Add New Skill", R.drawable.add
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // TODO CHANGE TO SOMETHING MORE RELIABLE
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Ошибка: ${state.error}", color = MaterialTheme.colorScheme.error)
                        Button(onClick = {
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
                        }) {
                            Text("Повторить")
                        }
                    }
                }
            }

            state.skills.isEmpty() && !state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Нет доступных навыков. Добавьте новый!")
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.skills, key = { it.id }) { skill ->
                        SkillItem(
                            name = skill.name,
                            groupTags = emptyList(),
                            timeTotalSeconds = skill.timeTotalSeconds,
                            onClickNavigationToDetails = {
                                onClickItemSkillNavigationToDetails(skill.id)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Text(text = "Loading...")
}

