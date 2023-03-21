package com.animesh.justapp.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.animesh.justapp.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CoilImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    error: Int? = null,
    placeholder: Int? = null,
    memoryCachePolicy: CachePolicy = CachePolicy.ENABLED,
    diskCachePolicy: CachePolicy = CachePolicy.ENABLED,
    onImageClick: () -> Unit
) {
    val image: Painter = rememberImagePainter(
        data = imageUrl,
        builder = {
            crossfade(true)
            error?.let {
                error(R.drawable.app_icon)
            }
            placeholder?.let {
                placeholder(R.drawable.app_icon)
            }
            memoryCachePolicy(memoryCachePolicy)
            diskCachePolicy(diskCachePolicy)
        }
    )
    Image(
        painter = image,
        contentDescription = contentDescription,
        modifier = modifier
            .padding(4.dp, 4.dp)
            .clip(CircleShape)
            .clickable { onImageClick() }
            .size(125.dp),
        contentScale = contentScale
    )
}