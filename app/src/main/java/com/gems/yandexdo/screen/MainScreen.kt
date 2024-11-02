package com.gems.yandexdo.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gems.yandexdo.ui.theme.YandexDOTheme

@Composable
fun MainScreen() {
    YandexDOTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Text("hello")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    YandexDOTheme {
        MainScreen()
    }
}