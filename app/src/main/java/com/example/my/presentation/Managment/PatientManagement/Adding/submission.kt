package com.example.my.presentation.Managment.PatientManagement.Adding

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.my.R
import com.example.my.Data.Auth.UserAuthViewModel
import com.example.my.Data.BookDatabase.SessionManager
import com.example.my.Data.BookDatabase.SubmissionViewModel
import com.example.my.Data.model.Database.Submissions
import com.example.my.presentation.Global.Cards.ManagementCard
import com.example.my.presentation.Managment.PatientManagement.Cards.CurrentSubmissionDialog
import com.example.my.ui.theme.GlobalTopNavBar



@Composable
fun CurrentSubmission(
    navController: NavHostController,
    SubmissionViewModel: SubmissionViewModel,
    authViewModel: UserAuthViewModel
) {
    var selected by remember { mutableStateOf<Submissions?>(null) }
    var currentHospitalName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selected) {
        if (selected != null) {
            SessionManager.fetchCurrentUserHospitalName { name ->
                currentHospitalName = name
            }
        }
    }

    LaunchedEffect(SubmissionViewModel.submissions.size) {
        Log.d("CurrentSubmissions", "Submissions count: ${SubmissionViewModel.submissions.size}")
    }

    Scaffold(
        topBar = {
            GlobalTopNavBar(
                title = "Current Submissions",
                onClose = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .paint(
                    painter = painterResource(R.drawable.img2),
                    contentScale = ContentScale.Crop
                )
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(SubmissionViewModel.submissions) { submissions ->
                    ManagementCard(
                        submission = submissions,
                        onClick = { selected = submissions }
                    )
                } // <--- This closes `items`
            }

            // Show details dialog when an submission is selected
            if (selected != null && currentHospitalName != null) {
                CurrentSubmissionDialog(
                    hospitalName = currentHospitalName!!,
                    submissiontitle = selected!!.submissiontitle,
                    authViewModel = authViewModel,
                    submissionId = selected!!.submissionId,
                    submissiondescription = selected!!.submissiondescription,
                    createdTime = selected!!.createdTime,
                    doctor = selected!!.doctor,
                    divisionName = selected!!.divisionName,
                    fileURL = selected!!.fileURL,
                    onClose = { selected = null }
                )
            }
        }
    }
}






