package com.droiddude.apps.galleryapp.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.droiddude.apps.galleryapp.R
import com.droiddude.apps.galleryapp.data.photos

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun GalleryApp() {
    var selectedPhotoId by remember {
        mutableStateOf(R.drawable.pexels_photo_1)
    }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        HorizontalPager()
        PhotosGrid(
            photos = photos,
            onClick = {
            selectedPhotoId = it
        })
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPager(
    modifier : Modifier = Modifier) {

    val colorMatrix = remember { ColorMatrix() }
    val pagerState = remember { PagerState() }
    HorizontalPager(
        pageCount = photos.size,
        state = PagerState()) { index ->
        val pageOffset = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
        val imageSize = animateFloatAsState(
            targetValue = if(pageOffset != 0.0f) 0.75f else 1f,
            animationSpec = tween(
                durationMillis = 1000
            ), label = "").value
        LaunchedEffect(key1 = imageSize) {
            if(pageOffset != 0.0f) colorMatrix.setToScale(1f,1f,1f,0.9f)
            else colorMatrix.setToScale(1f,1f,1f,1f)
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(photos.get(index)).build(),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .graphicsLayer {
                    scaleX = imageSize
                    scaleY = imageSize
                },
            colorFilter = ColorFilter.colorMatrix(colorMatrix)
        )
    }
}

@Composable
fun PhotosGrid(photos : List<Int>,
               modifier : Modifier = Modifier,
               onClick :(Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(5.dp)) {

        items(photos.size) { index ->
            Card(modifier = Modifier
                .height(150.dp)
                .width(150.dp)
                .padding(5.dp)) {
                Image(
                    painter = painterResource(id = photos.get(index)),
                    contentDescription = "Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clickable {
                        onClick(index)
                    })
            }
        }
    }

}
