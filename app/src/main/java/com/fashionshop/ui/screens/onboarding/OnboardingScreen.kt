package com.fashionshop.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fashionshop.ui.components.PrimaryButton

@Composable
fun OnboardingScreen(onStart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.11f),
                        Color.White
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(6.dp))
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo hoặc hình minh họa onboarding nổi bật (bạn có thể thay bằng ảnh của bạn)
                Surface(
                    shape = CircleShape,
                    shadowElevation = 14.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 18.dp)
                ) {
                    // Thay thế painterResource(...) bằng hình logo bạn thích hoặc ảnh minh hoạ
                    // Image(painter = painterResource(id = R.drawable.ic_sweater), contentDescription = null)
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "👕",
                            fontSize = 60.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                Text(
                    "FASHION",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    "Shop Sweater cao cấp",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "Khám phá các mẫu sweater thời thượng, chất lượng cao, đa sắc màu cho phong cách của bạn.",
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 6.dp, start = 12.dp, end = 12.dp),
                    lineHeight = 22.sp
                )
            }
            PrimaryButton(
                text = "Bắt đầu mua sắm",
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(2.dp, RoundedCornerShape(16.dp))
            )
        }
    }
}