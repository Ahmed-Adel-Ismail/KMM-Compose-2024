@file:Suppress("UNCHECKED_CAST")

package com.aismail.project

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import navigation.OnBackPressedChannel

class MainActivity : ComponentActivity() {

    private lateinit var backPressedChannel: OnBackPressedChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            App {
                backPressedChannel = it.value
            }

            BackHandler(backPressedChannel.isBackPressActive) {
                backPressedChannel.onBackPressed?.invoke()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {

}
