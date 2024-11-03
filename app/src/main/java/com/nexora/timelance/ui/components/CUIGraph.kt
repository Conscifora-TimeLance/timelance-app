package com.nexora.timelance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.nexora.timelance.data.model.DailyStat;
import com.nexora.timelance.ui.theme.ProgressFillColorLight

class CUIGraph {

    @Composable
    fun StatisticsGraph(dailyStats: List<DailyStat>) {
        val maxStat = dailyStats.maxByOrNull { it.hours * 60 + it.minutes } ?: DailyStat("", 0, 0)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dailyStats.forEach { stat ->
                // Вычисляем высоту столбика
                val height = DailyStat.calculateHeight(stat.hours, stat.minutes, maxStat.hours, maxStat.minutes)

                // Создаем Column для каждого элемента
                Column(
                    modifier = Modifier.width(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Столбик
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(height.dp)
                            .background(ProgressFillColorLight)
                    )

                    // Отображаем время над столбиком
                    Text(
                        // TODO make time (hour and minutes) convert to hour
                        text = if (stat.hours > 0) {
                            "${stat.hours}h ${stat.minutes}m"
                        } else {
                            "${stat.minutes}m"
                        },
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewStatisticsGraph() {
        val data = listOf(
            DailyStat("3 Sep", 3, 0),
            DailyStat("4 Sep", 2, 23),
            DailyStat("5 Sep", 1, 15),
            DailyStat("6 Sep", 4, 5),
            DailyStat("7 Sep", 0, 45),
            DailyStat("8 Sep", 2, 10),
            DailyStat("9 Sep", 3, 30)
        )

        StatisticsGraph(dailyStats = data)
    }

}