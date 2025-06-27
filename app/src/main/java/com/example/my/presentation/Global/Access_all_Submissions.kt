package com.example.my.presentation.Global

import com.example.my.Data.Auth.UserAuthViewModel
import com.example.my.Data.BookDatabase.SubmissionViewModel
import com.example.my.presentation.Global.Cards.DownloadSubmissionDetails
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.my.R
import com.example.my.Data.model.Database.Submissions
import com.example.my.ui.theme.GlobalTopNavBar
import com.example.my.presentation.Global.Cards.ManagementCard

@Composable
fun AccessAllSubmissions(
    navController: NavHostController,
    SubmissionViewModel: SubmissionViewModel,
    authViewModel: UserAuthViewModel
) {
    var selected by remember { mutableStateOf<Submissions?>(null) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            GlobalTopNavBar(
                title = "All Assignments", onClose = { navController.popBackStack() })
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .paint(
                    painter = painterResource(R.drawable.img2), contentScale = ContentScale.Crop
                )
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(SubmissionViewModel.submissions) { submissions ->
                ManagementCard(
                    submission = submissions, onClick = { selected = submissions })
            }
        }

        // Show download dialog on selection
        selected?.let { a ->
            DownloadSubmissionDetails(
                submissiontitle = a.submissiontitle,
                submissiondescription = a.submissiondescription,
                createdTime = a.createdTime,
                doctor = a.doctor,
                divisionName = a.divisionName,
                fileURL = a.fileURL,
                context = context,
                onClose = { selected = null })
        }
    }
}





