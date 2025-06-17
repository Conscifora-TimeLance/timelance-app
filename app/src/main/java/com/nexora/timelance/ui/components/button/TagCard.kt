package com.nexora.timelance.ui.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexora.timelance.domain.model.entity.Tag
import com.nexora.timelance.ui.theme.TextColorLight
import com.nexora.timelance.ui.theme.ThirdAccentColorLight

@Preview
@Composable
fun TagItem (tag: Tag = Tag("1", "Java"),
             onClick: (tagName: String) -> Unit = {}) {
    Text(
        text = tag.name,
        color = TextColorLight,
        style = MaterialTheme.typography.titleSmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(ThirdAccentColorLight)
            .clickable {
                onClick(tag.id)
            }
            .padding(8.dp, 5.dp)
    )
}