package com.nexora.timelance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.nexora.timelance.data.model.SkillData
import com.nexora.timelance.data.model.SkillGroupData
import com.nexora.timelance.ui.theme.PrimaryAccentColorLight
import com.nexora.timelance.ui.theme.ProgressBackgroundColorLight
import com.nexora.timelance.ui.theme.ProgressFillColorLight
import com.nexora.timelance.ui.theme.SecondAccentColorLight
import com.nexora.timelance.ui.theme.SecondColorLight
import com.nexora.timelance.ui.theme.TimelanceTheme
import kotlin.math.round
import kotlin.math.roundToInt

class SkillComp {

    @Preview(showBackground = true)
    @Composable
    fun PreviewScreen() {
        TimelanceTheme {
            SkillItem("Java Backend", SkillGroupData("Java"), 140000)
        }
    }

    @Composable
    fun SkillItem(name: String, groupTag: SkillGroupData, timeTotalSeconds: Int) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(SecondColorLight)
                .padding(vertical = 20.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // TODO CHANGE THIS HORROR

            val groupTags = listOf(
                SkillGroupData("Java"),
                SkillGroupData("Backend"),
                SkillGroupData("Theory"),
                SkillGroupData("Practice"),
                SkillGroupData("Practice"),
                SkillGroupData("Practice"),
                SkillGroupData("Practice"),
                SkillGroupData("Practice"),
                SkillGroupData("Practice"),
            )

            LazyHorizontalGrid (
                rows = GridCells.Fixed(1),
                Modifier.height(50.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = true
            ) {
                items(groupTags) { item ->
                    Text(
                        text = item.name,
                        color = SecondAccentColorLight,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(PrimaryAccentColorLight)
                            .padding(5.dp)
                    )
                }

            }



            Spacer(modifier = Modifier.height(4.dp))



            Text(
                text = "Timed: " + (timeTotalSeconds / 60 / 60).toString() + " hours",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

//            LinearProgressIndicator(
//                progress = {
//                    progress
//                },
//                modifier = Modifier.fillMaxWidth(),
//                color = ProgressFillColorLight,
//                trackColor = ProgressBackgroundColorLight,
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//
//
//            val percent: String = (progress * 100).roundToInt().toString();

//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(text = time, style = MaterialTheme.typography.bodySmall)
//                Text(text = "$percent%", style = MaterialTheme.typography.bodySmall)
//            }
        }
    }

}