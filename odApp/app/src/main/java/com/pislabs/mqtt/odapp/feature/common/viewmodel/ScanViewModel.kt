package com.pislabs.mqtt.odapp.feature.common.viewmodel

import android.content.Context
import android.os.SystemClock
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.TorchState
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pislabs.mqtt.odapp.core.common.base.viewmodel.BaseViewModel
import com.pislabs.mqtt.odapp.core.data.state.AppState
import com.pislabs.mqtt.odapp.core.util.log.LogUtils
import com.pislabs.mqtt.odapp.feature.common.component.qrcode.MlQrcodeAdapter
import com.pislabs.mqtt.odapp.feature.common.component.qrcode.ZxingQrcodeAdapter
import com.pislabs.mqtt.odapp.feature.common.results.ScanResultKey
import com.pislabs.mqtt.odapp.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
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
) : BaseViewModel(navigator, appState) {
    companion object {
        private const val TAG = "ScanViewModel"
    }

    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)

    private var cameraControl: CameraControl? = null

    // 是否正在处理扫描结果
    private var _isOnScanProcessing: Boolean = false

    val surfaceRequest = _surfaceRequest.asStateFlow()

    val torchEnabled = MutableStateFlow(false)

    /**
     * 绑定摄像头
     */
    suspend fun bindToCamera(lifecycleOwner: LifecycleOwner) {
//        val zxingAdapter = ZxingQrcodeAdapter()
        val mlAdapter = MlQrcodeAdapter()
        val provider = ProcessCameraProvider.awaitInstance(context)

        val onScanResult = fun(text: String) {
            LogUtils.d(TAG, "onScanResult ----> text: $text")
            // 正在处理回调时不会重复接受扫码结果
            if (_isOnScanProcessing) return

            _isOnScanProcessing = true

            viewModelScope.launch {
                try {
                    navigator.popBackStackWithResult(ScanResultKey, text)

                    // 返回成功将延时2000ms返回，防止返回失败
                    delay(2000)
                } catch (ex: Exception) {
                    LogUtils.e(TAG, ex.message)
                }

                _isOnScanProcessing = false
            }
        }

        val group = UseCaseGroup.Builder()
            .addUseCase(Preview.Builder().build().apply {
                setSurfaceProvider { _surfaceRequest.value = it }
            })
            .addUseCase(
                ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888).build()
                    .apply {
                        setAnalyzer(CameraXExecutors.ioExecutor()) { proxy ->
                            proxy.use {
                                val bitmap = it.toBitmap()
//                                zxingAdapter.onScan(bitmap, onScanResult)
                                mlAdapter.onScan(bitmap, onScanResult)
                            }
                        }
                    }).build()

        provider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, group).also {
            cameraControl = it.cameraControl

            // 监听闪光灯状态
            it.cameraInfo.torchState.observe(lifecycleOwner) { state ->
                torchEnabled.value = state == TorchState.ON
            }

            // 默认关闭闪光灯
            enableTorch(false)

            try {
                awaitCancellation()
            } finally {
                provider.unbindAll()
                cameraControl = null
            }
        }
    }

    /**
     * 切换手电筒状态
     */
    fun switchTorch() {
        enableTorch(!torchEnabled.value)
    }

    /**
     * 开启关闭闪光灯
     */
    fun enableTorch(enabled: Boolean) {
        if (cameraControl == null) return

        cameraControl!!.enableTorch(enabled)
        torchEnabled.value = enabled
    }
}