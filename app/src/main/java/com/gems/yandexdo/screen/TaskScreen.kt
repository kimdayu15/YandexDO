package com.gems.yandexdo.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gems.yandexdo.R
import com.gems.yandexdo.data.TodoItem
import com.gems.yandexdo.data.TodoItemsRepository
import com.gems.yandexdo.navigation.NavigationItem
import com.gems.yandexdo.ui.theme.YandexDOTheme
import java.text.SimpleDateFormat
import java.util.*

enum class Importance { NONE, LOW, HIGH }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(navController: NavHostController) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    YandexDOTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        var noteText by remember { mutableStateOf("") }
        var selectedDate by remember { mutableStateOf("") }
        var checked by remember { mutableStateOf(false) }
        var selectedImportance by remember { mutableStateOf(Importance.NONE) } // Add variable for importance
        val item = TodoItemsRepository(LocalContext.current)

        Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            TopAppBar(modifier = Modifier.padding(10.dp, 0.dp), title = {
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "Save",
                        modifier = Modifier.clickable {
                            if (noteText.isNotEmpty()) {
                                item.addTask(
                                    TodoItem(
                                        "1",
                                        noteText,
                                        selectedImportance.ordinal,
                                        dateFormat.parse(selectedDate)?.time,
                                        false,
                                        System.currentTimeMillis(),
                                        System.currentTimeMillis()
                                    )
                                )
                            }
                            navController.navigate(NavigationItem.Main.route)
                        })
                }
            }, navigationIcon = {
                IconButton(onClick = { navController.navigate(NavigationItem.Main.route) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close), contentDescription = null
                    )
                }
            })
        }) { values ->
            Column(
                modifier = Modifier
                    .padding(values)
                    .padding(10.dp)
            ) {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { newText -> noteText = newText },
                    placeholder = { Text("Write your note here") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 130.dp),
                    maxLines = Int.MAX_VALUE,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Importance")
                    Card {
                        var btnColor = Color.Transparent
                        Row {
                            Button(
                                onClick = {
                                    selectedImportance = Importance.LOW
                                    btnColor = Color.Red
                                }, colors = ButtonDefaults.buttonColors(btnColor)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_low),
                                    contentDescription = null
                                )
                            }
                            Button(
                                onClick = { selectedImportance = Importance.NONE },
                                colors = ButtonDefaults.buttonColors(btnColor)
                            ) {
                                Text("NO", color = Color.Black)
                            }
                            Button(
                                onClick = { selectedImportance = Importance.HIGH },
                                colors = ButtonDefaults.buttonColors(btnColor)
                            ) {
                                Text("!!", color = Color.Gray, fontSize = 20.sp)
                            }
                        }
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .height(1.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    val context = LocalContext.current
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Deadline")

                            if (selectedDate.isNotEmpty()) {
                                Text(
                                    text = "Selected Deadline: $selectedDate",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Blue
                                )
                            }
                        }
                        Switch(checked = checked, onCheckedChange = { isChecked ->
                            checked = isChecked
                            if (isChecked) {
                                showDatePickerDialog(context) { date ->
                                    selectedDate = date
                                }
                            } else {
                                selectedDate = ""
                            }
                        })
                    }
                }

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = null
                        )
                        Text(
                            "Delete",
                            color = Color.Red,
                            fontSize = 17.sp,
                            modifier = Modifier.padding(10.dp, 0.dp),
                            fontWeight = FontWeight.Medium
                        )

                    }
                }
            }
        }
    }
}


private fun showDatePickerDialog(
    context: android.content.Context, onDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context, { _, selectedYear, selectedMonth, selectedDay ->
            val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            onDateSelected(date)
        }, year, month, day
    )
    datePickerDialog.show()
}

@Preview(showBackground = true)
@Composable
fun TaskPreview() {
    YandexDOTheme {
        TaskScreen(rememberNavController())
    }
}

@Preview
@Composable
fun TaskDarkPreview() {
    YandexDOTheme(darkTheme = true) {
        TaskScreen(rememberNavController())
    }
}
