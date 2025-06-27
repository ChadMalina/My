package com.example.my.presentation.Global.Cards

import com.example.my.ui.theme.Blue80
import com.example.my.ui.theme.Teal80
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.my.Data.model.Database.Submissions


@Composable
fun ManagementCard(
    submission: Submissions,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick() }
            .border(3.dp, Teal80, shape = RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(
            red = 255,
            green = 255,
            blue = 255,
            alpha = 143
        ),),

        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center, // Center the column in the card
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // First row: time and deadline
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = submission.createdTime, fontSize = 16.sp,color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Title
                Text(
                    text = submission.submissiontitle,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue80
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Name and Class
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "By: ${submission.doctor}", fontSize = 16.sp,color = Color.Black)
                    Text(text = submission.divisionName, fontSize = 16.sp,color = Color.Black)
                }
            }
        }
    }
}



