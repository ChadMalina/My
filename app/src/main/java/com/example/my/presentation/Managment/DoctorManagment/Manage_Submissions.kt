package com.example.my.presentation.Managment.DoctorManagment

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import com.example.my.presentation.Managment.DoctorManagment.Cards.DoctorManagementCard
import com.example.my.presentation.Managment.DoctorManagment.Cards.UploadResultsDialog
import com.example.my.ui.theme.Blue80
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.google.firebase.database.FirebaseDatabase
import com.example.my.R
import com.example.my.Data.Auth.UserAuthViewModel
import com.example.my.Data.BookDatabase.SubmissionViewModel
import com.example.my.Data.BookDatabase.SessionManager
import com.example.my.Data.model.Database.Submissions
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.rememberCoroutineScope
import com.example.my.presentation.Managment.DoctorManagment.Cards.SubmissionCard
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageSubmissions(
    navController: NavHostController,
    submissionViewModel: SubmissionViewModel,
    authViewModel: UserAuthViewModel
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var selected by remember { mutableStateOf<Submissions?>(null) }
    var hospitalName by remember { mutableStateOf<String?>(null) }
    var selectedSubmission by remember { mutableStateOf<Submissions?>(null) }
    var selectedUserName by remember { mutableStateOf<String?>(null) }

    // 1️⃣ Fetch current hospital once
    LaunchedEffect(Unit) {
        SessionManager.fetchCurrentUserHospitalName { hospitalName = it }
    }

    // 2️⃣ Build flat list of (submission, userName)
    val submissions: List<Pair<Submissions, String>> = remember(hospitalName, submissionViewModel.createdSubmissions) {
        val sn = hospitalName ?: return@remember emptyList()
        submissionViewModel.createdSubmissions.flatMap { submissions ->
            val snapshot = runBlocking {
                FirebaseDatabase.getInstance()
                    .getReference("$sn/Submissions/${submissions.submissionId}")
                    .get()
                    .await()
            }
            snapshot.children
                // only children that have a “submittedfile” property get through
                .filter { ds -> ds.hasChild("submittedfile") }
                .mapNotNull { it.key }           // now these are true student usernames
                .map { userName -> submissions to userName }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Submitted Submissions", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor        = Blue80,
                    titleContentColor     = Color.White,
                    actionIconContentColor= Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .paint(painter = painterResource(R.drawable.img2), contentScale = ContentScale.Crop)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(submissions) { (submission, userName) ->
                SubmissionCard(
                    submission = submission,
                    navController = navController,
                    authViewModel = authViewModel,
                    userName = userName,
                    onUploadClick = {
                        selectedSubmission = submission
                        selectedUserName = userName
                    },
                    onDownloadClick = {
                        scope.launch {

                            // Step 1: Get the current hospital name (already remembered earlier)
                            val sn = hospitalName ?: return@launch  // Exit if school name is null

                            // Step 2: Build the Firebase reference path to this student's submission
                            val ref = FirebaseDatabase.getInstance()
                                .getReference("$sn/Submissions/${submission.submissionId}/$userName")

                            // Step 3: Fetch the snapshot at that path
                            val snap = ref.get().await()  // Use kotlinx.coroutines.tasks.await

                            // Step 4: Try to get the "submittedfile" URL from the snapshot
                            val fileUrl = snap.child("submittedfile")
                                .getValue(String::class.java)
                                .orEmpty()

                            // Step 5: Check if the URL is actually there
                            if (fileUrl.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "No submission URL found",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@launch
                            }

                            // Step 6: Try to open the URL with Chrome Custom Tabs first
                            try {
                                val customTabsIntent = CustomTabsIntent.Builder().build()
                                customTabsIntent.launchUrl(context, Uri.parse(fileUrl))

                            } catch (_: Exception) {
                                // Step 7: Fallback — open with default app chooser
                                val fallbackIntent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl)).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                val chooser = Intent.createChooser(fallbackIntent, "Open with…")

                                // Step 8: Guard against no available app
                                if (chooser.resolveActivity(context.packageManager) != null) {
                                    context.startActivity(chooser)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "No app found to open the link",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                    },

                )
            }
        }

        // 3️⃣ Show dialog when a submission is selected
        if (selectedSubmission != null && selectedUserName != null && hospitalName != null) {
            UploadResultsDialog(
                fileUrlInitial = "",
                hospitalName   = hospitalName!!,
                submissionId   = selectedSubmission!!.submissionId,
                userName       = selectedUserName!!,
                authViewModel  = authViewModel,
                context        = context
            ) {
                selectedSubmission = null
                selectedUserName = null
            }
        }
    }
}




