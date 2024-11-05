package com.gems.yandexdo.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gems.yandexdo.R
import com.gems.yandexdo.navigation.NavigationItem
import com.gems.yandexdo.ui.theme.YandexDOTheme




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    YandexDOTheme {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        var visibleState by remember { mutableStateOf(true) }
        val ic = if (visibleState) painterResource(id = R.drawable.ic_visibility) else painterResource(id = R.drawable.ic_no_visibility)

        val checkedStates = remember { mutableStateListOf(*Array(100) { false }) }

        val done by remember { derivedStateOf { checkedStates.count { it } } }


        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            color = MaterialTheme.colorScheme.primary
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    LargeTopAppBar(
                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = if (scrollBehavior.state.collapsedFraction > 0.5f) {
                                    Alignment.CenterVertically
                                } else {
                                    Alignment.Bottom
                                }
                            ) {
                                Column {
                                    Text(
                                        "Мои дела",
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = if (scrollBehavior.state.collapsedFraction > 0.5f) {
                                            24.sp
                                        } else {
                                            38.sp
                                        }
                                    )

                                    if (scrollBehavior.state.collapsedFraction < 0.5f) {
                                        Text(
                                            text = "Done - $done",
                                            fontSize = 20.sp,
                                            color = Color(0x4D000000)
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { visibleState = !visibleState }
                                ) {
                                    Icon(
                                        painter = ic,
                                        contentDescription = "Visibility",
                                        tint = Color(0xFF007AFF)
                                    )
                                }
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navController.navigate(NavigationItem.Task.route) },
                        containerColor = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                }
            ) { innerPadding ->
                Card(
                    modifier = Modifier,
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color(0xFFFFFFFF))
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                    ) {
                        itemsIndexed(checkedStates) { index, isChecked ->
                            EachTask(index, isChecked, checkedStates)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EachTask(index: Int, isChecked: Boolean, checkedStates: SnapshotStateList<Boolean>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { isChecked ->
                checkedStates[index] = isChecked
            }
        )
        Text(text = "Task $index")
    }
}




@Preview(showBackground = true)
@Composable
fun MainPreview() {
    YandexDOTheme {
        MainScreen(navController = rememberNavController())
    }
}

@Preview
@Composable
fun MainDarkPreview() {
    YandexDOTheme(darkTheme = true) {
        MainScreen(navController = rememberNavController())
    }
}