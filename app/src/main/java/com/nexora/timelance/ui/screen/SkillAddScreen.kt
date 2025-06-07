package com.nexora.timelance.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexora.timelance.R
import com.nexora.timelance.data.dto.SkillDto
import com.nexora.timelance.data.service.impl.SkillServiceImpl
import com.nexora.timelance.data.service.SkillService
import com.nexora.timelance.domain.model.entity.Tag
import com.nexora.timelance.ui.components.button.ButtonPrimary
import com.nexora.timelance.ui.theme.ButtonBackColorLight
import com.nexora.timelance.ui.theme.ButtonTextColorLight
import com.nexora.timelance.ui.theme.TimelanceTheme
import java.util.UUID


@Preview(showBackground = true)
@Composable
private fun PreviewScreen() {
    TimelanceTheme {
        val skillService = SkillServiceImpl()
        SkillAddScreen(skillService, onSkillSavedAndNavigateBack = { println("Preview: called") } )
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val state = rememberTextFieldState()

        var textState by remember { mutableStateOf("") }

        OutlinedTextField(
            value = textState,
            onValueChange = { newText -> textState = newText },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Enter skill name") },
            leadingIcon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorite Icon") },
            trailingIcon = {
                if (textState.isNotEmpty()) {
                    IconButton(onClick = { textState = "" }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear text")
                    }
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        ButtonPrimary(
            onClick = {
                skillService.createSkill(
                    SkillDto(UUID.randomUUID().toString(), textState,
                        listOf(skillService.createTag(Tag(name = "Empty"))), 0)
                )
                onSkillSavedAndNavigateBack()
            },
            containerColor = ButtonBackColorLight,
            contentColor = ButtonTextColorLight,
            contentDescription = "",
            icon = R.drawable.save,
            textContent = "SAVE"

        )

        Spacer(Modifier.height(2.dp))
    }
}