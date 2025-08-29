package com.fashionshop.ui.screens.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fashionshop.data.model.CartItem
import com.fashionshop.ui.components.QuantityStepper
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    itemsFlow: StateFlow<List<CartItem>>? = null,
    onChangeQty: (Int, Int) -> Unit = { _, _ -> },
    onRemove: (Int) -> Unit = {},
    onCheckout: (List<CartItem>) -> Unit = {} // Trả về danh sách item được chọn
) {
    val items by (itemsFlow?.collectAsState() ?: remember { mutableStateOf(emptyList<CartItem>()) })
    // Trạng thái chọn sản phẩm
    var checkedItems by remember { mutableStateOf(items.map { true }.toMutableList()) }

    // Đồng bộ checkedItems khi items thay đổi
    LaunchedEffect(items) {
        checkedItems = items.map { true }.toMutableList()
    }
    val selectedItems = items.filterIndexed { index, _ -> checkedItems.getOrNull(index) == true }
    val total = selectedItems.sumOf { it.subtotal }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Giỏ hàng",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        bottomBar = {
            if (items.isNotEmpty()) {
                Surface(shadowElevation = 6.dp, color = MaterialTheme.colorScheme.background) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checkedItems.all { it },
                                onCheckedChange = { checked ->
                                    checkedItems = checkedItems.map { checked }.toMutableList()
                                }
                            )
                            Text("Tất cả", modifier = Modifier.padding(end = 12.dp))
                            Column {
                                Text("Tổng cộng", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                Text(
                                    "%,d đ".format(total),
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Button(
                            onClick = { onCheckout(selectedItems) },
                            enabled = total > 0 && selectedItems.isNotEmpty(),
                            modifier = Modifier.height(52.dp)
                        ) {
                            Text(
                                "Thanh toán",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (items.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Giỏ hàng trống",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                                Color.White
                            )
                        )
                    )
                    .padding(padding)
            ) {
                itemsIndexed(items) { index, item ->
                    CartRow(
                        item = item,
                        checked = checkedItems.getOrNull(index) == true,
                        onCheckedChange = { checked ->
                            checkedItems = checkedItems.toMutableList().also { it[index] = checked }
                        },
                        onChange = { q -> onChangeQty(index, q) },
                        onRemove = {
                            onRemove(index)
                            checkedItems = checkedItems.toMutableList().also { it.removeAt(index) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CartRow(
    item: CartItem,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(end = 8.dp)
            )
            // Product Image
            Image(
                painter = rememberAsyncImagePainter(item.product.imageUrl),
                contentDescription = item.product.name,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            // Product Info and Actions
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    item.product.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "%,d đ".format(item.product.price),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "x${item.quantity}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
                Text("Phân loại: ${item.color} / ${item.size}", style = MaterialTheme.typography.bodySmall)
                QuantityStepper(
                    value = item.quantity,
                    onChange = onChange
                )
            }
            // Remove Icon
            IconButton(onClick = onRemove, modifier = Modifier.padding(start = 8.dp)) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Xóa sản phẩm",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}