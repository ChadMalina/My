package com.example.my.Data.Auth

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.example.my.Data.model.Auth.User
import com.example.my.presentation.navigation.ROUT_DOCTOR_DASHBOARD
import com.example.my.presentation.navigation.ROUT_LOGIN
import com.example.my.presentation.navigation.ROUT_PATIENT_DASHBOARD
import com.example.my.presentation.navigation.ROUT_REGISTER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import androidx.compose.runtime.State


class UserAuthViewModel(
    var navController: NavController, var context: Context){
    val mAuth: FirebaseAuth


    init {
        mAuth = FirebaseAuth.getInstance()
    }
    // This should hold the currently signed-in user's full data
    val _currentUserData = mutableStateOf<User?>(null)
    val currentUserData: State<User?> = _currentUserData

    fun signup(
        name: String,
        email: String,
        medicalcode: String,
        password: String,
        confpassword: String,
        patientordoctor: String
    ){
        if (name.isBlank() || email.isBlank() || medicalcode.isBlank() || password.isBlank() || confpassword.isBlank()) {
            Toast.makeText(context, "Name, email, medical code, and password cannot be blank", Toast.LENGTH_LONG).show()
            return
        } else if (password != confpassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
            return
        }

        //verify the medical code
        val hospitalRef = FirebaseDatabase.getInstance().getReference()
        val query = hospitalRef.orderByChild("info/medicalCode").equalTo(medicalcode)

        query.get().addOnSuccessListener { snapshot ->
            if ( !snapshot.exists()) {
                Toast.makeText(context, "Hospital does not exist in our database", Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }

            // Retrieve matched hospital name
            var matchedHospitalName: String? = null
            snapshot.children.forEach { matchedHospitalName = it.key }
            matchedHospitalName?.let { hospitalName ->
                // Create user in firebase Auth
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = mAuth.currentUser!!.uid
                            val user = User(
                                name = name,
                                email = email,
                                medicalcode = medicalcode,
                                password = password,
                                patientordoctor = patientordoctor,
                                id = userId
                            )

                            _currentUserData.value = user

                            // Save user under hospitalName/Role/Name/info
                            val userRef = FirebaseDatabase.getInstance()
                                .getReference("$hospitalName/$patientordoctor/$name/info")
                                .setValue(user).addOnCompleteListener { saveTask ->
                                    if(saveTask.isSuccessful) {
                                        Toast.makeText(context,"Registration Successfully", Toast.LENGTH_SHORT).show()
                                        if (patientordoctor.equals("Patient",true)) {
                                            navController.navigate(ROUT_PATIENT_DASHBOARD ){
                                                popUpTo(0) {inclusive = true}
                                                launchSingleTop = true // prevents creating multiple copies of the same destination
                                            }
                                        } else {
                                            navController.navigate(ROUT_DOCTOR_DASHBOARD){
                                                popUpTo(0) { inclusive = true } // clears entire backstack
                                                launchSingleTop = true // avoids creating multiple copies of the same destination
                                        }
                                    }
                                }
                        }
                    } else {
                Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                navController.navigate(ROUT_REGISTER)
            }
            }
        }
    }.addOnFailureListener {
        Toast.makeText(context, "Servers failed: ${it.message}", Toast.LENGTH_LONG).show()
    }
}

fun mainsignin(email: String, password: String) {
    if (email.isBlank() || password.isBlank()) {
        Toast.makeText(context, "Email and password cannot be blank", Toast.LENGTH_LONG).show()
        return
    }

    mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { authTask ->
            if (!authTask.isSuccessful) {
                Toast.makeText(context, "Login failed: incorrect email or password", Toast.LENGTH_LONG).show()
                return@addOnCompleteListener
            }else{

                val currentUid = mAuth.currentUser!!.uid
                val dbRef = FirebaseDatabase.getInstance().getReference()

                dbRef.get().addOnSuccessListener { snapshot ->
                    // Search through Hospital->Patient/Doctor
                    snapshot.children.forEach { hospital ->
                        val hospitalname = hospital.key ?: return@forEach

                        // Check Patients
                        hospital.child("Patient").children.forEach { node ->
                            val idInDb = node.child("info/id").getValue(String::class.java)
                            if (idInDb == currentUid) {
                                val user = node.child("info").getValue(User::class.java)
                                if (user != null) {
                                    _currentUserData.value = user
                                }
                                Toast.makeText(context, "Welcome Patient", Toast.LENGTH_SHORT)
                                    .show()
                                navController.navigate(ROUT_PATIENT_DASHBOARD){
                                    popUpTo(0) { inclusive = true } // clears entire backstack
                                    launchSingleTop = true // avoids creating multiple copies of the same destination
                                }
                                return@addOnSuccessListener
                            }
                        }

                        // Check Doctors
                        hospital.child("Doctor").children.forEach { node ->
                            val idInDb = node.child("info/id").getValue(String::class.java)
                            if (idInDb == currentUid) {
                                val user = node.child("info").getValue(User::class.java)
                                if (user != null) {
                                    _currentUserData.value = user
                                }
                                Toast.makeText(context, "Welcome Doctor", Toast.LENGTH_SHORT)
                                    .show()
                                navController.navigate(ROUT_DOCTOR_DASHBOARD){
                                    popUpTo(0) { inclusive = true } // clears entire backstack
                                    launchSingleTop = true // avoids creating multiple copies of the same destination
                                }
                                return@addOnSuccessListener
                            }
                        }
                    }

                    Toast.makeText(context, "User role not found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to get user data", Toast.LENGTH_SHORT).show()
            }
        }
}

fun logout() {
    mAuth.signOut()
    navController.navigate(ROUT_LOGIN) {
        popUpTo(0)  // clear backstack so user can’t hit “back” into the app
    }
}





fun isLoggedIn(): Boolean = mAuth.currentUser != null
}

