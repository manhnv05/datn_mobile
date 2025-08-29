package com.fashionshop

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fashionshop.navigation.Route
import com.fashionshop.ui.screens.auth.LoginScreen
import com.fashionshop.ui.screens.auth.RegisterScreen
import com.fashionshop.ui.screens.cart.CartScreen
import com.fashionshop.ui.screens.checkout.CheckoutScreen
import com.fashionshop.ui.screens.home.HomeScreen
import com.fashionshop.ui.screens.onboarding.OnboardingScreen
import com.fashionshop.ui.screens.product.ProductDetailScreen
import com.fashionshop.ui.screens.profile.ProfileScreen
import com.fashionshop.ui.screens.splash.SplashScreen
import com.fashionshop.ui.screens.admin.AdminScreen
import com.fashionshop.viewmodel.AuthViewModel
import com.fashionshop.viewmodel.CartViewModel

@Composable
fun FashionShopApp() {
    val navController = rememberNavController()
    // Use lifecycle-aware ViewModels scoped to the Activity to avoid losing state
    val authVM: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val cartVM: CartViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val loggedIn by authVM.loggedIn.collectAsState()

    NavHost(navController = navController, startDestination = Route.Splash.route) {
        composable(Route.Splash.route) {
            // Decide the next screen based on current auth state to avoid extra hops
            val isLoggedIn by authVM.loggedIn.collectAsState()
            val isAdmin by authVM.isAdmin.collectAsState()
            SplashScreen(onFinish = {
                val next = if (isLoggedIn) {
                    if (isAdmin) Route.Admin.route else Route.Main.route
                } else Route.Onboarding.route
                navController.navigate(next) {
                    popUpTo(Route.Splash.route) { inclusive = true }
                    launchSingleTop = true
                }
            })
        }
        composable(Route.Onboarding.route) {
            OnboardingScreen(
                onStart = {
                    navController.navigate(if (loggedIn) Route.Main.route else Route.Login.route) {
                        // Do not remove Onboarding from back stack so users can go back instead of exiting the app
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Route.Login.route) {
            // Reactively navigate when login state becomes true
            val isLoggedIn by authVM.loggedIn.collectAsState()
            val isAdmin by authVM.isAdmin.collectAsState()
            LaunchedEffect(isLoggedIn, isAdmin) {
                if (isLoggedIn) {
                    val dest = if (isAdmin) Route.Admin.route else Route.Main.route
                    navController.navigate(dest) {
                        popUpTo(Route.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            LoginScreen(
                onLogin = { email, pwd ->
                    authVM.login(email, pwd)
                },
                onGoRegister = { navController.navigate(Route.Register.route) },
                onLoginWithGoogle = {
                    authVM.loginWithGoogle()
                },
                onGuestContinue = {
                    authVM.loginAsGuest()
                }
            )
        }
        composable(Route.Register.route) {
            // Reactively navigate when login state becomes true after registration
            val isLoggedIn by authVM.loggedIn.collectAsState()
            val isAdmin by authVM.isAdmin.collectAsState()
            LaunchedEffect(isLoggedIn, isAdmin) {
                if (isLoggedIn) {
                    val dest = if (isAdmin) Route.Admin.route else Route.Main.route
                    navController.navigate(dest) {
                        popUpTo(Route.Register.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            RegisterScreen(
                onRegister = { name, email, pwd ->
                    authVM.register(name, email, pwd)
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Route.Main.route) {
            MainScaffold(navController = navController, cartVM = cartVM, authVM = authVM)
        }
        composable(Route.Admin.route) {
            AdminScreen(onLogout = {
                authVM.logout()
                navController.navigate(Route.Login.route) {
                    popUpTo(Route.Admin.route) { inclusive = true }
                }
            })
        }
        composable(Route.ProductDetail.pattern) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: "0"
            ProductDetailScreen(id = id, onBack = { navController.popBackStack() }, onAddToCart = { p, s, c, q ->
                cartVM.add(p, s, c, q)
                // Navigate to Main; Cart is inside the inner NavHost
                navController.navigate(Route.Main.route) {
                    popUpTo(Route.Main.route) { inclusive = false }
                    launchSingleTop = true
                }
            })
        }
        composable(Route.Checkout.route) {
            CheckoutScreen(total = cartVM.total(), onDone = {
                cartVM.clear()
                // Navigate back to Main; Home resides in the inner NavHost
                navController.navigate(Route.Main.route) {
                    popUpTo(Route.Main.route) { inclusive = false }
                    launchSingleTop = true
                }
            })
        }
    }
}

@Composable
fun MainScaffold(navController: NavHostController, cartVM: CartViewModel, authVM: AuthViewModel) {
    // Use a dedicated inner NavController for bottom tabs to avoid graph conflicts
    val innerNavController = rememberNavController()
    var selected by remember { mutableStateOf(0) }
    val items = listOf(Route.Home, Route.Cart, Route.Profile)

    // Handle back press: if not on Home tab, go to Home instead of exiting the app
    val isOnHome by remember {
        derivedStateOf {
            selected == 0 && innerNavController.currentDestination?.route == Route.Home.route
        }
    }
    androidx.activity.compose.BackHandler(enabled = !isOnHome) {
        // Navigate to Home tab and consume back press
        selected = 0
        if (innerNavController.currentDestination != null) {
            innerNavController.navigate(Route.Home.route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(Route.Home.route) { saveState = true }
            }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, route ->
                    NavigationBarItem(
                        selected = selected == index,
                        onClick = {
                            selected = index
                            // Guard against navigating before inner graph is ready
                            if (innerNavController.currentDestination != null) {
                                innerNavController.navigate(route.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    // Use a stable route for popUpTo to avoid issues before graph is ready
                                    popUpTo(Route.Home.route) { saveState = true }
                                }
                            }
                        },
                        icon = {
                            when (route) {
                                Route.Home -> Icon(Icons.Default.Home, contentDescription = null)
                                Route.Cart -> Icon(Icons.Default.ShoppingCart, contentDescription = null)
                                Route.Profile -> Icon(Icons.Default.Person, contentDescription = null)
                                else -> {}
                            }
                        },
                        label = { Text(route.route.replaceFirstChar { it.uppercase() }) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = innerNavController,
            startDestination = Route.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Route.Home.route) { HomeScreen(onOpenProduct = { id -> navController.navigate("product/$id") }) }
            composable(Route.Cart.route) { CartScreen(
                itemsFlow = cartVM.items,
                onChangeQty = { idx, qty -> cartVM.updateQuantity(idx, qty) },
                onRemove = { idx -> cartVM.remove(idx) },
                onCheckout = { navController.navigate(Route.Checkout.route) }
            ) }
            composable(Route.Profile.route) { ProfileScreen(onLogout = {
                authVM.logout()
                navController.navigate(Route.Login.route) {
                    popUpTo(Route.Main.route) { inclusive = true }
                }
            }) }
        }
    }
}
