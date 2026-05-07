package com.example.superheroes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.superheroes.data.HeroesRepository
import com.example.superheroes.ui.HeroesScreen
import com.example.superheroes.ui.theme.SuperheroesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SuperheroesTheme {
                HeroesScreen(
                    heroes = HeroesRepository.heroes
                )
            }
        }
    }
}