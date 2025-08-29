package com.fashionshop.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fashionshop.viewmodel.AdminProductsViewModel

@Composable
fun AdminScreen(onLogout: () -> Unit) {
    val vm: AdminProductsViewModel = viewModel()
    val products by vm.products.collectAsState()

    var name by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Bảng điều khiển Admin", style = MaterialTheme.typography.headlineSmall)
        Divider()

        // Add product form
        Card {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Thêm sản phẩm", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên sản phẩm") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Giá (đ)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Ảnh (URL)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        val price = priceText.toLongOrNull() ?: 0L
                        val img = if (imageUrl.isNotBlank()) imageUrl else "https://picsum.photos/seed/new${'$'}{System.currentTimeMillis()}/600/800"
                        if (name.isNotBlank() && price > 0) {
                            vm.add(name.trim(), price, img)
                            name = ""; priceText = ""; imageUrl = ""
                        }
                    }) { Text("Thêm") }
                    OutlinedButton(onClick = { name = ""; priceText = ""; imageUrl = "" }) { Text("Xóa nhập") }
                }
            }
        }

        Divider()
        Text("Danh sách sản phẩm (${ '$' }{products.size})", style = MaterialTheme.typography.titleMedium)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(products, key = { it.id }) { p ->
                ListItem(
                    headlineContent = { Text(p.name) },
                    supportingContent = { Text("%,d đ".format(p.price)) },
                    trailingContent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextButton(onClick = { vm.delete(p.id) }) { Text("Xóa") }
                        }
                    }
                )
                Divider()
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onLogout) { Text("Đăng xuất") }
            Text("Quản lý sản phẩm", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}
