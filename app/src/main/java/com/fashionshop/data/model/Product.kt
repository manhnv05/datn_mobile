package com.fashionshop.data.model

data class Product(
    val id: String,
    val name: String,
    val price: Long,
    val salePrice: Long? = null,
    val imageUrl: String,
    val rating: Float = 4.5f,
    val colors: List<String> = listOf("Đen", "Trắng", "Be"),
    val sizes: List<String> = listOf("S", "M", "L", "XL"),
    val description: String = "Áo sweater cao cấp, chất liệu cotton dày dặn, form chuẩn."
)
