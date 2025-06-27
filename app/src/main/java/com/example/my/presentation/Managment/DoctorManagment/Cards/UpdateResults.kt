package com.example.my.presentation.Managment.DoctorManagment.Cards


import android.widget.Toast
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
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.my.Data.Auth.UserAuthViewModel
import com.example.my.Data.BookDatabase.SubmissionViewModel
import com.example.my.Data.model.Database.Submissions
import com.example.my.ui.theme.Green80


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateResult(
    navController: NavController,
    SubmissionViewModel: SubmissionViewModel,
    userAuthViewModel: UserAuthViewModel, // Pass UserAuthViewModel
    ResultToEdit: Submissions? = null,
) {

// At top of your composable:
    if (ResultToEdit == null) {
        // You could show a loading spinner, or pop back immediately:
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }
    val Submissions = ResultToEdit


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                Text("Update Assignments", fontWeight = FontWeight.Bold)
            }, actions = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Green80,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White
            )
            )
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .paint(
                    painter = painterResource(R.drawable.img2), contentScale = ContentScale.Crop
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
                // Removed teacher text field, now fetching the teacher name from the current user
                val doctor = userAuthViewModel.currentUserData.value?.name

                var divisionName by remember { mutableStateOf(Submissions.divisionName) }
                var submissiontitle by remember { mutableStateOf(Submissions.submissiontitle) }
                var submissiondescription by remember { mutableStateOf(Submissions.submissiondescription) }
                var fileURL by remember { mutableStateOf(Submissions.fileURL) }

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

                // Assignment Title
                OutlinedTextField(
                    value = submissiontitle,
                    onValueChange = { submissiontitle = it },
                    label = { Text("Title") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.KeyboardArrowUp, contentDescription = null
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = backgroundColor,
                        focusedContainerColor = backgroundColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Assignment Description
                OutlinedTextField(
                    value = submissiondescription,
                    onValueChange = { submissiondescription = it },
                    label = { Text("Description") },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
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
                    leadingIcon = { Icon(Icons.Default.MailOutline, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = backgroundColor,
                        focusedContainerColor = backgroundColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "The URL you are going to paste above is the link to the submission" + " which you have uploaded to a third - party app or website." + " An example is the link generated for public file sharing in cloud services like Google Drive when you choose to share an uploaded file." + " NOTE: The URL must start with https://",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color = Color(red = 103, green = 58, blue = 183, alpha = 255),
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp)
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

                    val context = LocalContext.current


                    Button(
                        onClick = {
                            Submissions.submissionId.let { submissionId ->
                                SubmissionViewModel.updateSubmission(
                                    submissionId = submissionId,
                                    divisionName = divisionName,
                                    title = submissiontitle,
                                    description = submissiondescription,
                                    fileURL = fileURL,
                                    onSuccess = {
                                        // go back to ManageCreatedAssignments
                                        navController.popBackStack()
                                    },
                                    onError = { e ->
                                        Toast.makeText(
                                            context,
                                            "Failed to update: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    })
                            }
                        }, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Update Result")
                    }


                }
            }
        }
    }
}




