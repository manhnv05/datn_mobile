package com.fashionshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fashionshop.data.model.Product
import com.fashionshop.data.repository.FakeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

class HomeViewModel: ViewModel() {
    private val repo = FakeRepository
    private val _uiState = MutableStateFlow(HomeUiState(loading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        // Observe product changes so Admin actions reflect immediately on Home
        viewModelScope.launch {
            repo.productsFlow().collectLatest { items ->
                _uiState.value = HomeUiState(products = items, loading = false)
            }
        }
    }
}

 data class HomeUiState(
    val products: List<Product> = emptyList(),
    val loading: Boolean = false
)
