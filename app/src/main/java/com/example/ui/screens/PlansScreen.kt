package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.User
import com.example.ui.components.ComplianceNoticeCard
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
import com.example.ui.viewmodel.ScreenTab

@Composable
fun PlansScreen(
    user: User?,
    onActivatePlan: (String, Double) -> Unit,
    onNavigate: (ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PakBlackBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Investment Plans",
                color = PakWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Choose a verified plan to unlock daily video tasks & earnings.",
                color = PakTextSecondary,
                fontSize = 13.sp
            )
        }

        // Compliance banner
        item {
            ComplianceNoticeCard()
        }

        // Plan A Card (Primary requested plan)
        item {
            val isCurrentPlan = user?.isPlanActive == true && user.activePlanName.contains("Plan A")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("plan_a_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(
                        listOf(PakEmeraldGreen, PakGold)
                    ),
                    width = 2.dp
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PakEmeraldGreen)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "MOST POPULAR",
                                    color = PakBlackBg,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Plan A",
                                color = PakWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (isCurrentPlan) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PakEmeraldGreen.copy(alpha = 0.2f))
                                    .border(1.dp, PakEmeraldGreen, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    color = PakEmeraldGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "Rs. 100",
                            color = PakGold,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "/ one-time activation",
                            color = PakTextSecondary,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    PlanFeatureRow("YouTube Video Watching System")
                    PlanFeatureRow("Video Duration: 15 Seconds only")
                    PlanFeatureRow("Daily Video Limit: 10 videos")
                    PlanFeatureRow("Reward: Rs. 15 per verified video")
                    PlanFeatureRow("Proposed Cycle Return: Up to Rs. 1,500*")
                    PlanFeatureRow("Direct JazzCash & EasyPaisa Withdrawal")

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isCurrentPlan) {
                        Button(
                            onClick = { onNavigate(ScreenTab.WATCH_EARN) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PakEmeraldGreen,
                                contentColor = PakBlackBg
                            )
                        ) {
                            Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Start Watching Videos Now", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { onActivatePlan("Plan A (Rs. 100)", 100.0) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("activate_plan_a_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PakGold,
                                contentColor = PakBlackBg
                            )
                        ) {
                            Text("Activate Plan A (Rs. 100)", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Plan B Card (Growth Plan)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PakDarkBorder, PakDarkBorder)))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Plan B (Growth)",
                        color = PakWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "Rs. 500",
                            color = PakWhite,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "/ activation",
                            color = PakTextSecondary,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    PlanFeatureRow("Daily Video Limit: 20 videos")
                    PlanFeatureRow("Video Duration: 15 Seconds")
                    PlanFeatureRow("Reward: Rs. 25 per verified video")
                    PlanFeatureRow("Priority Admin Approval")

                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedButton(
                        onClick = { onActivatePlan("Plan B (Rs. 500)", 500.0) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.linearGradient(listOf(PakEmeraldGreen, PakEmeraldGreen)))
                    ) {
                        Text("Activate Plan B (Rs. 500)", color = PakEmeraldGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Plan C Card (Executive Plan)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PakDarkBorder, PakDarkBorder)))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Plan C (Executive)",
                        color = PakWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "Rs. 1,000",
                            color = PakWhite,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "/ activation",
                            color = PakTextSecondary,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    PlanFeatureRow("Daily Video Limit: 30 videos")
                    PlanFeatureRow("Reward: Rs. 35 per verified video")
                    PlanFeatureRow("Instant SMS withdrawal notifications")
                    PlanFeatureRow("Dedicated VIP Support via WhatsApp")

                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedButton(
                        onClick = { onActivatePlan("Plan C (Rs. 1,000)", 1000.0) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.linearGradient(listOf(PakGold, PakGold)))
                    ) {
                        Text("Activate Plan C (Rs. 1,000)", color = PakGold, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun PlanFeatureRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = PakEmeraldGreen,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = PakTextSecondary,
            fontSize = 13.sp
        )
    }
}
