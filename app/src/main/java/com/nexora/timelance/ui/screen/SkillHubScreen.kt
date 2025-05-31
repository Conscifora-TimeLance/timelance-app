package com.nexora.timelance.ui.screen

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nexora.timelance.R
import com.nexora.timelance.data.repository.list.SkillRepositoryImpl
import com.nexora.timelance.data.repository.list.TagRepositoryImpl
import com.nexora.timelance.data.repository.list.HistorySkillRepositoryImpl
import com.nexora.timelance.data.service.SkillServiceImpl
import com.nexora.timelance.domain.model.entity.Skill
import com.nexora.timelance.domain.repository.SkillRepository
import com.nexora.timelance.domain.service.SkillService
import com.nexora.timelance.ui.components.SkillItem
import com.nexora.timelance.ui.components.navigation.AppDestinations
import com.nexora.timelance.ui.theme.TimelanceTheme
import java.util.UUID


@Preview(showBackground = true)
@Composable
private fun PreviewScreen() {
    TimelanceTheme {
        val skillService = SkillServiceImpl()
        SkillHubScreen(rememberNavController(), skillService)
    }
}


@Composable
fun SkillHubScreen(
    navController: NavHostController,
    skillService: SkillService,
) {

    val scope = rememberCoroutineScope()

    val skillDtoItems = skillService.getAllSkills()

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

            Button(onClick = { navController.navigate(AppDestinations.ROUTE_SKILL_ADD_SCREEN) }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.play),
                        contentDescription = "Home Icon",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // TODO CHANGE TO SOMETHING MORE RELIABLE
        when {
            skillDtoItems == null -> {
                LoadingIndicator()
            }

            else -> {
                LazyColumn {
                    items(skillDtoItems) { skill ->
                        SkillItem(
                            name = skill.name,
                            groupTags = emptyList(),
                            timeTotalSeconds = skill.timeTotalSeconds
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

