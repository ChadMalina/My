package com.example.my.presentation.Account

import com.example.my.ui.theme.Green80
import com.example.my.ui.theme.Teal80
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.my.R
import com.example.my.Data.Auth.HospitalAuthViewModel
import com.example.my.Data.Auth.UserAuthViewModel
import com.example.my.ui.theme.GlobalTopNavBar


@Composable
fun ManageHospitalAccount(
    navController: NavController,
    UserAuthViewModel: UserAuthViewModel,
    HospitalAuthViewModel: HospitalAuthViewModel
) {

    val hospital = HospitalAuthViewModel._currentHospitalData.value
    val hospitalname = hospital?.hosipitalname ?: "Hospital"
    val hospitalemail = hospital?.hosipitalemail ?: "Unknown email"
    val medicalcode = hospital?.medicalcode ?: "Unknown school code"

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
                )
                .fillMaxSize()
                .padding(paddingValues)
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = hospitalname.uppercase(),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Teal80,
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
                            fontFamily = FontFamily.SansSerif,
                            color = Color.DarkGray
                        )
                    ) {
                        Text("Email")
                        Text("School Code")
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
                        Text(text = hospitalemail)
                        Text(text = medicalcode)
                    }
                }


            }

            Spacer(modifier = Modifier.height(20.dp))



            Button(
                onClick = {HospitalAuthViewModel.logout()},
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green80,
                    contentColor = Color.DarkGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(vertical = 4.dp)
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


