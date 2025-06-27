package com.example.my.presentation.Managment.PatientManagement.Cards

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.*
import com.example.my.Data.Auth.UserAuthViewModel
import com.example.my.ui.theme.Blue80


@Composable
fun CurrentSubmissionDialog(
    hospitalName: String,
    submissiontitle: String,
    authViewModel: UserAuthViewModel,
    submissionId: String,
    submissiondescription: String,
    createdTime: String,
    doctor: String,
    divisionName: String,
    fileURL: String, // ✅ New parameter
    onClose: () -> Unit
) {
    val context = LocalContext.current // ✅ Needed for launching intent
    var showSubmitDialog by remember { mutableStateOf(false) }

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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NEW SUBMISSION",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Blue80
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
                    Text(text = "Doctor: $doctor", color = Color.DarkGray, fontSize = 14.sp)
                    Text(text = "Division: $divisionName", color = Color.DarkGray, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            //opens the dialog box to enter the file URL
                            showSubmitDialog = true
                        }) {
                            Text("Submit")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
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
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK  // ✅ Correct usage
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
                                Toast.makeText(context, "File URL is empty", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Text("Download Submission")
                        }
                    }



                    if (showSubmitDialog) {
                        SubmitFileUrlDialog(
                            fileUrlInitial = "",  // TODO: start empty or pass existing URL here
                            hospitalName = hospitalName,
                            submissionId = submissionId,
                            authViewModel = authViewModel,
                            context = context,
                            onClose = { showSubmitDialog = false } // hide dialog on close
                        )
                    }


                }
            }
        }
    }
}






