package com.fashionshop.navigation

sealed class Route(val route: String) {
    data object Splash: Route("splash")
    data object Onboarding: Route("onboarding")
    data object Login: Route("login")
    data object Register: Route("register")
    data object Main: Route("main")
    data object Home: Route("home")
    data object Cart: Route("cart")
    data object Profile: Route("profile")
    data object Admin: Route("admin")
    data class ProductDetail(val id: String): Route("product/$id") {
        companion object { const val pattern = "product/{id}" }
    }
    data object Checkout: Route("checkout")
}
