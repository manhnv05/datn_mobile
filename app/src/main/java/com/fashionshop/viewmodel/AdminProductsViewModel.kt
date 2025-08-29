package com.fashionshop.viewmodel

import androidx.lifecycle.ViewModel
import com.fashionshop.data.repository.FakeRepository
import kotlinx.coroutines.flow.StateFlow
import com.fashionshop.data.model.Product

class AdminProductsViewModel: ViewModel() {
    private val repo = FakeRepository

    val products: StateFlow<List<Product>> = repo.productsFlow()

    fun add(name: String, price: Long, imageUrl: String) {
        repo.addProduct(name, price, imageUrl)
    }

    fun delete(id: String) {
        repo.deleteProduct(id)
    }

    fun update(product: Product) {
        repo.updateProduct(product)
    }
}
