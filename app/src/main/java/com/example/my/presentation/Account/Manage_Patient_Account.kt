package com.example.my.presentation.Account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.my.Data.Auth.HospitalAuthViewModel
import com.example.my.Data.Auth.UserAuthViewModel
import com.example.my.ui.theme.GlobalTopNavBar
import com.example.my.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape


@Composable
fun ManagePatientAccount(
    navController: NavController,
    userAuthViewModel: UserAuthViewModel,
    hospitalAuthViewModel: HospitalAuthViewModel
){

    val User = userAuthViewModel._currentUserData.value
    val userName = User?.name ?: "Unknown user"
    val email = User?.email ?: "Unknown email"
    val medicalcode = User?.medicalcode ?: "Unknown code"


    Scaffold(
        topBar = {
            GlobalTopNavBar(
                title = "Manage Your Account",
                onClose = {navController.popBackStack()}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .paint(
                    painter = painterResource(R.drawable.img2),
                    contentScale = ContentScale.Crop
                ).fillMaxSize().padding(paddingValues).padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = userName.uppercase(),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Column(

                ) {
                    CompositionLocalProvider(
                        LocalTextStyle provides MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif

                        )
                    ){
                        Text("Email")
                        Text("Medical Code")
                    }
                }

                Spacer(modifier = Modifier.width(2.dp))

                Column(
                ) {
                    CompositionLocalProvider(
                        LocalTextStyle provides MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.DarkGray
                        )
                    ) {
                        Text(":")
                        Text(":")
                    }
                }

                Spacer(modifier = Modifier.width(5.dp))

                Column(
                ) {
                    CompositionLocalProvider(
                        LocalTextStyle provides MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 22.sp,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.DarkGray
                        )
                    ) {
                        Text(text = email)
                        Text(text = medicalcode)
                    }

                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            Button(
                onClick = {userAuthViewModel.logout()},
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.DarkGray
                ),
                modifier = Modifier.fillMaxWidth().height(60.dp).padding(vertical = 4.dp)
            ) {
                Text(
                    text = "LOGOUT",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
