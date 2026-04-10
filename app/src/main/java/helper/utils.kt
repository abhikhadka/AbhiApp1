package helper

import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.IOException

@Composable
fun rememberAssetImage(context: Context, path: String): ImageBitmap? {
    return remember(path) {
        try {
            context.assets.open(path).use { inputStream ->
                BitmapFactory.decodeStream(inputStream).asImageBitmap()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

// Helper to find Activity from Context safely
fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
