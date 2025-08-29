package com.fashionshop.ui.screens.product

import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.LayoutDirection
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.fashionshop.data.model.Product
import com.fashionshop.data.repository.FakeRepository
import com.fashionshop.ui.components.PrimaryButton
import com.fashionshop.ui.components.QuantityStepper
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    id: String,
    onBack: () -> Unit,
    onAddToCart: (Product, String, String, Int) -> Unit
) {
    var product by remember { mutableStateOf<Product?>(null) }
    var size by remember { mutableStateOf("M") }
    var color by remember { mutableStateOf("Đen") }
    var qty by remember { mutableStateOf(1) }

    LaunchedEffect(id) {
        product = FakeRepository.getProduct(id)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        product?.name ?: "Chi tiết sản phẩm",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        // Đặt nút "Thêm vào giỏ" ở bottomBar để luôn nổi bật, dính dưới cùng màn hình
        bottomBar = {
            if (product != null) {
                Surface(
                    shadowElevation = 10.dp,
                    color = MaterialTheme.colorScheme.background
                ) {
                    PrimaryButton(
                        text = "Thêm vào giỏ",
                        onClick = { onAddToCart(product!!, size, color, qty) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                            .height(52.dp)
                    )
                }
            }
        }
    ) { padding ->
        product?.let { p ->
            Column(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.09f),
                                Color.White
                            )
                        )
                    )
                    .padding(
                        start = padding.calculateStartPadding(LayoutDirection.Ltr),
                        end = padding.calculateEndPadding(LayoutDirection.Ltr),
                        top = padding.calculateTopPadding(),
                        bottom = 0.dp // Để không bị trùng với bottomBar
                    )
            ) {
                // Ảnh sản phẩm bo góc lớn, hiệu ứng bóng, overlay giảm giá
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val painter = rememberAsyncImagePainter(p.imageUrl)
                    val isLoaded = painter.state is AsyncImagePainter.State.Success
                    val imageAlpha by animateFloatAsState(
                        targetValue = if (isLoaded) 1f else 0f,
                        animationSpec = tween(durationMillis = 650),
                        label = "image-alpha"
                    )

                    Box(
                        Modifier
                            .size(260.dp)
                            .shadow(24.dp, RoundedCornerShape(32.dp))
                            .clip(RoundedCornerShape(32.dp))
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = p.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(imageAlpha),
                            contentScale = ContentScale.Crop
                        )
                        // Nếu có giảm giá, hiển thị badge %
                        val discountPercent = getDiscountPercent(p.price, p.salePrice)
                        if (discountPercent > 0) {
                            Box(
                                Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(10.dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFFE53935), Color(0xFFFF9800))
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    "-$discountPercent%",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(18.dp))

                // Giá sản phẩm: giá cũ (nếu có) gạch ngang, giá mới nổi bật
                Row(
                    Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val salePrice = p.salePrice ?: p.price
                    if (p.salePrice != null && p.salePrice!! < p.price) {
                        Text(
                            text = "%,d đ".format(p.price),
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                fontSize = 18.sp,
                                textDecoration = TextDecoration.LineThrough,
                                fontWeight = FontWeight.Normal
                            ),
                            modifier = Modifier.padding(end = 12.dp)
                        )
                    }
                    Text(
                        text = "%,d đ".format(salePrice),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                    )
                }

                Spacer(Modifier.height(16.dp))

                Card(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(22.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 7.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        Modifier
                            .padding(horizontal = 24.dp, vertical = 20.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Text(
                            p.name,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (!p.description.isNullOrBlank()) {
                            Text(
                                p.description!!,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Size Select (Chips)
                        Text("Chọn size", fontWeight = FontWeight.Medium)
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            p.sizes.forEach { sz ->
                                FilterChip(
                                    selected = sz == size,
                                    onClick = { size = sz },
                                    label = {
                                        Text(sz, fontWeight = if (sz == size) FontWeight.Bold else FontWeight.Normal)
                                    },
                                    shape = RoundedCornerShape(50),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        labelColor = MaterialTheme.colorScheme.onSurface,
                                    )
                                )
                            }
                        }

                        // Color Select (color circle chips)
                        Text("Chọn màu", fontWeight = FontWeight.Medium)
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            p.colors.forEach { clr ->
                                val colorObj = parseColor(clr)
                                Box(
                                    Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(colorObj)
                                        .border(
                                            width = if (clr == color) 3.dp else 0.dp,
                                            color = if (clr == color) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable { color = clr },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (clr == color) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        QuantityStepper(
                            value = qty,
                            onChange = { qty = it }
                        )
                        // Không đặt nút Thêm vào giỏ ở đây nữa!
                    }
                }
            }
        } ?: Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
    }
}

/**
 * Chuyển tên màu đơn giản sang mã màu. Có thể mở rộng cho nhiều màu!
 */
fun parseColor(name: String): Color = when (name.lowercase()) {
    "đen" -> Color(0xFF222222)
    "trắng" -> Color(0xFFF8F8F8)
    "đỏ" -> Color(0xFFE53935)
    "xanh" -> Color(0xFF1E88E5)
    "vàng" -> Color(0xFFFFEB3B)
    "be" -> Color(0xFFF5F5DC)
    else -> Color.Gray
}

/**
 * Tính phần trăm giảm giá, trả về số nguyên (0 nếu không giảm)
 * Sửa: Nhận vào Long thay vì Int để đồng bộ với Product.price, salePrice
 */
fun getDiscountPercent(oldPrice: Long, salePrice: Long?): Int {
    if (salePrice == null || salePrice >= oldPrice) return 0
    return (100 - (100.0 * salePrice / oldPrice)).roundToInt()
}