package com.example.superheroes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.superheroes.model.Hero
import com.example.superheroes.model.HeroesRepository

/**
 * 整个页面的“骨架”：
 * 顶部栏 + 列表内容
 *
 * 为什么用 Scaffold？
 * 因为 Scaffold 专门用来搭“页面框架”，它会帮我们处理顶部栏、内容区域的内边距等。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroesScreen() {
    Scaffold(
        topBar = {
            HeroesTopAppBar()
        }
    ) { innerPadding ->
        HeroesList(
            heroes = HeroesRepository.heroes,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * 顶部应用栏。
 *
 * 为什么用 CenterAlignedTopAppBar？
 * 因为实验要求标题水平居中，CenterAlignedTopAppBar 天生就是做这个的。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroesTopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

/**
 * 英雄列表。
 *
 * 为什么用 LazyColumn？
 * 1. 它是“懒加载列表”，只绘制屏幕上看得见的内容，性能更好。
 * 2. 实验要求就是滚动列表。
 * 3. contentPadding 控制列表整体边距，Arrangement.spacedBy 控制卡片之间的间距。
 */
@Composable
fun HeroesList(
    heroes: List<Hero>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(heroes) { hero ->
            HeroItem(
                hero = hero,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * 单个英雄卡片。
 *
 * 布局思路：
 * 1. Card：提供“卡片感”
 * 2. Row：文字和图片横向排列
 * 3. Column：名字和说明纵向排列
 * 4. Image：右边固定大小显示头像
 *
 * 为什么要给图片做 clip？
 * 因为图片原本是矩形，clip(RoundedCornerShape(8.dp)) 可以把它裁成圆角，更接近实验图。
 */
@Composable
fun HeroItem(
    hero: Hero,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 72.dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = stringResource(hero.nameRes),
                    style = MaterialTheme.typography.displaySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(hero.descriptionRes),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Image(
                painter = painterResource(hero.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}