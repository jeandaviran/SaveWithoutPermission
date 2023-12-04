package com.bcp.savewithoutpermission.utils

import android.graphics.Bitmap
import android.view.View

fun Bitmap.resize(width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(this, width, height, true)
}

fun viewToBitmap(view: View): Bitmap {
    view.isDrawingCacheEnabled = true
    val bitmap = Bitmap.createBitmap(view.drawingCache)
    view.isDrawingCacheEnabled = false
    return bitmap
}