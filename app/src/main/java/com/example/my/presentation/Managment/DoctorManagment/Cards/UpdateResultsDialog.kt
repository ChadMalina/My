package com.example.my.presentation.Managment.DoctorManagment.Cards


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.google.firebase.database.FirebaseDatabase
import com.example.my.Data.Auth.UserAuthViewModel
import com.example.my.ui.theme.Teal80

@Composable
fun UploadResultsDialog(
    fileUrlInitial: String,
    hospitalName: String,
    submissionId: String,
    userName: String,
    authViewModel: UserAuthViewModel,
    context: Context,
    onClose: () -> Unit
) {
    var reportfileURL by remember { mutableStateOf(fileUrlInitial) }


    Dialog(onDismissRequest = { onClose() }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Top Row with Title and Close Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Upload Results URL",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Teal80
                    )
                    IconButton(onClick = { onClose() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // File URL TextField
                OutlinedTextField(
                    value = reportfileURL,
                    onValueChange = { reportfileURL = it },
                    label = { Text("File URL") },
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Explanation text
                Text(
                    text = "The URL you are going to paste above is the link to the submission which you have uploaded to a third-party app or website." +
                            " An example is the link generated for public file sharing in cloud services like Google Drive when you choose to share an uploaded file." +
                            " NOTE: The URL must start with https://",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color = Color(red = 103, green = 58, blue = 183, alpha = 255),
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Submit Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            if (reportfileURL.isBlank()) {
                                Toast.makeText(context, "Enter a URL", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            // write to /<schoolName>/Assignments/<assignId>/<userName>/reportfileURL
                            FirebaseDatabase.getInstance()
                                .getReference("$hospitalName/Submissions/$submissionId/$userName/reportfileURL")
                                .setValue(reportfileURL)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Results uploaded", Toast.LENGTH_SHORT).show()
                                    onClose()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}




