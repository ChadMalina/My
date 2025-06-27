package com.example.my.presentation.Global.Cards

import com.example.my.ui.theme.Teal80

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement

@Composable
fun DownloadSubmissionDetails(
    submissiontitle: String,
    submissiondescription: String,
    createdTime: String,
    doctor: String,
    divisionName: String,
    fileURL: String,
    context: Context,
    onClose: () -> Unit
) {
    Dialog(onDismissRequest = { onClose() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    // Close Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "READ ASSIGNMENT",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Teal80
                        )
                        IconButton(onClick = { onClose() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    // Content
                    Text(text = submissiontitle, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = submissiondescription, fontSize = 16.sp)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Created: $createdTime", color = Color.Gray, fontSize = 14.sp)
                    Text(text = "doctor: $doctor", color = Color.DarkGray, fontSize = 14.sp)
                    Text(text = "Division: $divisionName", color = Color.DarkGray, fontSize = 14.sp)


                    Spacer(modifier = Modifier.height(24.dp))


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            if (fileURL.isNotBlank()) {

                                val uri = Uri.parse(fileURL)

                                // 1) Try Chrome Custom Tabs
                                try {
                                    val customTabs = CustomTabsIntent.Builder().build()
                                    customTabs.launchUrl(context, uri)
                                } catch (_: Exception) {
                                    // 2) Fallback to normal ACTION_VIEW with chooser
                                    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                    val chooser = Intent.createChooser(intent, "Open with…")
                                    // 3) Guard so we don't crash if truly nothing can handle it
                                    if (chooser.resolveActivity(context.packageManager) != null) {
                                        context.startActivity(chooser)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "No app available to open link",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "File URL is empty", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }) {
                            Text("Download Submission")
                        }
                    }
                }
            }
        }
    }
}



