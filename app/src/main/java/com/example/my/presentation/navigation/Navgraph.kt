package com.example.my.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.my.Data.Auth.HospitalAuthViewModel
import com.example.my.Data.Auth.UserAuthViewModel
import com.example.my.Data.BookDatabase.SubmissionViewModel
import com.example.my.Data.model.Database.Submissions
import com.example.my.presentation.About.About
import com.example.my.presentation.Account.ManageHospitalAccount
import com.example.my.presentation.Account.ManagePatientAccount
import com.example.my.presentation.Global.AccessAllSubmissions
import com.example.my.presentation.Managment.DoctorManagment.Cards.UpdateResult
import com.example.my.presentation.Managment.DoctorManagment.CreateSubmission
import com.example.my.presentation.Managment.DoctorManagment.ManageCreatedResults
import com.example.my.presentation.Managment.DoctorManagment.ManageSubmissions
import com.example.my.presentation.Managment.PatientManagement.Adding.CurrentSubmission
import com.example.my.presentation.Managment.PatientManagement.Adding.Results
import com.example.my.presentation.Screens.Dashboard.DoctorDashboard
import com.example.my.presentation.Screens.Dashboard.HospitalDashboard
import com.example.my.presentation.Screens.Dashboard.PatientDashboard
import com.example.my.presentation.Splash.SplashScreen
import com.example.my.presentation.components.Login.HospitalLogin
import com.example.my.presentation.components.Login.LoginScreen
import com.example.my.presentation.components.Register.HospitalRegister
import com.example.my.presentation.components.Register.MainRegister
import com.google.firebase.auth.FirebaseAuth


@Composable
fun NavGraph(

) {
    val navController = rememberNavController()
    val startDestination = ROUT_SPLASH

    val context = LocalContext.current
    // Initialize your shared ViewModels only once and remember them
    val authViewModel = remember { UserAuthViewModel(navController, context) }
    val hospitalAuthViewModel = remember { HospitalAuthViewModel(navController, context) }
    val submissionViewModel =
        remember { SubmissionViewModel(navController, context, authViewModel) }
    val currentUser = FirebaseAuth.getInstance().currentUser

    // Navigate to login if not logged in already on launch
    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            navController.navigate(ROUT_LOGIN) {
                popUpTo(0) { inclusive = true } // Clear navigation backstack
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }
        composable(ROUT_LOGIN) {
            LoginScreen(navController, authViewModel)
        }
        composable(ROUT_HOSPITAL_LOGIN) {
            HospitalLogin(navController, hospitalAuthViewModel)
        }

        composable(ROUT_REGISTER) {
            MainRegister(navController, authViewModel)
        }

        composable(ROUT_HOSPITAL_REGISTER) {
            HospitalRegister(navController, hospitalAuthViewModel)
        }

        composable(ROUT_HOSPITAL_DASHBOARD) {
            HospitalDashboard(navController, hospitalAuthViewModel)
        }

        composable(ROUT_PATIENT_DASHBOARD) {
            PatientDashboard(navController, authViewModel)
        }
        composable(ROUT_PATIENT_CURRENT_SUBMISSIONS) {
            CurrentSubmission(
                navController, submissionViewModel, authViewModel
            )
        }
        composable(ROUT_PATIENT_CURRENT_RESULTS) {
            Results(
                navController, submissionViewModel
            )
        }
        composable(ROUT_ACCESS_ALL_SUBMISSIONS) {
            AccessAllSubmissions(
                navController, submissionViewModel, authViewModel
            )
        }
        composable(ROUT_DOCTOR_DASHBOARD) {
            DoctorDashboard(navController, authViewModel)
        }
        composable(ROUT_DOCTOR_CREATE_RESULTS) {
            CreateSubmission(navController, submissionViewModel, authViewModel)
        }
        composable(ROUT_DOCTOR_UPDATE_RESULTS) {
            val resultToEdit = navController
                .previousBackStackEntry
                ?.savedStateHandle
                ?.get<Submissions>("ResultToEdit")

            // Pass it into the composable
            UpdateResult(
                navController = navController,
                SubmissionViewModel = submissionViewModel,
                userAuthViewModel = authViewModel,
                ResultToEdit = resultToEdit


            )
        }

        composable(ROUT_DOCTOR_MANAGE_SUBMISSIONS) {
            ManageSubmissions(navController, submissionViewModel, authViewModel)
        }
        composable(ROUT_DOCTOR_MANAGE_RESULTS) {
            ManageCreatedResults(navController, submissionViewModel, authViewModel)
        }
        composable(ROUT_ABOUT) {
            About(navController)
        }
        composable(ROUT_MANAGE_HOSPITAL_ACCOUNT) {
            ManageHospitalAccount(navController, authViewModel, hospitalAuthViewModel)
        }
        composable(ROUT_MANAGE_PATIENT_ACCOUNT) {
            ManagePatientAccount(navController, authViewModel, hospitalAuthViewModel)
        }

    }
}



