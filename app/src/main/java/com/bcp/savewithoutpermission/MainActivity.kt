package com.bcp.savewithoutpermission

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bcp.savewithoutpermission.utils.getTimeMillis
import com.bcp.savewithoutpermission.utils.resize
import com.bcp.savewithoutpermission.utils.showToast
import com.bcp.savewithoutpermission.utils.viewToBitmap
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnDownloadImage).setOnClickListener {
            showToast(this, "Please save the image")
            saveImageWithIntent()
        }
    }

    private fun saveImageWithIntent() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).addImageFormat()
        startActivityForResult(intent, REQUEST_CODE_CREATE_CAPTURE)
    }

    private fun Intent.addImageFormat(): Intent {
        return this.apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
            putExtra(Intent.EXTRA_TITLE, "BCP_${getTimeMillis()}.jpg")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                REQUEST_CODE_CREATE_CAPTURE -> {
                    val outputUri = data?.data

                    outputUri?.let {
                        try {
                            val viewToCapture = findViewById<ConstraintLayout>(R.id.viewCapture)
                            val imageBitmap = viewToBitmap(viewToCapture)

                            //Se puede redimensionar la imagen
                            val imageResized = imageBitmap.resize(width = 700, height = 1400)

                            val outputStream = contentResolver.openOutputStream(outputUri)
                            outputStream?.let {
                                imageResized.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                                outputStream.flush()
                                outputStream.close()
                            }
                            showToast(this, "Imagen guardada!")
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }



    companion object {
        private const val REQUEST_CODE_CREATE_CAPTURE = 180
    }
}