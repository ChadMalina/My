package com.example.my.presentation.Managment.PatientManagement.Adding


import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.example.my.Data.BookDatabase.SessionManager
import com.example.my.Data.BookDatabase.SubmissionViewModel
import com.example.my.Data.model.Database.Submissions
import com.example.my.R
import com.example.my.presentation.Managment.PatientManagement.Cards.ResultsCard
import com.example.my.ui.theme.Green80
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


@SuppressLint("QueryPermissionsNeeded")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Results(
    navController: NavHostController,
    submissionViewModel: SubmissionViewModel,
) {
    val context = LocalContext.current
    var hospitalName by remember { mutableStateOf<String?>(null) }

    var submissions by remember {
        mutableStateOf<List<Triple<Submissions, String, String>>>(
            emptyList()
        )
    }

    // Fetch the current hospital name once
    LaunchedEffect(Unit) {
        SessionManager.fetchCurrentUserHospitalName { sn ->
            hospitalName = sn
            if (sn != null) {
                val newList = submissionViewModel.createdSubmissions.flatMap { submission ->
                    val submissionId = submission.submissionId
                    val snapshot = runBlocking {
                        FirebaseDatabase.getInstance().getReference("$sn/Submissions/$submissionId")
                            .get().await()
                    }

                    snapshot.children.filter { it.key != "info" }.mapNotNull { userSnap ->
                        val userName = userSnap.key ?: return@mapNotNull null
                        val reportfileURL =
                            userSnap.child("reportfileURL").getValue(String::class.java)
                        if (!reportfileURL.isNullOrBlank()) {
                            Triple(submission, userName, reportfileURL)
                        } else null
                    }
                }
                submissions = newList
            }
        }
    }

    LaunchedEffect(submissions.size) {
        Log.d("Results", "Submissions size: ${submissions.size}")
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Results", fontWeight = FontWeight.Bold) }, actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Green80,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .paint(
                    painterResource(R.drawable.img2), contentScale = ContentScale.Crop
                )
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(submissions) { (submissions, userName, reportfileURL) ->
                ResultsCard(
                    Submissions = submissions,
                    userName = userName,
                    reportfileURL = reportfileURL,
                    onDownloadClick = {
                        if (reportfileURL.isNotBlank()) {

                            val uri = reportfileURL.toUri()

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
                                        context, "No app available to open link", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "File URL is empty", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }
}





