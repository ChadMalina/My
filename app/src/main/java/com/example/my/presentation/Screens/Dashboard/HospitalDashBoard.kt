package com.example.my.presentation.Screens.Dashboard


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.my.R
import com.example.my.Data.Auth.HospitalAuthViewModel
import com.example.my.presentation.navigation.ROUT_ABOUT
import com.example.my.presentation.navigation.ROUT_MANAGE_HOSPITAL_ACCOUNT
import com.example.my.ui.theme.Blue80
import com.example.my.ui.theme.Green80
import com.example.my.ui.theme.Teal80


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalDashboard(
    navController: NavHostController, HospitalAuthViewModel: HospitalAuthViewModel
) {

    val hospital = HospitalAuthViewModel._currentHospitalData.value
    val hospitalName = hospital?.hosipitalname ?: "Hospital"
    val medicalcode = hospital?.medicalcode ?: "Code"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                Text(
                    text = "Hospital Info", fontWeight = FontWeight.Bold, fontSize = 20.sp
                )
            }, actions = {
                IconButton(onClick = { navController.navigate(ROUT_MANAGE_HOSPITAL_ACCOUNT) }) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Manage Account")
                }
                IconButton(onClick = { navController.navigate(ROUT_ABOUT) }) {
                    Icon(Icons.Default.Info, contentDescription = "About")
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF673AB7),
                titleContentColor = Blue80,
                navigationIconContentColor = Teal80,
                actionIconContentColor = Green80
            )
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .paint(
                    painter = painterResource(R.drawable.img2), contentScale = ContentScale.Crop
                )
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .height(50.dp)
                    .background(Color.White.copy(alpha = 0.7f)), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = hospitalName.uppercase(),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    color = Blue80,
                )

            }

            Spacer(modifier = Modifier.height(20.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.7f)), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Welcome, Administrator of $hospitalName to My Hospital App, where all doctors and patients will be freely able to" + " obtain and share submissions with each other.",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.7f)), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "This is the Medical code $medicalcode for your hospital. This will allow doctors and" + " patients to register to be able to freely use the app.",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

            }

            Spacer(modifier = Modifier.height(10.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.7f)), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "I hope your hospital enjoy the services provided by My Hospital App. Thank you for choosing us.",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

            }


        }
    }
}





