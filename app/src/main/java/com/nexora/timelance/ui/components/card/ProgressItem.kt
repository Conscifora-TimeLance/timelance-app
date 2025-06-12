package com.nexora.timelance.ui.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nexora.timelance.ui.theme.ProgressBackgroundColorLight
import com.nexora.timelance.ui.theme.ProgressFillColorLight
import com.nexora.timelance.ui.theme.SecondColorLight
import com.nexora.timelance.util.TimeUtil.Companion.secondsToTrackedTime
import kotlin.math.roundToInt

@Composable
fun ProgressItem(title: String, time: Long, /*progress: Float*/) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(SecondColorLight)
            .padding(vertical = 20.dp, horizontal = 5.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = {
                100f
            },
            modifier = Modifier.fillMaxWidth(),
            color = ProgressFillColorLight,
            trackColor = ProgressBackgroundColorLight,
        )
        Spacer(modifier = Modifier.height(4.dp))


        // val percent: String = (progress * 100).roundToInt().toString();

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = secondsToTrackedTime(time), style = MaterialTheme.typography.bodySmall)
            // In the future here will be percent from goal/timed
            // Text(text = "$percent%", style = MaterialTheme.typography.bodySmall)
        }
    }
}

