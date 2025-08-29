package com.fashionshop.viewmodel

import androidx.lifecycle.ViewModel
import com.fashionshop.data.model.CartItem
import com.fashionshop.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel: ViewModel() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items

    fun add(product: Product, size: String, color: String, quantity: Int) {
        val current = _items.value.toMutableList()
        val idx = current.indexOfFirst { it.product.id == product.id && it.size == size && it.color == color }
        if (idx >= 0) {
            val old = current[idx]
            current[idx] = old.copy(quantity = old.quantity + quantity)
        } else {
            current.add(CartItem(product, size, color, quantity))
        }
        _items.value = current
    }

    fun remove(index: Int) {
        val current = _items.value.toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            _items.value = current
        }
    }

    fun updateQuantity(index: Int, quantity: Int) {
        val current = _items.value.toMutableList()
        if (index in current.indices) {
            val item = current[index]
            current[index] = item.copy(quantity = quantity.coerceAtLeast(1))
            _items.value = current
        }
    }

    fun total(): Long = _items.value.sumOf { it.subtotal }

    fun clear() { _items.value = emptyList() }
}
