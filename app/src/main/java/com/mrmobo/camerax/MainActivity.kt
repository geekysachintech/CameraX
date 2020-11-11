package com.mrmobo.camerax

import android.content.pm.PackageManager
import android.opengl.Matrix
import android.os.Bundle
import android.view.Surface
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            )
            == PackageManager.PERMISSION_GRANTED){

            textureView.post {
                startCamera()
            }




        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                1234
            )
        }
    }

    private fun startCamera() {
       // val previewConfig :PreviewConfig.Builder = PreviewConfig.

       val previewConfig: PreviewConfig = PreviewConfig.Builder().apply {

           setTargetAspectRatio(AspectRatio.RATIO_16_9)
           setLensFacing(CameraX.LensFacing.BACK)
       }.build()

        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener{
            val parent: ViewGroup = textureView.parent as ViewGroup
            parent.removeView(textureView)
            parent.addView(textureView, 0)
            updateTransform()

//            val surfaceTexture = textureView.surfaceTexture
//            val surface = Surface(surfaceTexture)
            textureView.setSurfaceTexture(it.surfaceTexture)
        }
        CameraX.bindToLifecycle(this, preview)

    }

    private fun updateTransform() {
        val matrix = android.graphics.Matrix()
        val centerX: Float = textureView.width/2f
        val centerY: Float = textureView.height/2f

        val rotationDegrees: Int = when(textureView.display.rotation){
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)
        textureView.setTransform(matrix)
    }
}