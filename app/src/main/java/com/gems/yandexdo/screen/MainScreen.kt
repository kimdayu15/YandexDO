package com.gems.yandexdo.screen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import com.gems.yandexdo.data.TodoItem
import com.gems.yandexdo.data.TodoItemsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, repository: TodoItemsRepository) {
    YandexDOTheme {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        var visibleState by remember { mutableStateOf(true) }
        val ic =
            if (visibleState) painterResource(id = R.drawable.ic_visibility) else painterResource(id = R.drawable.ic_no_visibility)

        val tasks = remember { mutableStateListOf<TodoItem>() }
        LaunchedEffect(Unit) {
            tasks.addAll(repository.getTasks())
        }
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
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    if (scrollBehavior.state.collapsedFraction < 0.5f) {
                                        Text(
                                            text = "Done - ${tasks.count { it.isCompleted }}",
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
                        onClick = { navController.navigate(NavigationItem.TaskScreen.route) },
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
                        items(tasks) { task ->
                            EachTask(task, repository, tasks, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EachTask(
    task: TodoItem,
    repository: TodoItemsRepository,
    tasks: MutableList<TodoItem>,
    navController: NavHostController
) {
    val isChecked = remember { mutableStateOf(task.isCompleted) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked.value,
            onCheckedChange = { checked ->
                isChecked.value = checked
                val updatedTask = task.copy(isCompleted = checked)
                val index = tasks.indexOf(task)
                if (index >= 0) {
                    tasks[index] = updatedTask
                    repository.updateTask(updatedTask)
                }
            }
        )
        Text(
            text = task.text,
            modifier = Modifier.padding(start = 8.dp),
            style = LocalTextStyle.current.copy(
                textDecoration = if (isChecked.value) TextDecoration.LineThrough else TextDecoration.None
            ),
            maxLines = 3
        )
        IconButton(onClick = {
            // Navigate to the task details screen and pass the task ID
            navController.navigate("${NavigationItem.TaskScreen.route}/${task.id}")
        }) {
            Icon(painter = painterResource(R.drawable.ic_info), contentDescription = "Task Info")
        }
    }
}




class MockTodoItemsRepository(context: Context) : TodoItemsRepository(context) {
    override fun getTasks(): List<TodoItem> {
        return listOf(
            TodoItem(
                "1",
                "Sample Task 1",
                1,
                0L,
                false,
                System.currentTimeMillis(),
                System.currentTimeMillis()
            ),
            TodoItem(
                "2",
                "Sample Task 2",
                2,
                0L,
                true,
                System.currentTimeMillis(),
                System.currentTimeMillis()
            ),
            TodoItem(
                "3",
                "Sample Task 3",
                0,
                0L,
                false,
                System.currentTimeMillis(),
                System.currentTimeMillis()
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MainPreview() {
    val mockRepository = MockTodoItemsRepository(context = LocalContext.current)
    YandexDOTheme {
        MainScreen(navController = rememberNavController(), repository = mockRepository)
    }
}

@Preview
@Composable
fun MainDarkPreview() {
    val mockRepository = MockTodoItemsRepository(context = LocalContext.current)
    YandexDOTheme(darkTheme = true) {
        MainScreen(navController = rememberNavController(), repository = mockRepository)
    }
}