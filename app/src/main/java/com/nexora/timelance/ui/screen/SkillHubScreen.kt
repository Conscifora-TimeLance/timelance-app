package com.nexora.timelance.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nexora.timelance.data.model.SkillData
import com.nexora.timelance.data.model.SkillGroupData
import com.nexora.timelance.ui.components.SkillComp
import com.nexora.timelance.ui.theme.TimelanceTheme

class SkillHubScreen {

    val skillComp = SkillComp()

    @Preview(showBackground = true)
    @Composable
    fun PreviewScreen() {
        TimelanceTheme {
            Screen(rememberNavController())
        }
    }

    @Composable
    fun Screen(navController: NavHostController) {

        val scope = rememberCoroutineScope()

        val skillItems = listOf(
            SkillData("Java Backend", SkillGroupData("Java"), 100000),
            SkillData("Java Theory", SkillGroupData("Java"), 100000),
            SkillData("Java Front Desktop", SkillGroupData("Java"), 100000),
            SkillData("Java Android", SkillGroupData("Java"), 100000),
            SkillData("Java Practice", SkillGroupData("Java"), 100000),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
            ) {

                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier.height(600.dp),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = true
                ) {
                    items(skillItems) { item ->
                        skillComp.SkillItem(
                            name = item.name,
                            groupTag = item.groupTag,
                            timeTotalSeconds = item.timeTotalSeconds
                        )
                    }

                }
            }
        }
    }


}