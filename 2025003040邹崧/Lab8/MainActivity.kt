package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.model.Hero
import com.example.myapplication.model.HeroesRepository
import com.example.myapplication.ui.theme.SuperheroesTheme

// 关键：添加实验性API注解（解决TopAppBar标红）
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperheroesTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(R.string.app_name), // 无id=
                                    style = MaterialTheme.typography.displayLarge
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { paddingValues ->
                    // 直接调用HeroesScreen的HeroesList（无重复、L大写）
                    HeroesList(
                        heroes = HeroesRepository.heroes,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

// 预览函数也加注解
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    SuperheroesTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { paddingValues ->
            HeroesList(
                heroes = HeroesRepository.heroes,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}