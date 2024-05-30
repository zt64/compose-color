package dev.zt64.compose.color

import androidx.compose.ui.window.ComposeUIViewController
import dev.zt64.compose.color.sample.Sample
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    Sample()
}