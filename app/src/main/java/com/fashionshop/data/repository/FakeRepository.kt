package com.fashionshop.data.repository

import com.fashionshop.data.model.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Make repository a singleton so Admin and Home share the same data source
object FakeRepository {

    private val _products = MutableStateFlow(
        List(16) { i ->
            Product(
                id = (i + 1).toString(),
                name = listOf("Basic", "Luxury", "Oversize", "Premium")[i % 4] + " Sweater " + (i + 1),
                price = listOf(390_000L, 490_000L, 590_000L, 790_000L)[i % 4],
                salePrice = if (i % 4 == 0) null else listOf(340_000L, 420_000L, 550_000L, 0L)[i % 4].takeIf { it > 0 },
                imageUrl = "https://picsum.photos/seed/sweater" + (i + 5) + "/600/800",
            )
        }
    )

    fun productsFlow(): StateFlow<List<Product>> = _products

    suspend fun getProducts(): List<Product> {
        delay(400) // fake network
        return _products.value
    }

    suspend fun getProduct(id: String): Product? {
        delay(250)
        return _products.value.find { it.id == id }
    }

    fun addProduct(name: String, price: Long, imageUrl: String, salePrice: Long? = null) {
        val current = _products.value
        val nextId = (current.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0) + 1
        val newP = Product(id = nextId.toString(), name = name, price = price, salePrice = salePrice, imageUrl = imageUrl)
        _products.value = current + newP
    }

    fun updateProduct(updated: Product) {
        val current = _products.value
        _products.value = current.map { if (it.id == updated.id) updated else it }
    }

    fun deleteProduct(id: String) {
        _products.value = _products.value.filterNot { it.id == id }
    }
}