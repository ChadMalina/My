package com.example.my.Data.Auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.example.my.Data.model.Auth.Hospital
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.my.presentation.navigation.ROUT_LOGIN
import com.example.my.presentation.navigation.ROUT_HOSPITAL_DASHBOARD
import com.example.my.presentation.navigation.ROUT_HOSPITAL_REGISTER

class HospitalAuthViewModel(var navController: NavController, var context: Context) {
    val mAuth: FirebaseAuth

    init {
        mAuth = FirebaseAuth.getInstance()
    }

    private val _currenthospitalData = mutableStateOf<Hospital?>(null)
    val _currentHospitalData: State<Hospital?> = _currenthospitalData

    fun Hospitalregister(
        hospitalname: String,
        hospitalemail: String,
        medicalcode: String,
        hospitalpassword: String,
        hospitalconfpassword: String
    ) {
        if (hospitalname.isBlank() || hospitalemail.isBlank() || medicalcode.isBlank() || hospitalpassword.isBlank()) {
            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_LONG).show()
        } else if (hospitalpassword != hospitalconfpassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
        } else {
            mAuth.createUserWithEmailAndPassword(hospitalemail, hospitalpassword)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val hospitalId = mAuth.currentUser!!.uid

                        // Store only safe school data (NOT password)
                        val hospitalInfo = mapOf(
                            "hospitalname" to hospitalname,
                            "hospitalemail" to hospitalemail,
                            "hospitalCode" to medicalcode,
                            "hospitalpassword" to hospitalpassword,
                            "uid" to hospitalId
                        )

                        val regRef = FirebaseDatabase.getInstance()
                            .getReference("$hospitalname/info")

                        regRef.setValue(hospitalInfo).addOnCompleteListener {
                            if (it.isSuccessful) {

                                _currenthospitalData.value = Hospital(
                                    hosipitalname = hospitalname,
                                    hosipitalemail = hospitalemail,
                                    medicalcode = medicalcode,
                                    hospitalpassword = hospitalpassword,
                                    id = mAuth.currentUser!!.uid
                                )
                                Toast.makeText(
                                    context,
                                    "Registered Successfully: Welcome, hospital manager",
                                    Toast.LENGTH_LONG
                                ).show()
                                navController.navigate(ROUT_HOSPITAL_DASHBOARD) {
                                    popUpTo(0) { inclusive = true } // clears entire backstack
                                    launchSingleTop =
                                        true // avoids creating multiple copies of the same destination
                                }
                            } else {
                                //Log.e("ERROR_FB", "Hospitalregister: ", it.exception)
                                Toast.makeText(
                                    context,
                                    "${it.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Registration failed: ${it.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        navController.navigate(ROUT_HOSPITAL_REGISTER)
                    }
                }
        }
    }


    fun hospitalsignin(email: String, password: String) {

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Email and password cannot be blank", Toast.LENGTH_LONG)
                .show()
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val rootRef = FirebaseDatabase.getInstance().getReference()
                    rootRef.get().addOnSuccessListener { snap ->
                        snap.children.forEach { hospitalNode ->
                            val info = hospitalNode.child("info")
                            if (info.child("uid")
                                    .getValue(String::class.java) == mAuth.currentUser!!.uid
                            ) {
                                val hospital = info.getValue(Hospital::class.java)
                                if (hospital != null) {
                                    // ─── NEW: set your state ─────────────────────
                                    _currenthospitalData.value = hospital
                                    // ───────────────────────────────────────────────
                                }
                                return@addOnSuccessListener
                            }
                        }
                    }
                    Toast.makeText(
                        this.context,
                        "Login successful: Welcome hospital manager",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate(ROUT_HOSPITAL_DASHBOARD) {
                        popUpTo(0) { inclusive = true } // clears entire backstack
                        launchSingleTop =
                            true // avoids creating multiple copies of the same destination
                    }
                } else {
                    Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    fun isLoggedIn(): Boolean {
        return mAuth.currentUser != null
    }

    fun logout() {
        mAuth.signOut()
        navController.navigate(ROUT_LOGIN) {
            popUpTo(0)
        }
    }
}