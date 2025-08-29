package com.fashionshop.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fashionshop.R

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onGoRegister: () -> Unit,
    onLoginWithGoogle: () -> Unit,
    onGuestContinue: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    var showPwd by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // Main Gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f),
                        Color.White
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Glowing Avatar/Logo
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .shadow(14.dp, CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.13f),
                                Color.White
                            ),
                            radius = 62f
                        ), CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                    modifier = Modifier.size(70.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Logo",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            Spacer(Modifier.height(22.dp))
            Text(
                "Chào mừng trở lại!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    letterSpacing = 0.8.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Đăng nhập để tiếp tục mua sắm trải nghiệm tuyệt vời.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                ),
                modifier = Modifier.padding(bottom = 18.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(22.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f)
                ),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        placeholder = { Text("demo@shop.com") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.MailOutline,
                                contentDescription = "Email"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        textStyle = TextStyle(fontSize = 17.sp)
                    )
                    OutlinedTextField(
                        value = pwd,
                        onValueChange = { pwd = it },
                        label = { Text("Mật khẩu") },
                        placeholder = { Text("••••••") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password"
                            )
                        },
                        trailingIcon = {
                            val eyeIcon = if (showPwd) R.drawable.ic_eye_off else R.drawable.ic_eye
                            Icon(
                                painter = painterResource(id = eyeIcon),
                                contentDescription = if (showPwd) "Ẩn mật khẩu" else "Hiện mật khẩu",
                                modifier = Modifier
                                    .clickable { showPwd = !showPwd }
                                    .size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        visualTransformation = if (showPwd) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        textStyle = TextStyle(fontSize = 17.sp)
                    )
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            onLogin(email, pwd)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        Text("Đăng nhập", fontWeight = FontWeight.Bold, fontSize = 19.sp)
                    }

                    // Register link
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Chưa có tài khoản?",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light)
                        )
                        TextButton(onClick = onGoRegister) {
                            Text(
                                "Đăng ký",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(18.dp))
            Text(
                "Demo: demo@shop.com / 123456",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
            )

            Spacer(Modifier.height(30.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(Modifier.weight(1f), thickness = 1.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                Text(
                    "  hoặc  ",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Divider(Modifier.weight(1f), thickness = 1.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            }
            Spacer(Modifier.height(20.dp))

            // Google Login Button
            OutlinedButton(
                onClick = {
                    focusManager.clearFocus()
                    onLoginWithGoogle()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.Transparent
                ),
                border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text("Tiếp tục với Google", fontWeight = FontWeight.Medium, fontSize = 15.sp)
            }

            Spacer(Modifier.height(13.dp))

            // Guest Continue Button
            OutlinedButton(
                onClick = {
                    focusManager.clearFocus()
                    onGuestContinue()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.secondary,
                    containerColor = Color.Transparent
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.45f)),
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Guest",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text("Tiếp tục với Khách", fontWeight = FontWeight.Medium, fontSize = 15.sp)
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}