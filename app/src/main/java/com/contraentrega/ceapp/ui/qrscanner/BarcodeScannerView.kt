package com.contraentrega.ceapp.ui.qrscanner

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@Composable
fun BarcodeScannerView(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner,
    isScannerActive: Boolean,
    onBarcodeDetected: (String) -> Unit
) {
    val context = LocalContext.current
    val previewView = PreviewView(context)

    if (isScannerActive) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder().build()
            preview.setSurfaceProvider(previewView.surfaceProvider)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalysis = ImageAnalysis.Builder().build()
            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(context),
                { imageProxy -> analyzeImage(imageProxy, onBarcodeDetected) }
            )

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageAnalysis
                )
            } catch (e: Exception) {
                Log.e("BarcodeScannerView", "Camera initialization failed", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    )
}

@OptIn(androidx.camera.core.ExperimentalGetImage::class)
private fun analyzeImage(imageProxy: ImageProxy, onBarcodeDetected: (String) -> Unit) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient()
        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    onBarcodeDetected(barcode.displayValue ?: "")
                }
            }
            .addOnFailureListener { e ->
                Log.e("BarcodeScannerView", "Barcode detection failed", e)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}











