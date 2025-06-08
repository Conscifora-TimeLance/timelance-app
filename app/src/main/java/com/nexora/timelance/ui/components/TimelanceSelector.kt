package com.nexora.timelance.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.nexora.timelance.domain.model.entity.Tag
import com.nexora.timelance.ui.theme.TextColorLight
import com.nexora.timelance.ui.theme.ThirdAccentColorLight
import java.util.UUID

@Composable
fun ModernTagSelector(
    availableTags: List<String>,
    selectedTags: MutableList<Tag>,
    onTagSelected: (Tag) -> Unit,
    onTagRemoved: (Tag) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val filteredTags = if (query.isEmpty()) {
        availableTags
    } else {
        availableTags.filter { it.contains(query, ignoreCase = true) }
    }

    Column {
        OutlinedTextField(
            value = query,
            onValueChange = { newText ->
                query = newText
                expanded = true
            },
            label = { Text("Select a tag") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
        )

        if (expanded) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (filteredTags.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No matching tags") },
                        onClick = {}
                    )
                } else {
                    filteredTags.forEach { tagName ->
                        DropdownMenuItem(
                            text = { Text(tagName) },
                            onClick = {
                                if (!selectedTags.any { it.name == tagName }) {
                                    val newTag = Tag(UUID.randomUUID().toString(), tagName)
                                    selectedTags.add(newTag)
                                    onTagSelected(newTag)
                                }
                                query = ""
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedTags.forEach { tag ->
                TagChip(
                    tag = tag,
                    onRemove = {
                        selectedTags.remove(tag)
                        onTagRemoved(tag)
                    }
                )
            }
        }
    }
}

@Composable
fun TagChip(
    tag: Tag,
    onRemove: () -> Unit
) {
    Surface(
        color = ThirdAccentColorLight,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = tag.name,
                style = MaterialTheme.typography.labelMedium,
                color = TextColorLight
            )
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove tag",
                    tint = TextColorLight
                )
            }
        }
    }
}