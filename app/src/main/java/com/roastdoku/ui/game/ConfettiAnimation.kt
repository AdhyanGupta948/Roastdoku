package com.roastdoku.ui.game
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp // <-- Import this
import kotlin.random.Random


@Composable
fun ConfettiAnimation() {
    val particles = remember { mutableStateOf<List<Particle>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        particles.value = List(100) {
            Particle(
                x = Random.nextFloat(),
                y = -0.1f,
                vx = (Random.nextFloat() - 0.5f) * 0.02f,
                vy = Random.nextFloat() * 0.03f + 0.01f,
                color = Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat()
                )
            )
        }
        
        repeat(300) {
            particles.value = particles.value.map { particle ->
                particle.copy(
                    x = particle.x + particle.vx,
                    y = particle.y + particle.vy,
                    vy = particle.vy + 0.0005f // gravity
                )
            }
            kotlinx.coroutines.delay(16)
        }
    }
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.value.forEach { particle ->
            drawCircle(
                color = particle.color,
                radius = 8.dp.toPx(),
                center = Offset(
                    particle.x * size.width,
                    particle.y * size.height
                )
            )
        }
    }
}

data class Particle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val color: Color
)

