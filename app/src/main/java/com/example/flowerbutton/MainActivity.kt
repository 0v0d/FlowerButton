package com.example.flowerbutton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flowerbutton.ui.theme.FlowerButtonTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlowerButtonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SampleScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Composable
fun SampleScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(Modifier.height(32.dp))
        // ハラハラ用グリーングラデ
        PetalCounterButton(
            label = "ワクワク",
            gradient = listOf(Color(0xFFFDE9BC), Color(0xFFF2B948))
        )
    }
}

@Composable
fun GradientImageButton(
    @DrawableRes imageRes: Int,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Image(
        painter = painterResource(imageRes),
        contentDescription = null,
        modifier = modifier
            .size(120.dp)
            .clickable { onClick() }
            .drawWithContent {
                // まず元画像を描画
                drawContent()
                // その上にグラデーションを乗せ、BlendMode.SrcIn で
                // “画像のアルファ部分” のみグラデーションで塗りつぶし
                drawRect(
                    brush = Brush.radialGradient(gradientColors),
                    blendMode = BlendMode.SrcIn
                )
            }
    )
}



@Composable
fun PetalCounterButton(
    label: String,
    gradient: List<Color>,
    modifier: Modifier = Modifier
) {
    var count by remember { mutableStateOf(0) }   // 0〜5

    // ボタン自体
    Box(
        modifier = modifier
            .size(120.dp)
             .clip(CircleShape)
            .clickable
            { count = (count + 1) % 6 }   // 6→0
    ) {
        /** ── 背景を Canvas で描く ── */
        Canvas(modifier = Modifier.matchParentSize()) {
            val radiusPetal  = size.minDimension * 0.2f   // 花びら半径
            val radiusCenter = size.minDimension * 0.35f   // 中央円半径
            val center = size.center

            // 花びら（0〜5 枚）
            repeat(count) { i ->
                val angle   = (i / 5f) * 2f * PI.toFloat() - PI.toFloat() / 2f
                val offsetX = cos(angle) * radiusPetal * 1.25f
                val offsetY = sin(angle) * radiusPetal * 1.25f
                drawCircle(
                    brush  = Brush.linearGradient(gradient),
                    radius = radiusPetal,
                    center = Offset(center.x + offsetX, center.y + offsetY)
                )
            }

            // 中央の円
            drawCircle(
                brush  = Brush.linearGradient(gradient),
                radius = radiusCenter,
                center = center
            )
        }

        /** ── テキスト ── */
        Text(
            text  = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
