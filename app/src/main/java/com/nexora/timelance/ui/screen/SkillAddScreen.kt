package com.nexora.timelance.ui.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexora.timelance.R
import com.nexora.timelance.data.dto.SkillDto
import com.nexora.timelance.data.service.impl.SkillServiceImpl
import com.nexora.timelance.domain.model.entity.Tag
import com.nexora.timelance.ui.components.ModernTagSelector
import com.nexora.timelance.ui.components.SearchableExposedDropdownMenuBox
import com.nexora.timelance.ui.components.TagChip
import com.nexora.timelance.ui.components.button.ButtonPrimary
import com.nexora.timelance.ui.theme.ButtonBackColorLight
import com.nexora.timelance.ui.theme.ButtonTextColorLight
import com.nexora.timelance.ui.theme.TextColorLight
import com.nexora.timelance.ui.theme.ThirdAccentColorLight
import com.nexora.timelance.ui.theme.TimelanceTheme
import java.util.UUID


@Preview(showBackground = true)
@Composable
private fun PreviewScreen() {
    TimelanceTheme {
        val skillService = SkillServiceImpl()
        SkillAddScreen(skillService, onSkillSavedAndNavigateBack = { println("Preview: called") })
    }
}

@Composable
fun SkillAddScreen(
    skillService: SkillServiceImpl,
    onSkillSavedAndNavigateBack: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val state = rememberTextFieldState()

        var textState by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }
        val selectedTags = remember { mutableStateListOf<Tag>() }
        val availableTags = skillService.getAllTags()

        Text("Skill name", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = textState,
            onValueChange = { newText -> textState = newText },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(
                        id = R.drawable.braille
                    ),
                    contentDescription = "Favorite Icon",
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                if (textState.isNotEmpty()) {
                    IconButton(onClick = { textState = "" }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear text")
                    }
                }
            }
        )

        Text("Tags", style = MaterialTheme.typography.titleLarge)

        SearchableExposedDropdownMenuBox(
            availableTags, selectedTags,
            onTagSelected = { /* Обработка выбора */ },
            onTagRemoved = { /* Обработка удаления */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )

//        ModernTagSelector(
//            availableTags, selectedTags,
//            onTagSelected = { /* Обработка выбора */ },
//            onTagRemoved = { /* Обработка удаления */ },
//        )

        Spacer(modifier = Modifier.weight(1f))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            ButtonPrimary(
                onClick = {
                    skillService.createSkill(
                        SkillDto(
                            UUID.randomUUID().toString(), textState,
                            selectedTags, 0
                        )
                    )
                    onSkillSavedAndNavigateBack()
                },
                containerColor = ButtonBackColorLight,
                contentColor = ButtonTextColorLight,
                contentDescription = "",
                icon = R.drawable.save,
                textContent = "SAVE",
                enabled = textState.length >= 3
            )
        }

        Spacer(Modifier.height(2.dp))
    }
}