package com.example.my.presentation.Managment.DoctorManagment


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.my.R
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import com.example.my.Data.Auth.UserAuthViewModel
import com.example.my.Data.BookDatabase.SubmissionViewModel
import com.example.my.presentation.navigation.ROUT_DOCTOR_DASHBOARD
import com.example.my.ui.theme.Blue80
import com.example.my.ui.theme.Teal80
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSubmission(
    navController: NavController,
    submissionViewModel: SubmissionViewModel,
    userAuthViewModel: UserAuthViewModel, // Pass UserAuthViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Create Submissions", fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue80,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .paint(
                    painter = painterResource(R.drawable.img2),
                    contentScale = ContentScale.Crop
                )
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Removed doctor text field, now fetching the teacher name from the current user
                val doctor = userAuthViewModel.currentUserData.value?.name

                var divisionName by remember { mutableStateOf("") }
                var submissiontitle by remember { mutableStateOf("") }
                var submissiondescription by remember { mutableStateOf("") }
                var fileURL by remember { mutableStateOf("") }
                var createdTime by remember { mutableStateOf("") }

                val backgroundColor = Color.White.copy(alpha = 0.5f)

                // Class Name
                OutlinedTextField(
                    value = divisionName,
                    onValueChange = { divisionName = it },
                    label = { Text("Class Name") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = backgroundColor,
                        focusedContainerColor = backgroundColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Submission Title
                OutlinedTextField(
                    value = submissiontitle,
                    onValueChange = { submissiontitle = it },
                    label = { Text("Title") },
                    leadingIcon = { Icon(Icons.Default.Create, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = backgroundColor,
                        focusedContainerColor = backgroundColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Submission Description
                OutlinedTextField(
                    value = submissiondescription,
                    onValueChange = { submissiondescription = it },
                    label = { Text("Description") },
                    leadingIcon = { Icon(Icons.Default.Build, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = backgroundColor,
                        focusedContainerColor = backgroundColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 5
                )
                // File URL
                OutlinedTextField(
                    value = fileURL,
                    onValueChange = { fileURL = it },
                    label = { Text("File URL") },
                    leadingIcon = { Icon(Icons.Default.Create, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = backgroundColor,
                        focusedContainerColor = backgroundColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "The URL you are going to paste above is the link to the SUBMISSION which you have uploaded to a third - party app or website." +
                            " An example is the link generated for public file sharing in cloud services like Google Drive when you choose to share an uploaded file." +
                            "  NOTE: The URL must start with https://",
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
                        .padding(start = 20.dp, end = 20.dp),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                    createdTime = sdf.format(Date())

                    Button(
                        onClick = {
                            // Validate input fields
                            if (divisionName.isBlank() || submissiontitle.isBlank() || submissiondescription.isBlank() ||  fileURL.isBlank()) {
                                Toast.makeText(
                                    navController.context,
                                    "Please fill all fields",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (doctor.isNullOrBlank()) {
                                // Teacher name is missing
                                Toast.makeText(
                                    navController.context,
                                    "Doctor not found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Proceed with submission creation
                                submissionViewModel.createSubmission(
                                    doctor!!, // Doctor name
                                    divisionName,
                                    submissiontitle,
                                    submissiondescription,
                                    fileURL,
                                    createdTime, // Pass it here

                                )

                                navController.navigate(ROUT_DOCTOR_DASHBOARD)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Teal80,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Create",
                            fontSize = 18.sp
                        )
                    }

                }
            }
        }
    }
}






