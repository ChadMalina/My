package com.example.my.presentation.components.Register


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.my.R
import com.example.my.Data.Auth.HospitalAuthViewModel
import com.example.my.Data.Auth.MedicalCodeGenerator
import com.example.my.presentation.navigation.ROUT_HOSPITAL_LOGIN
import com.example.my.presentation.navigation.ROUT_PATIENT_DASHBOARD
import com.example.my.ui.theme.Blue80


@SuppressLint("SuspiciousIndentation")
@Composable
fun HospitalRegister(
    navController: NavHostController,
    hospitalAuthViewModel: HospitalAuthViewModel
) {
    Column(
        modifier = Modifier
            .paint(
                painter = painterResource(R.drawable.img2),
                contentScale = ContentScale.Crop
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Text(
            text = "REGISTER YOUR HOSPITAL",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color = Color(red = 255, green = 255, blue = 255, alpha = 255),
            modifier = Modifier
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))

        var hospitalname by remember { mutableStateOf("") }
        var hospitalemail by remember { mutableStateOf("") }
        val medicalcode: MutableState<String> = remember { mutableStateOf("") }
        var hospitalpassword by remember { mutableStateOf("") }
        var hospitalconfpassword by remember { mutableStateOf("") }

        OutlinedTextField(
            value = hospitalname,
            onValueChange = { hospitalname = it },
            label = { Text(text = "Hospital Name", fontFamily = FontFamily.SansSerif) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            shape = RoundedCornerShape(5.dp)

        )


        OutlinedTextField(
            value = hospitalemail,
            onValueChange = { hospitalemail = it },
            label = { Text(text = "Hospital Email Address", fontFamily = FontFamily.SansSerif) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            shape = RoundedCornerShape(5.dp)
        )


        Spacer(modifier = Modifier.height(10.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            OutlinedTextField(
                value = medicalcode.value,
                onValueChange = {},
                label = { Text(text = "Hospital Code", fontFamily = FontFamily.SansSerif) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = { Icon(imageVector = Icons.Default.Place, contentDescription = "") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp),
                shape = RoundedCornerShape(5.dp),
                readOnly = true
            )

            Button(
                onClick = {
                    MedicalCodeGenerator.generateMedicalCode { medicalCode ->
                        medicalcode.value = medicalCode
                    }
                },
                colors = ButtonDefaults.buttonColors(Blue80),
                modifier = Modifier
                    .height(56.dp),
                shape = RoundedCornerShape(5.dp),
            ) {
                Text(text = "GENERATE")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "The code generated above is your hospital code." +
                    " This will be used by both patients and doctors to register to your specific hospital.",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color = Color(red = 103, green = 58, blue = 183, alpha = 255),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(10.dp))


        OutlinedTextField(
            value = hospitalpassword,
            onValueChange = { hospitalpassword = it },
            label = { Text(text = "Password", fontFamily = FontFamily.SansSerif) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            shape = RoundedCornerShape(5.dp)

        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = hospitalconfpassword,
            onValueChange = { hospitalconfpassword = it },
            label = { Text(text = "Confirm Password", fontFamily = FontFamily.SansSerif) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            shape = RoundedCornerShape(5.dp)

        )

        Spacer(modifier = Modifier.height(10.dp))




        Button(
            onClick = { hospitalAuthViewModel.Hospitalregister(hospitalname, hospitalemail,
                medicalcode.toString(), hospitalpassword, hospitalconfpassword) },
            colors = ButtonDefaults.buttonColors(
                Color(
                    red = 103,
                    green = 58,
                    blue = 183,
                    alpha = 255
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(text = "REGISTER")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { navController.navigate(ROUT_HOSPITAL_LOGIN) },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(
                text = "Already a member?",
                fontFamily = FontFamily.SansSerif,
                color = Color(red = 200, green = 200, blue = 201, alpha = 255),
                fontSize = 16.sp
            )
            Text(
                text = " Login here",
                fontFamily = FontFamily.SansSerif,
                color = Color(red = 0, green = 188, blue = 212, alpha = 255),
                fontSize = 16.sp
            )
        }

    }
}

