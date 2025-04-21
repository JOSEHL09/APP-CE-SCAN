package com.contraentrega.ceapp.ui.qrscanner

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.contraentrega.ceapp.processing.ImageConverter
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage


class BarcodeScannerManager {
    @OptIn(ExperimentalGetImage::class)
    fun processImageProxy(scanner: BarcodeScanner, imageProxy: ImageProxy, onBarcodeDetected: (String) -> Unit) {
        val bitmap = imageProxy.image?.let { ImageConverter.imageProxyToBitmap(it) }

        if (bitmap != null) {
            val image = InputImage.fromBitmap(bitmap, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        barcode.rawValue?.let { code ->
                            try {
                                val orderNumber = code.toInt() // Validar que sea un número
                                onBarcodeDetected(orderNumber.toString())
                            } catch (e: NumberFormatException) {
                                Log.e("BarcodeScanner", "El código escaneado no es un número válido")
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e("BarcodeScanner", "Error al escanear: ${it.message}")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            Log.e("BarcodeScanner", "No se pudo convertir la imagen")
            imageProxy.close()
        }
    }
}



