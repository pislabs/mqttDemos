package com.pislabs.mqtt.odapp.feature.common.view

import android.app.Activity
import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.util.Consumer
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pislabs.mqtt.odapp.R
import com.pislabs.mqtt.odapp.core.designsystem.theme.ArrowLeftIcon
import com.pislabs.mqtt.odapp.core.designsystem.theme.CommonIcon
import com.pislabs.mqtt.odapp.core.ui.extension.pxToDp
import com.pislabs.mqtt.odapp.core.util.permission.PermissionUtils
import com.pislabs.mqtt.odapp.core.util.toast.ToastUtils
import com.pislabs.mqtt.odapp.feature.common.viewmodel.ScanViewModel

/**
 * 扫码路由
 * 参考：
 * - https://juejin.cn/post/7571285984088997940
 * - https://juejin.cn/post/7492398520583766025
 * - https://github.com/jenly1314/ZXingLite
 *
 * @param viewModel 扫码 ViewModel
 */
@Composable
internal fun ScanRoute(
    viewModel: ScanViewModel = hiltViewModel()
) {
    val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()

    ScanScreen(
        surfaceRequest = surfaceRequest,
        onBackClick = viewModel::navigateBack,
        onBindCamera = viewModel::bindToCamera
    )
}

/**
 * 扫码界面
 *
 * @param onBackClick 返回按钮回调
 * @author Joker.X
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScanScreen(
    pageTitle: String = "",
    surfaceRequest: SurfaceRequest?,
    onBackClick: () -> Unit = {},
    onBindCamera: suspend (lifecycleOwner: LifecycleOwner, onResult: Consumer<String>) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    if (context is Activity) {
        // 获取通知权限
        PermissionUtils.requestCameraPermission(context) { granted ->
            if (!granted) {
                // 权限获取失败，提示用户
                ToastUtils.showError("需要权限才能扫码")
            }
        }
    }

    LaunchedEffect(Unit) {
        onBindCamera(lifecycleOwner) { text ->
            ToastUtils.show("Scan result:$text")
        }
    }

    Box(Modifier.fillMaxSize()) {
        surfaceRequest?.also {
            CameraXViewfinder(
                surfaceRequest = surfaceRequest,
                modifier = Modifier.fillMaxSize()
            )
        }

        ScanFrame(onBackClick = onBackClick)
    }
}

@Composable
private fun ScanFrame(
    onBackClick: () -> Unit = {},
) {
    Box(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                IconButton(onClick = onBackClick) {
                    ArrowLeftIcon(tint = Color.White)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                ScanLine()
            }

            Row(
                horizontalArrangement = Arrangement.Absolute.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-100).dp)
            ) {
                FlashButton()
            }

            Row(
                horizontalArrangement = Arrangement.Absolute.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Text(color = Color.White, text = "扫二维码/条形码")
            }
        }
    }
}

@Composable
private fun ScanLine() {
    // 动画状态
    var startAnimation by rememberSaveable { mutableStateOf(false) }

    // 当前Box大小
    val boxHeight = remember { mutableStateOf(0.dp) }

    val topPadding by animateDpAsState(
        if (startAnimation) boxHeight.value else 0.dp,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Restart)
    )

    LaunchedEffect(boxHeight.value) {
        startAnimation = true
    }

    Box(
        Modifier
            .fillMaxSize()
            .onPlaced() { co ->
                boxHeight.value = co.boundsInParent().height.pxToDp().dp
            }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(start = 0.dp, top = topPadding.value.dp)
        ) {
            HorizontalDivider(
                thickness = 2.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun FlashButton() {
    val cameraManager = LocalContext.current.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val char = cameraManager.getCameraCharacteristics(cameraManager.cameraIdList[0])

    Box(Modifier.wrapContentSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(100.dp)
        ) {
            IconButton(onClick = {
                ToastUtils.show("打开/关闭闪光灯")
            }) {
                CommonIcon(
                    resId = R.drawable.ic_flashlight_on
                )
            }

            Text(color = Color.White, text = "点亮")
        }
    }
}

/**
 * OrderLoadMore 组件预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ScanFramePreview() {
    ScanFrame()
}