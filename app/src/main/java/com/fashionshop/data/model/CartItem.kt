package com.fashionshop.data.model

data class CartItem(
    val product: Product,
    val size: String,
    val color: String,
    val quantity: Int
) {
    val subtotal: Long get() = product.price * quantity
}
