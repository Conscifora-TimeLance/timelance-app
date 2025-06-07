package com.nexora.timelance.ui.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexora.timelance.R
import com.nexora.timelance.corotutine.TimeTracker
import com.nexora.timelance.data.dto.SkillDto
import com.nexora.timelance.domain.model.entity.HistorySkill
import com.nexora.timelance.data.service.impl.SkillServiceImpl
import com.nexora.timelance.ui.model.DailyStat
import com.nexora.timelance.domain.model.entity.Tag
import com.nexora.timelance.data.service.SkillService
import com.nexora.timelance.ui.components.SkillGraph
import com.nexora.timelance.ui.components.button.ButtonPrimary
import com.nexora.timelance.ui.theme.PrimaryAccentColorLight
import com.nexora.timelance.ui.theme.SecondAccentColorLight
import com.nexora.timelance.ui.theme.SecondColorLight
import com.nexora.timelance.ui.theme.TimelanceTheme
import com.nexora.timelance.ui.theme.TimerActiveColorLight
import com.nexora.timelance.util.TimeUtil.Companion.secondsToTime
import com.nexora.timelance.util.TimeUtil.Companion.secondsToTrackedTime
import java.time.LocalDate
import java.util.UUID


@Preview(showBackground = true)
@Composable
private fun PreviewScreen() {
    TimelanceTheme {
        val skillService = SkillServiceImpl()
        val skillTest = SkillDto(UUID.randomUUID().toString(), "Test Preview",
            listOf(Tag("1", "Backend")), 0)
        skillService.createSkill(skillTest)
        SkillDetailsScreen(skillTest.skillId, skillService)
    }
}


@Composable
fun SkillDetailsScreen(
    skillId: String?,
    skillService: SkillService
) {

    val skillData = if(skillId.isNullOrBlank()) {
        SkillDto(name = "Test", groupTags = listOf(Tag(UUID.randomUUID().toString(), "Backend")),
            timeTotalSeconds = 0)
    } else {
        skillService.getSkillBySkillId(skillId)
    }

    val skillGraph: SkillGraph = SkillGraph()

    val statisticsSevenDaysData = listOf(
        DailyStat("3 Sep", 3, 0),
        DailyStat("4 Sep", 2, 23),
        DailyStat("5 Sep", 1, 15),
        DailyStat("6 Sep", 4, 5),
        DailyStat("7 Sep", 0, 45),
        DailyStat("8 Sep", 2, 10),
        DailyStat("9 Sep", 3, 30)
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val count = remember { mutableStateOf(0) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
//            .statusBarsPadding()
    ) {
        Text(skillData.name, style = MaterialTheme.typography.titleLarge)

        data class CarouselItem(
            val id: Int,
            @DrawableRes val imageResId: Int,
            @StringRes val contentDescriptionResId: Int
        )

        val groupTags = skillData.groupTags

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 8.dp)
        ) {
            items(groupTags) { item ->
                Text(
                    text = item.name,
                    color = SecondAccentColorLight,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(PrimaryAccentColorLight)
                        .padding(5.dp)
                )
            }

        }

        Text("Skill Details", style = MaterialTheme.typography.titleMedium)
        Text("Total: ${secondsToTrackedTime(skillData.timeTotalSeconds)}")

        skillGraph.StatisticsGraph(statisticsSevenDaysData)

        val time = remember { mutableLongStateOf(0) }
        val tracker = remember { TimeTracker() }

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonPrimary(
                onClick = {
                    if (tracker.isRunning) {
                        tracker.stop()
                        skillService.addTrackHistoryBySkillId(HistorySkill(UUID.randomUUID().toString(),
                            skillData.skillId, time.longValue, LocalDate.now()))
                    } else {
                        tracker.start(time)
                    }
                },
                containerColor = PrimaryAccentColorLight,
                contentColor = Color.White,
                icon = if (tracker.isRunning) R.drawable.pause else R.drawable.play,
                textContent = if (tracker.isRunning) "Pause" else "Start"
            )

            if (tracker.isRunning) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = secondsToTime(time.longValue),
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier
                            .widthIn(min = 400.dp)
                            .heightIn(min = 150.dp)
                            .background(
                                color = if (tracker.isRunning) TimerActiveColorLight else SecondColorLight,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 100.dp, vertical = 40.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
        Spacer(Modifier.height(2.dp))
    }
}