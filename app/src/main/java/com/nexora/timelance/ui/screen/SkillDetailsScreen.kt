package com.nexora.timelance.ui.screen

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.nexora.timelance.R
import com.nexora.timelance.corotutine.TimeTracker
import com.nexora.timelance.data.dto.SkillDto
import com.nexora.timelance.domain.model.entity.HistorySkill
import com.nexora.timelance.data.service.impl.SkillServiceImpl
import com.nexora.timelance.domain.model.entity.Tag
import com.nexora.timelance.data.service.SkillService
import com.nexora.timelance.data.service.TimerForegroundService
import com.nexora.timelance.ui.components.stat.StatisticsGraph
import com.nexora.timelance.ui.components.button.ButtonPrimary
import com.nexora.timelance.ui.components.button.TagItem
import com.nexora.timelance.ui.components.card.HistoryItem
import com.nexora.timelance.ui.theme.ButtonBackActiveColorLight
import com.nexora.timelance.ui.theme.PrimaryAccentColorLight
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
        val tagAndroid = skillService.createTag(Tag(UUID.randomUUID().toString(), "Android"))
        val tagBackend = skillService.createTag(Tag(UUID.randomUUID().toString(), "Backend"))

        val createSkill =
            skillService.createSkill(SkillDto("1", "Java", listOf(tagBackend), 1400000))
        val createSkill1 =
            skillService.createSkill(SkillDto("2", "Kotlin", listOf(tagAndroid), 140000))

        skillService.addTrackHistoryBySkillId(
            HistorySkill(
                skillId = createSkill.id, date = LocalDate.now(),
                timeTrackedSeconds = createSkill.timeTotalSeconds
            )
        )

        skillService.addTrackHistoryBySkillId(
            HistorySkill(
                skillId = createSkill1.id, date = LocalDate.now(),
                timeTrackedSeconds = createSkill1.timeTotalSeconds
            )
        )

        SkillDetailsScreen(createSkill.id, skillService, { println() })
    }
}


@Composable
fun SkillDetailsScreen(
    skillId: String?,
    skillService: SkillService,
    onTagToSkillHubFilter: (tagName : String) -> Unit
) {

    var skillDto by remember {
        mutableStateOf(
            if (skillId.isNullOrBlank()) {
                SkillDto(
                    name = "Test", groupTags = listOf(Tag(UUID.randomUUID().toString(), "Backend")),
                    timeTotalSeconds = 0
                )
            } else {
                skillService.getSkillById(skillId)
            }
        )
    }
    var historyDto by remember {
        mutableStateOf(
            if (skillId.isNullOrBlank()) {
                emptyList()
            } else {
                skillService.getHistoryBySkillId(skillId).histories
            }
        )
    }

    LaunchedEffect(skillId) {
        skillDto = if (skillId.isNullOrBlank()) {
            SkillDto(
                name = "Test", groupTags = listOf(Tag(UUID.randomUUID().toString(), "Backend")),
                timeTotalSeconds = 0
            )
        } else {
            skillService.getSkillById(skillId)
        }
        historyDto = if (skillId.isNullOrBlank()) {
            emptyList()
        } else {
            skillService.getHistoryBySkillId(skillId).histories
        }
    }


    val statisticsSevenDaysData = if (skillId.isNullOrBlank()) {
        emptyList()
    } else {
        skillService.getHistoryBySkillId(skillId).histories
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(skillDto.name, style = MaterialTheme.typography.titleLarge)

        SectionGroupTags(skillDto.groupTags, onTagToSkillHubFilter)
        SectionDetails(skillDto)
        StatisticsGraph(statisticsSevenDaysData)
        SectionTracking(
            skillService = skillService,
            skillDto = skillDto,
            onTimerStop = {
                skillDto = skillService.getSkillById(skillDto.skillId)
                historyDto = skillService.getHistoryBySkillId(skillDto.skillId).histories
            },
            context = LocalContext.current
        )
        Spacer(Modifier.height(2.dp))
        SectionHistory(historyDto)

    }
}

@Composable
private fun SectionGroupTags(groupTags: List<Tag>,
                             onTagToSkillHubFilter: (tagName : String) -> Unit = {}) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp)
    ) {
        items(groupTags) { item ->
            TagItem(item, onTagToSkillHubFilter)
        }

    }
}

@Composable
private fun SectionDetails(skillData: SkillDto) {
    // Text("Skill Details", style = MaterialTheme.typography.titleMedium)
    if (skillData.timeTotalSeconds != 0L) {
        Text("Total: ${secondsToTrackedTime(skillData.timeTotalSeconds)}")
    }
}

@Composable
private fun SectionTracking(
    skillService: SkillService,
    skillDto: SkillDto,
    onTimerStop: () -> Unit,
    context: Context,
) {
    var elapsedTime by remember { mutableLongStateOf(0) }
    var isTracking by remember { mutableStateOf(false) }
    var currentTrackingSkillId by remember { mutableStateOf<String?>(null) }
    val timeUpdateReceiver = remember { TimeUpdateReceiver() }

    // Check if service is already running for this skill
    LaunchedEffect(Unit) {
        val isServiceRunning = isServiceRunning(context, TimerForegroundService::class.java)
        isTracking = isServiceRunning
    }

    DisposableEffect(Unit) {
        val filter = IntentFilter(TimerForegroundService.ACTION_UPDATE_TIME)
        context.registerReceiver(timeUpdateReceiver, filter)

        onDispose {
            context.unregisterReceiver(timeUpdateReceiver)
        }
    }

    LaunchedEffect(timeUpdateReceiver) {
        snapshotFlow { timeUpdateReceiver.currentTime to timeUpdateReceiver.currentSkillId }
            .collect { (time, skillId) ->
                elapsedTime = time
                currentTrackingSkillId = skillId
                isTracking = skillId == skillDto.skillId
            }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        ButtonPrimary(
            onClick = {
                if (isTracking) {
                    // Stop tracking only if this is the currently tracked skill
                    if (currentTrackingSkillId == skillDto.skillId) {
                        val stopIntent = Intent(context, TimerForegroundService::class.java).apply {
                            action = TimerForegroundService.ACTION_STOP_TIMER
                            putExtra(TimerForegroundService.EXTRA_SKILL_ID, skillDto.skillId)
                        }
                        context.startService(stopIntent)

                        if (elapsedTime > 0) {
                            skillService.addTrackHistoryBySkillId(
                                HistorySkill(
                                    UUID.randomUUID().toString(),
                                    skillDto.skillId,
                                    elapsedTime,
                                    LocalDate.now()
                                )
                            )
                        }
                        elapsedTime = 0
                        onTimerStop()
                    }
                } else {
                    // Start tracking this skill
                    val startIntent = Intent(context, TimerForegroundService::class.java).apply {
                        action = TimerForegroundService.ACTION_START_TIMER
                        putExtra(TimerForegroundService.EXTRA_SKILL_ID, skillDto.skillId)
                        putExtra(TimerForegroundService.EXTRA_SKILL_NAME, skillDto.name)
                    }
                    ContextCompat.startForegroundService(context, startIntent)
                }
                isTracking = !isTracking
            },
            containerColor = if (isTracking && currentTrackingSkillId == skillDto.skillId)
                ButtonBackActiveColorLight
            else
                PrimaryAccentColorLight,
            contentColor = Color.White,
            icon = if (isTracking && currentTrackingSkillId == skillDto.skillId)
                R.drawable.pause
            else
                R.drawable.play,
            textContent = if (isTracking && currentTrackingSkillId == skillDto.skillId)
                "Stop"
            else
                "Start",
        )

        if (isTracking && currentTrackingSkillId == skillDto.skillId) {
            TimerDisplay(elapsedTime, true)
        }
    }
}

class TimeUpdateReceiver : BroadcastReceiver() {
    var currentTime by mutableLongStateOf(0L)
        private set
    var currentSkillId by mutableStateOf<String?>(null)
        private set

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TimerForegroundService.ACTION_UPDATE_TIME) {
            currentTime = intent.getLongExtra(TimerForegroundService.EXTRA_TIME_VALUE, 0L)
            currentSkillId = intent.getStringExtra(TimerForegroundService.EXTRA_SKILL_ID)
        }
    }
}

fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return manager.getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == serviceClass.name }
}

@Composable
private fun TimerDisplay(time: Long, isRunning: Boolean) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = secondsToTime(time),
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .widthIn(min = 400.dp)
                .heightIn(min = 100.dp)
                .background(
                    color = if (isRunning) TimerActiveColorLight else SecondColorLight,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 100.dp, vertical = 40.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SectionHistory(historyList: List<HistorySkill>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp, 8.dp)
            .height(200.dp)
    ) {
        items(historyList) { item ->
            HistoryItem(item)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = PrimaryAccentColorLight
            )
        }
    }
}