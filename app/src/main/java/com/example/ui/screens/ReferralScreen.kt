package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Referral
import com.example.data.model.User
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

@Composable
fun ReferralScreen(
    user: User?,
    referrals: List<Referral>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val refCode = user?.referralCode ?: "PK-786012"
    val inviteLink = "https://pakinvestment.pk/join?ref=$refCode"

    val totalInvited = referrals.size
    val qualifiedCount = referrals.count { it.status == "QUALIFIED" }
    val totalReferralEarned = referrals.filter { it.status == "QUALIFIED" }.sumOf { it.bonusAmount }

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
                text = "Referral Program",
                color = PakWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Earn Rs. 75 bonus for every friend who activates Plan A.",
                color = PakGold,
                fontSize = 13.sp
            )
        }

        // Hero Referral Bonus Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("referral_hero_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(listOf(PakGoldDark, PakGold))
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF261E06), Color(0xFF141A14))
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "REFERRAL REWARD",
                                color = PakGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PakGold.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "INSTANT BONUS",
                                    color = PakGold,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Rs. 75 / Member",
                            color = PakWhite,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Share your referral link with friends on WhatsApp or social media. When they register and activate Plan A, Rs. 75 is automatically credited to your balance.",
                            color = PakTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats Summary Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x33000000))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Total Invited", color = PakTextSecondary, fontSize = 11.sp)
                                Text(text = "$totalInvited", color = PakWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Box(modifier = Modifier.width(1.dp).height(28.dp).background(PakDarkBorder))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Active Qualified", color = PakTextSecondary, fontSize = 11.sp)
                                Text(text = "$qualifiedCount", color = PakEmeraldLight, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Box(modifier = Modifier.width(1.dp).height(28.dp).background(PakDarkBorder))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Bonus Earned", color = PakTextSecondary, fontSize = 11.sp)
                                Text(text = "Rs. ${totalReferralEarned.toInt()}", color = PakGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }

        // Share Code & Link Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PakDarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PakDarkBorder, PakDarkBorder)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "YOUR UNIQUE REFERRAL CODE",
                        color = PakTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(PakDarkSurfaceVariant)
                            .border(1.dp, PakGold.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = refCode,
                            color = PakGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(refCode))
                                Toast.makeText(context, "Referral Code Copied!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Code", tint = PakGold, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "INVITE LINK",
                        color = PakTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(PakDarkSurfaceVariant)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = inviteLink,
                            color = PakTextSecondary,
                            fontSize = 12.sp,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(inviteLink))
                                Toast.makeText(context, "Link Copied!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Link", tint = PakEmeraldLight, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Join Pak Investment")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Assalam-o-Alaikum! Join Pak Investment with my Referral Code $refCode and earn daily rewards by watching 15-second videos! Register here: $inviteLink"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Pak Investment via"))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("share_referral_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PakEmeraldGreen,
                            contentColor = PakBlackBg
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Share via WhatsApp / Messages", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Referral History List
        item {
            Text(
                text = "INVITED FRIENDS HISTORY",
                color = PakTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }

        if (referrals.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = PakDarkSurface)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(text = "No invited friends yet. Share your code above!", color = PakTextMuted, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(referrals) { ref ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = PakDarkSurface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = ref.referredName, color = PakWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = ref.referredPhone, color = PakTextSecondary, fontSize = 12.sp)
                            Text(text = "Joined: ${ref.dateFormatted}", color = PakTextMuted, fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (ref.status == "QUALIFIED") "+Rs. ${ref.bonusAmount.toInt()}" else "Pending Plan",
                                color = if (ref.status == "QUALIFIED") PakGold else PakTextMuted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (ref.status == "QUALIFIED") PakEmeraldGreen.copy(alpha = 0.15f)
                                        else PakDarkSurfaceVariant
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (ref.status == "QUALIFIED") "ACTIVE" else "NOT ACTIVATED",
                                    color = if (ref.status == "QUALIFIED") PakEmeraldLight else PakTextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}
