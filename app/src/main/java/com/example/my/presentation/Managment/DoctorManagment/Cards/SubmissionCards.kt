package com.example.my.presentation.Managment.DoctorManagment.Cards


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.my.Data.Auth.UserAuthViewModel
import com.example.my.Data.model.Database.Submissions
import com.example.my.ui.theme.Teal80


@Composable
fun SubmissionCard(
    submission: Submissions,
    authViewModel: UserAuthViewModel,
    navController: NavController,
    userName: String,
    onDownloadClick: () -> Unit,
    onUploadClick: () -> Unit
) {

    val context = LocalContext.current


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp), colors = CardDefaults.cardColors(
            containerColor = Color(
                red = 255, green = 255, blue = 255, alpha = 143
            ),
        ), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
            ) {


                Spacer(modifier = Modifier.height(8.dp))

                Text(text = submission.divisionName, fontSize = 16.sp, color = Color.Black)

                Text(
                    text = userName, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Teal80
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = submission.submissiontitle, fontSize = 16.sp, color = Color.Black)
                }
            }

            Button(
                onClick = { onDownloadClick() },
                modifier = Modifier.padding(end = 1.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, contentColor = Color.Black
                ),
                contentPadding = PaddingValues(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Download Assignment",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Download \nAssignment",
                        lineHeight = 12.sp,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            }

            Button(
                onClick = { onUploadClick() },
                modifier = Modifier.padding(end = 16.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, contentColor = Color.Black
                ),
                contentPadding = PaddingValues(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Upload Results",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Upload \nResults",
                        lineHeight = 12.sp,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

