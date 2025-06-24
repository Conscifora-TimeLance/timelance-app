package com.nexora.timelance.ui.components.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nexora.timelance.data.dto.SkillDto
import com.nexora.timelance.ui.viewmodel.GlobalSearchUiState


@Composable
fun GlobalSearchResultsOverlay(
    uiState: GlobalSearchUiState,
    onSkillClick: (SkillDto) -> Unit,
    onDismissRequest: () -> Unit
) {
    AnimatedVisibility(
        visible = uiState.isSearchActive,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // Полупрозрачный фон
                .clickable(onClick = onDismissRequest, indication = null, interactionSource = remember { MutableInteractionSource() }) // Клик для закрытия
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f) // Занимает, например, 70% высоты экрана
                    .align(Alignment.TopCenter) // Позиционируем сверху
                    .padding(top = 0.dp), // Может быть нужно, если TopAppBar имеет высоту
                // shape = MaterialTheme.shapes.medium, // Опционально: скругление углов
                tonalElevation = 8.dp
            ) {
                Column {
                    if (uiState.isLoading) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    } else if (uiState.error != null) {
                        Text(
                            text = uiState.error,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    if (uiState.searchQuery.isNotBlank() && uiState.searchResults.isEmpty() && !uiState.isLoading && uiState.error == null) {
                        Text(
                            text = "No skills found for '${uiState.searchQuery}'",
                            modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(uiState.searchResults, key = { it.skillId }) { skill ->
                            SearchResultItem(skill = skill, onClick = { onSkillClick(skill) })
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(
    skill: SkillDto,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(skill.name) },
        supportingContent = {
            if (skill.groupTags.isNotEmpty()) {
                Text(skill.groupTags.joinToString { it.name }, style = MaterialTheme.typography.bodySmall)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}