package com.nexora.timelance.ui.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexora.timelance.data.repository.list.SkillRepositoryImpl
import com.nexora.timelance.data.repository.list.TagRepositoryImpl
import com.nexora.timelance.domain.model.entity.Skill
import com.nexora.timelance.domain.repository.SkillRepository
import com.nexora.timelance.ui.theme.TimelanceTheme
import java.util.UUID

class AddSkillScreen {

    @Preview(showBackground = true)
    @Composable
    fun PreviewScreen() {
        TimelanceTheme {
            val tagRepository = TagRepositoryImpl()
            val skillRepository = SkillRepositoryImpl(tagRepository)
            ShowSkillAddScreen(skillRepository)
        }
    }

}

@Composable
fun ShowSkillAddScreen (
    skillRepository: SkillRepository
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
    ) {
        Text("Add Skill", style = MaterialTheme.typography.bodyMedium)


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


        Button(onClick = {skillRepository.saveSkill(
            Skill(UUID.randomUUID().toString(), textState, 0)
        )}) {
            Text("SAVE")
        }

        Spacer(Modifier.height(2.dp))
    }
}