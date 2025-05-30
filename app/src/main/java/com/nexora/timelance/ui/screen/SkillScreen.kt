package com.nexora.timelance.ui.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexora.timelance.R
import com.nexora.timelance.ui.model.DailyStat
import com.nexora.timelance.domain.model.entity.Tag
import com.nexora.timelance.ui.components.SkillGraph
import com.nexora.timelance.ui.theme.PrimaryAccentColorLight
import com.nexora.timelance.ui.theme.SecondAccentColorLight
import com.nexora.timelance.ui.theme.TimelanceTheme
import kotlinx.coroutines.launch
import java.util.UUID

class SkillScreen {

    @Preview(showBackground = true)
    @Composable
    fun PreviewScreen() {
        TimelanceTheme {
            ShowSkillScreen()
        }
    }

}

@Composable
fun ShowSkillScreen () {
    val skillGraph : SkillGraph = SkillGraph()

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
    val count = remember{ mutableStateOf(0) }


    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
//            .statusBarsPadding()
    ) {
        Text("Name of skill", style = MaterialTheme.typography.bodyMedium)

        data class CarouselItem(
            val id: Int,
            @DrawableRes val imageResId: Int,
            @StringRes val contentDescriptionResId: Int
        )

        val groupTags =
            listOf(
                Tag(UUID.randomUUID().toString(), "Kotlin"),
                Tag(UUID.randomUUID().toString(), "Android"),
                Tag(UUID.randomUUID().toString(), "Programming"),
            )

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
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(PrimaryAccentColorLight)
                        .padding(5.dp)
                )
            }

        }

        Text("Last 7 days: 2m", style = MaterialTheme.typography.bodyMedium)
        Text("Last 30 days: 5.5 h", style = MaterialTheme.typography.bodyMedium)
        Text("Total: 6.30 h", style = MaterialTheme.typography.bodyMedium)

        skillGraph.StatisticsGraph(statisticsSevenDaysData)

        OutlinedCard(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(15.dp)
        ) {

            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Start Timer",
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxHeight(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    ElevatedButton(
                        onClick = {
                            scope.launch {
                                val result = snackbarHostState.showSnackbar("Timer is Started", "Ok", duration= SnackbarDuration.Short)
    //                        if(result==SnackbarResult.ActionPerformed) count.value++
                            } },
                        modifier =
                            Modifier.padding(it)
                                .fillMaxHeight()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.play),
                            contentDescription = "Skill Icon",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(2.dp))
    }
}