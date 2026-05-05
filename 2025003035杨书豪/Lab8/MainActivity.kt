package com.example.superheroes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.superheroes.ui.theme.SuperheroesTheme

/**
 * 入口 Activity。
 *
 * 为什么这里要先设置 WindowCompat.setDecorFitsSystemWindows(window, false)？
 * 因为这是 edge-to-edge 的基础设置，意思是让应用内容可以延伸到状态栏/导航栏下面，
 * 这样界面更现代，也方便我们自己控制系统栏视觉。
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 让应用支持 edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            SuperheroesTheme(window = window) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HeroesApp()
                }
            }
        }
    }
}

@Composable
fun HeroesApp() {
    HeroesScreen()
}