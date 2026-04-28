package com.example.superheroes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun HeroesList(
    heroes: List<Hero>,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(heroes) { hero ->
            HeroItem(hero = hero)
        }
    }
}

@Composable
fun HeroItem(
    hero: Hero,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        // 卡片内Row：【文字在左，图片在右】
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // 卡片内边距16dp，和细节图一致
            verticalAlignment = Alignment.CenterVertically // 文字和图片垂直居中对齐
        ) {
            // 左侧文字区域：Column，上下两个Text
            Column(
                modifier = Modifier.weight(1f) // 文字占满剩余空间，图片靠右
            ) {
                Text(
                    text = stringResource(hero.nameRes),
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = stringResource(hero.descriptionRes),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // 文字和图片的间距：16dp
            Spacer(modifier = Modifier.width(16.dp))

            // 右侧图片：72dp正方形，圆角8dp
            Image(

                painter = painterResource(hero.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp)), // 图片圆角8dp
                contentScale = ContentScale.Crop
            )
        }
    }
}