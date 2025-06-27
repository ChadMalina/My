package com.example.my.presentation.Managment.DoctorManagment

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.my.R
import com.example.my.Data.Auth.UserAuthViewModel
import com.example.my.Data.BookDatabase.SubmissionViewModel
import com.example.my.Data.model.Database.Submissions
import com.example.my.presentation.Global.Cards.DownloadSubmissionDetails
import com.example.my.presentation.Managment.DoctorManagment.Cards.DoctorManagementCard
import com.example.my.ui.theme.Blue80
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCreatedResults(
    navController: NavHostController,
    submissionViewModel: SubmissionViewModel,
    authViewModel: UserAuthViewModel
) {
    var selected by remember { mutableStateOf<Submissions?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var hospitalName by remember { mutableStateOf<String?>(null) }
    var selectedToDelete by remember { mutableStateOf<Submissions?>(null) }

    LaunchedEffect(submissionViewModel.createdSubmissions.size) {
        Log.d("Manage Results", "Created results count: ${submissionViewModel.createdSubmissions.size}")
        submissionViewModel.createdSubmissions.forEach {
            Log.d("Manage Results", "submission title: ${it.submissiontitle}, doctor: ${it.doctor}")
        }
    }

    LaunchedEffect(Unit) {
        Log.d("Manage Results", "SubmissionViewModel hash: ${submissionViewModel.hashCode()}")
    }

    LaunchedEffect(submissionViewModel.createdSubmissions.size) {
        Log.d("Manage Results", "Rendering ${submissionViewModel.createdSubmissions.size} submissions")
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Created Submissions", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
        // 1️⃣ Scrollable list of doctor's own submissions
        LazyColumn(
            modifier = Modifier
                .paint(
                    painter = painterResource(R.drawable.img2),
                    contentScale = ContentScale.Crop
                )
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(submissionViewModel.createdSubmissions) { submissions ->
                DoctorManagementCard(
                    submission = submissions,
                    navController = navController,
                    onReadClick = { selected = submissions },
                    onDeleteClick = {selected},

                )
            }
        }

        // 2️⃣ Read / Download dialog
        selected?.let { a ->
            DownloadSubmissionDetails(
                submissiontitle      = a.submissiontitle,
                submissiondescription = a.submissiondescription,
                createdTime      = a.createdTime,
                doctor           = a.doctor,
                divisionName        = a.divisionName,
                fileURL          = a.fileURL,
                context          = LocalContext.current,
                onClose          = { selected = null }
            )
        }

        // 3️⃣ Delete confirmation dialog
        selectedToDelete?.let { toDelete ->
            DeleteConfirmationDialog(
                submissionTitle = toDelete.submissiontitle,
                onConfirm = {
                    submissionViewModel.deleteSubmission(toDelete.submissionId)
                    selectedToDelete = null
                },
                onDismiss = { selectedToDelete = null }
            )
        }
    }
}


@Composable
fun DeleteConfirmationDialog(
    submissionTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Assignment") },
        text = { Text("Are you sure you want to delete \"$submissionTitle\"? This cannot be undone.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}



