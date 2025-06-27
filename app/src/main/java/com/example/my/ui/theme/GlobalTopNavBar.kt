package com.example.my.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalTopNavBar(
    title: String, onClose: () -> Unit = {}
) {
    TopAppBar(
        title = {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic
        )
    }, actions = {
        IconButton(onClick = { onClose() }) {
            Icon(Icons.Default.Close, contentDescription = "Close")
        }
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color(0xFF673AB7),
        titleContentColor = Color.White,
        navigationIconContentColor = Color.White,
        actionIconContentColor = Color.White
    )
    )
}