package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.PakBlackBg
import com.example.ui.theme.PakDarkBorder
import com.example.ui.theme.PakDarkSurface
import com.example.ui.theme.PakDarkSurfaceVariant
import com.example.ui.theme.PakEmeraldDark
import com.example.ui.theme.PakEmeraldGreen
import com.example.ui.theme.PakEmeraldLight
import com.example.ui.theme.PakGold
import com.example.ui.theme.PakGoldDark
import com.example.ui.theme.PakSuccess
import com.example.ui.theme.PakTextMuted
import com.example.ui.theme.PakTextPrimary
import com.example.ui.theme.PakTextSecondary
import com.example.ui.theme.PakWhite
import kotlinx.coroutines.delay

@Composable
fun AuthScreen(
    onLoginSuccess: (phone: String, pin: String) -> Unit,
    onRegisterSuccess: (fullName: String, phone: String, email: String, pin: String, referral: String?) -> Unit,
    onQuickDemoLogin: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isRegisterMode by remember { mutableStateOf(false) }

    // Form fields
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("03001234567") }
    var email by remember { mutableStateOf("") }
    var securityPin by remember { mutableStateOf("7860") }
    var referralCode by remember { mutableStateOf("") }

    // OTP Verification Modal state
    var showOtpDialog by remember { mutableStateOf(false) }
    var enteredOtp by remember { mutableStateOf("") }
    var otpTimer by remember { mutableIntStateOf(30) }
    val generatedOtp = remember { "786920" }

    LaunchedEffect(showOtpDialog) {
        if (showOtpDialog) {
            otpTimer = 30
            while (otpTimer > 0) {
                delay(1000)
                otpTimer--
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PakBlackBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Logo Branding
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(PakEmeraldGreen, PakEmeraldDark))
                    )
                    .border(2.dp, PakGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PK",
                    color = PakGold,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "PAK INVESTMENT",
                color = PakWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Text(
                text = "Secure Investment & Video Task Earning Portal",
                color = PakGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mode Selector
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurface)
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (!isRegisterMode) PakEmeraldGreen else Color.Transparent)
                            .clickable { isRegisterMode = false }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Login",
                            color = if (!isRegisterMode) PakBlackBg else PakTextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isRegisterMode) PakEmeraldGreen else Color.Transparent)
                            .clickable { isRegisterMode = true }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Register",
                            color = if (isRegisterMode) PakBlackBg else PakTextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Form inputs
            if (isRegisterMode) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = PakEmeraldLight) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedTextFieldColors()
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = PakEmeraldLight) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = outlinedTextFieldColors()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Mobile Number (03XX-XXXXXXX)") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = PakEmeraldLight) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_phone_input"),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = outlinedTextFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = securityPin,
                onValueChange = { securityPin = it },
                label = { Text("2-Step Verification Security PIN (4 Digits)") },
                leadingIcon = { Icon(Icons.Default.Security, contentDescription = null, tint = PakGold) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_pin_input"),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = PasswordVisualTransformation(),
                colors = outlinedTextFieldColors()
            )

            if (isRegisterMode) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = referralCode,
                    onValueChange = { referralCode = it },
                    label = { Text("Referral Code (Optional)") },
                    placeholder = { Text("e.g. PK-786012") },
                    leadingIcon = { Icon(Icons.Default.Star, contentDescription = null, tint = PakGold) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedTextFieldColors()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Button
            Button(
                onClick = {
                    if (isRegisterMode) {
                        showOtpDialog = true
                    } else {
                        onLoginSuccess(phoneNumber, securityPin)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("auth_action_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PakEmeraldGreen,
                    contentColor = PakBlackBg
                )
            ) {
                Text(
                    text = if (isRegisterMode) "Verify Mobile & Register" else "Secure Login",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Demo Switcher
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PakDarkBorder, PakDarkBorder)))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "QUICK DEMO ACCOUNTS",
                        color = PakGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onQuickDemoLogin("PK-786012") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Ali (Plan A Active)", fontSize = 11.sp, color = PakEmeraldLight)
                        }
                        OutlinedButton(
                            onClick = { onQuickDemoLogin("PK-991204") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Hamza (Plan Locked)", fontSize = 11.sp, color = PakTextSecondary)
                        }
                    }
                }
            }
        }

        // Automatic OTP Verification Dialog
        if (showOtpDialog) {
            Dialog(onDismissRequest = { showOtpDialog = false }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)),
                    color = PakDarkSurface
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(PakEmeraldGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = PakEmeraldGreen, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Automatic OTP Verification",
                            color = PakWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "We sent a 6-digit verification code to $phoneNumber",
                            color = PakTextSecondary,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Simulated auto-fill banner
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { enteredOtp = generatedOtp },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = PakDarkSurfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "Auto-detected OTP SMS:", color = PakTextSecondary, fontSize = 10.sp)
                                    Text(text = generatedOtp, color = PakGold, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, letterSpacing = 2.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(PakEmeraldGreen)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("Auto Fill", color = PakBlackBg, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = enteredOtp,
                            onValueChange = { enteredOtp = it },
                            label = { Text("Enter 6-Digit Code") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = outlinedTextFieldColors()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (otpTimer > 0) "Resend code in ${otpTimer}s" else "Resend Code",
                            color = if (otpTimer > 0) PakTextMuted else PakGold,
                            fontSize = 12.sp,
                            modifier = Modifier.clickable(enabled = otpTimer == 0) { otpTimer = 30 }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                showOtpDialog = false
                                onRegisterSuccess(
                                    fullName.ifBlank { "Investor Member" },
                                    phoneNumber,
                                    email.ifBlank { "member@pakinvestment.pk" },
                                    securityPin,
                                    referralCode.ifBlank { null }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PakEmeraldGreen, contentColor = PakBlackBg)
                        ) {
                            Text("Confirm & Create Account", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
