package com.nexora.timelance.ui.components.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexora.timelance.R
import com.nexora.timelance.domain.model.entity.HistorySkill
import com.nexora.timelance.ui.components.button.ButtonIcon
import com.nexora.timelance.ui.theme.PrimaryAccentColorLight
import com.nexora.timelance.ui.theme.SecondColorLight
import com.nexora.timelance.util.TimeUtil.Companion.secondsToTrackedTime
import java.time.LocalDate

@Composable
fun HistoryItem(history: HistorySkill) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 5.dp)
            .clickable { },
        elevation = CardDefaults.cardElevation(),
        shape = CutCornerShape(5.dp),
        colors = CardColors(
            SecondColorLight,
            PrimaryAccentColorLight,
            PrimaryAccentColorLight,
            PrimaryAccentColorLight
        ),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = history.date.toString(),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = secondsToTrackedTime(history.timeTackedSeconds)
                )
            }

            ButtonIcon(
                onClick = { println("") },
                icon = R.drawable.dots_vertical,
                buttonSize = 25.dp,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    HistoryItem(HistorySkill(date = LocalDate.now(), skillId = "1", timeTackedSeconds = 1400))
}