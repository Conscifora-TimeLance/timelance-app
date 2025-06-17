package com.nexora.timelance.ui.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexora.timelance.domain.model.entity.Tag
import com.nexora.timelance.ui.components.button.TagItem
import com.nexora.timelance.ui.theme.PrimaryAccentColorLight
import com.nexora.timelance.ui.theme.SecondAccentColorLight
import com.nexora.timelance.ui.theme.SecondColorLight
import com.nexora.timelance.ui.theme.TimelanceTheme
import com.nexora.timelance.util.TimeUtil.Companion.secondsToTrackedTime
import java.util.UUID

@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
    TimelanceTheme {
        SkillItem("Java Backend",
            listOf(Tag(UUID.randomUUID().toString(), "Java")),
            140000,
            onClickNavigationToDetails = {})
    }
}

@Composable
fun SkillItem(
    name: String,
    groupTags: List<Tag>,
    timeTotalSeconds: Long,
    onClickNavigationToDetails: () -> Unit,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        ),
        shape = CutCornerShape(10.dp),
        colors = CardColors(
            SecondColorLight,
            PrimaryAccentColorLight,
            PrimaryAccentColorLight,
            PrimaryAccentColorLight
        ),
        modifier = Modifier
            .fillMaxWidth()
//            .wrapContentHeight()
//            .background(SecondColorLight)
//            .border(1.dp, color = PrimaryAccentColorLight)
            .padding(vertical = 10.dp, horizontal = 5.dp)
            .clickable {
                onClickNavigationToDetails()
            }
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 5.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(groupTags) { item ->
                    TagItem(item)
                }

            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Timed: " + if (timeTotalSeconds != 0L) secondsToTrackedTime(timeTotalSeconds) else 0,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}