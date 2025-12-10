package com.pislabs.mqtt.odapp.feature.common.viewmodel

import android.content.Context
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pislabs.mqtt.odapp.core.common.base.viewmodel.BaseViewModel
import com.pislabs.mqtt.odapp.core.data.state.AppState
import com.pislabs.mqtt.odapp.feature.common.component.qrcode.MlQrcodeAdapter
import com.pislabs.mqtt.odapp.feature.common.component.qrcode.ZxingQrcodeAdapter
import com.pislabs.mqtt.odapp.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 扫码 ViewModel
 */
@HiltViewModel
class ScanViewModel @Inject constructor(
    navigator: AppNavigator,
    appState: AppState,
    savedStateHandle: SavedStateHandle,
    @param:ApplicationContext private val context: Context,
): BaseViewModel(navigator, appState) {
    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest = _surfaceRequest.asStateFlow()

    private var cameraControl: CameraControl? = null

    /**
     * 绑定摄像头
     */
    suspend fun bindToCamera(lifecycleOwner: LifecycleOwner, onResult: Consumer<String>) {
        val zxingAdapter = ZxingQrcodeAdapter()
        val mlAdapter = MlQrcodeAdapter()
        val provider = ProcessCameraProvider.awaitInstance(context)

        val onScanResult = { text: String ->
            onResult.accept(text)
        }

        val group = UseCaseGroup.Builder()
            .addUseCase(Preview.Builder().build().apply {
                setSurfaceProvider { _surfaceRequest.value = it }
            })
            .addUseCase(
                ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888).build().apply {
                        setAnalyzer(CameraXExecutors.ioExecutor()) { proxy ->
                            proxy.use {
                                val bitmap = it.toBitmap()
                                zxingAdapter.onScan(bitmap, onScanResult)
                                mlAdapter.onScan(bitmap, onScanResult)
                            }
                        }
                    }).build()

        provider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, group).also {
            cameraControl = it.cameraControl
            try {
                awaitCancellation()
            } finally {
                provider.unbindAll()
                cameraControl = null
            }
        }
    }
}