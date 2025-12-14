package com.pislabs.mqtt.odapp.feature.common.view

import android.app.Activity
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pislabs.mqtt.odapp.R
import com.pislabs.mqtt.odapp.core.designsystem.theme.ArrowLeftIcon
import com.pislabs.mqtt.odapp.core.designsystem.theme.CommonIcon
import com.pislabs.mqtt.odapp.core.ui.extension.debouncedClickable
import com.pislabs.mqtt.odapp.core.ui.extension.pxToDp
import com.pislabs.mqtt.odapp.core.ui.extension.toPx
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
    val torchEnabled by viewModel.torchEnabled.collectAsState()

    ScanScreen(
        surfaceRequest = surfaceRequest,
        torchEnabled = torchEnabled,
        onSwitchTorch = viewModel::switchTorch,
        onNavigateBack = viewModel::navigateBack,
        onBindCamera = viewModel::bindToCamera
    )
}

/**
 * 扫码界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScanScreen(
    pageTitle: String = "扫一扫",
    surfaceRequest: SurfaceRequest? = null,
    torchEnabled: Boolean = false,  // 手电筒状态
    onSwitchTorch: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onBindCamera: suspend (lifecycleOwner: LifecycleOwner) -> Unit
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
        onBindCamera(lifecycleOwner)
    }

    Box(Modifier.fillMaxSize()) {
        surfaceRequest?.also {
            CameraXViewfinder(
                surfaceRequest = surfaceRequest,
                modifier = Modifier.fillMaxSize()
            )
        }

        ScanMask(
            hollowOffset = DpOffset(0.dp, (-20).dp),
            hollowSize = DpSize(250.dp, 250.dp),
            maskTitle = "扫二维码/条形码",
            maskColor = Color.Black.copy(alpha = 0.5F),
        )

        ScanFrame(
            title = pageTitle,
            torchEnabled = torchEnabled,  // 手电筒状态
            onSwitchTorch = onSwitchTorch,
            onBackClick = onNavigateBack
        )
    }
}

@Composable
private fun ScanFrame(
    title: String = "",
    torchEnabled: Boolean = false,  // 手电筒状态
    onSwitchTorch: () -> Unit,
    onBackClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Row(Modifier.weight(1F)) {
                    IconButton(onClick = onBackClick) {
                        ArrowLeftIcon(tint = Color.White)
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(2F)
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        modifier = Modifier
                            .weight(weight = 2F)
                            .wrapContentSize()
                    )
                }

                Row(Modifier.weight(1F)) { }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {
            }

            Row(
                horizontalArrangement = Arrangement.Absolute.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-40).dp)
            ) {
                TorchSwitchButton(
                    torchEnabled = torchEnabled,  // 手电筒状态
                    onSwitchTorch = onSwitchTorch,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Absolute.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) { }
        }
    }
}

@Composable
private fun ScanMask(
    hollowSize: DpSize,
    maskColor: Color,
    maskTitle: String = "",
    hollowOffset: DpOffset = DpOffset.Zero,
    cornerRadius: Dp = 0.dp
) {
    // 获取屏幕宽度（像素）
    // 获取 displayMetrics
    val displayMetrics = LocalContext.current.resources.displayMetrics
    // 获取屏幕宽度（单位：像素）
    val widthPixels = displayMetrics.widthPixels
    // 获取屏幕高度（单位：像素）
    val heightPixels = displayMetrics.heightPixels

    val leftOffsetPx = (widthPixels - hollowSize.width.toPx()) / 2 + hollowOffset.x.toPx()
    val topOffsetPx = (heightPixels - hollowSize.height.toPx()) / 2 + hollowOffset.y.toPx()
    val rightOffsetPx = leftOffsetPx + hollowSize.width.toPx()
    val bottomOffsetPx = topOffsetPx + hollowSize.height.toPx()

    val cornerStokeWidth = 2.dp.toPx()
    val halfCornerStokeWidth = (cornerStokeWidth / 4).dp.toPx()
    val cornerOffset = 8.dp.toPx()
    val cornerSize = 13.dp.toPx()

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            // 创建圆角矩形遮罩
            val revealedPath = Path().apply {
                val roundRect = RoundRect(
                    left = leftOffsetPx,
                    top = topOffsetPx,
                    right = rightOffsetPx,
                    bottom = bottomOffsetPx,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )

                addRoundRect(roundRect)
            }

            // 创建整个画布的矩形遮罩，与圆角矩形做差级操作
            val maskPath = Path().apply {
                addRect(size.toRect())
                op(this, revealedPath, PathOperation.Difference)
            }

            // 左上偏移位置
            val ltPos = Offset(leftOffsetPx - cornerOffset, topOffsetPx - cornerOffset)
            // 左下偏移位置
            val lbPos = Offset(leftOffsetPx - cornerOffset, bottomOffsetPx + cornerOffset)
            // 右上偏移位置
            val rtPos = Offset(rightOffsetPx + cornerOffset, topOffsetPx - cornerOffset)
            // 右下偏移位置
            val rbPos = Offset(rightOffsetPx + cornerOffset, bottomOffsetPx + cornerOffset)

            // 画四个边角
            drawPoints(
                listOf(
                    // 左上
                    ltPos.copy(y = ltPos.y + cornerSize),
                    ltPos.copy(y = ltPos.y - halfCornerStokeWidth),
                    ltPos.copy(x = ltPos.x - halfCornerStokeWidth),
                    ltPos.copy(x = ltPos.x + cornerSize),

                    // 左下
                    lbPos.copy(y = lbPos.y - cornerSize),
                    lbPos.copy(y = lbPos.y + halfCornerStokeWidth),
                    lbPos.copy(x = lbPos.x + halfCornerStokeWidth),
                    lbPos.copy(x = lbPos.x + cornerSize),

                    // 右上
                    rtPos.copy(y = rtPos.y + cornerSize),
                    rtPos.copy(y = rtPos.y - halfCornerStokeWidth),
                    rtPos.copy(x = rtPos.x + halfCornerStokeWidth),
                    rtPos.copy(x = rtPos.x - cornerSize),

                    // 右下
                    rbPos.copy(y = rbPos.y - cornerSize),
                    rbPos.copy(y = rbPos.y + halfCornerStokeWidth),
                    rbPos.copy(x = rbPos.x + halfCornerStokeWidth),
                    rbPos.copy(x = rbPos.x - cornerSize),
                ),
                pointMode = PointMode.Lines,
                Color.Cyan,
                strokeWidth = cornerStokeWidth
            )

            // 绘制遮罩
            drawPath(
                path = maskPath,
                color = maskColor
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .absoluteOffset(0.dp, topOffsetPx.pxToDp() - 40.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(text = maskTitle, color = Color.White)
        }

        Box(
            Modifier
                .absoluteOffset(leftOffsetPx.pxToDp(), topOffsetPx.pxToDp())
                .size(hollowSize)
        ) {
            ScanLine()
        }
    }
}

@Composable
private fun ScanLine() {
    // 动画状态
    var startAnimation by rememberSaveable { mutableStateOf(false) }

    // 当前Box大小
    val scanHeight = remember { mutableStateOf(0.dp) }

    val linePosition by animateDpAsState(
        if (startAnimation) scanHeight.value else 0.dp,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Restart)
    )

    val lineAlpha by animateFloatAsState(
        if (startAnimation) 0f else 1f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Restart)
    )

    LaunchedEffect(scanHeight.value) {
        startAnimation = true
    }

    Box(
        Modifier
            .fillMaxSize()
            .onPlaced() { co ->
                scanHeight.value = co.boundsInParent().height.pxToDp()
            }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(start = 0.dp, top = linePosition.value.dp)
        ) {
            HorizontalDivider(
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(lineAlpha)
            )
        }
    }
}

@Composable
private fun TorchSwitchButton(
    torchEnabled: Boolean = false,  // 手电筒状态
    onSwitchTorch: () -> Unit,
) {
    Box(Modifier.wrapContentSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(100.dp).debouncedClickable{
                onSwitchTorch()
            }
        ) {
            if (torchEnabled) {
                CommonIcon(
                    resId = R.drawable.ic_flashlight_on,
                    tint = Color.Yellow,
                    size = 30.dp
                )
                Text(color = Color.Yellow, text = "点击关闭")
            }
            else {
                CommonIcon(
                    resId = R.drawable.ic_flashlight_off,
                    tint = Color.White,
                    size = 30.dp
                )
                Text(color = Color.White, text = "点击照亮")
            }
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
private fun ScanScreenPreview() {
    val bindCamera = fun(lifecycleOwner: LifecycleOwner) {}

    ScanScreen(
        onBindCamera = bindCamera,
    )
}