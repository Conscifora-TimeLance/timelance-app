package com.nexora.timelance.ui.components.stat

import android.inputmethodservice.Keyboard.Row
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.Alignment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexora.timelance.domain.model.entity.HistorySkill

import com.nexora.timelance.ui.model.DailyStat;
import com.nexora.timelance.ui.theme.ProgressFillColorLight
import com.nexora.timelance.ui.theme.TextColorLight
import java.time.format.DateTimeFormatter


@Composable
fun StatisticsGraph(histories: List<HistorySkill>) {
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd\tMMM")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    ) {
        if (histories.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val mapGroupByDate = histories.groupBy { it.date }
                val maxSeconds =
                    mapGroupByDate.map { value -> value.value.sumOf { it.timeTrackedSeconds } }
                        .maxOrNull() ?: 0
                val sortedDates = mapGroupByDate.keys.toList().sortedDescending()

                items(sortedDates.size) { index ->
                    val date = sortedDates[index]
                    val dailyStat = DailyStat.createStat(date, mapGroupByDate[date]!!)
                    val height = DailyStat.calculateHeight(dailyStat.totalSeconds, maxSeconds)

                    Column(
                        modifier = Modifier.width(35.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = date.format(dateFormatter),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Box(
                            modifier = Modifier
                                .width(15.dp)
                                .height(height.dp)
                                .background(ProgressFillColorLight)
                        )

                        Text(
                            text = if (dailyStat.totalHours > 0) {
                                "${dailyStat.totalHours}h ${dailyStat.totalMinutes}m"
                            } else if (dailyStat.totalMinutes > 0) {
                                "${dailyStat.totalMinutes}m"
                            } else {
                                "${dailyStat.totalSeconds}s"
                            },
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 4.dp),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Begin your journey...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextColorLight
                )
            }
        }
    }
}

    @Preview(showBackground = true)
    @Composable
    fun PreviewStatisticsGraph() {
//    val data = listOf(
//        DailyStat("3 Sep", 0),
//        DailyStat("4 Sep", 0),
//        DailyStat("5 Sep", 0),
//        DailyStat("6 Sep", 0),
//        DailyStat("7 Sep", 0),
//        DailyStat("8 Sep", 0),
//        DailyStat("9 Sep", 0)
//    )
//
//    StatisticsGraph(histories = data)
    }

