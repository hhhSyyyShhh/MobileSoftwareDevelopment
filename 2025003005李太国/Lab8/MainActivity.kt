package com.yxyn.myhomework8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.yxyn.myhomework8.R
import com.yxyn.myhomework8.model.HeroesRepository
import com.yxyn.myhomework8.ui.theme.Myhomework8Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Myhomework8Theme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = getString(R.string.app_name),
                                    style = MaterialTheme.typography.displayLarge,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxSize()
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                ) {
                    HeroesList(
                        heroes = HeroesRepository.heroes,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}