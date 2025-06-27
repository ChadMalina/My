package com.example.my.Data.BookDatabase

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.my.Data.Auth.UserAuthViewModel
import com.example.my.Data.model.Database.Submissions
import com.example.my.presentation.navigation.ROUT_DOCTOR_DASHBOARD

class SubmissionViewModel(
    var navController: NavHostController,
    var context: Context,
    var UserAuthViewModel: UserAuthViewModel
) {


    private val databaseReference = FirebaseDatabase.getInstance().getReference("Submission")

    private val _submissions = mutableStateListOf<Submissions>()
    val submissions: SnapshotStateList<Submissions> = _submissions

    private val _createdSubmissions = mutableStateListOf<Submissions>()
    val createdSubmissions: SnapshotStateList<Submissions> = _createdSubmissions


    // Initialize the ViewModel
    init {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            SessionManager.fetchCurrentUserHospitalName { hospitalName: String? ->
                if (!hospitalName.isNullOrEmpty()) {
                    SessionManager.fetchCurrentUsername { currentUsername: String? ->
                        if (!currentUsername.isNullOrEmpty()) {
                            val assignRef = FirebaseDatabase.getInstance()
                                .getReference("$hospitalName/Submissions")

                            assignRef.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    Log.d("SubmissionsViewModel", "Firebase onDataChange triggered")
                                    _submissions.clear()
                                    _createdSubmissions.clear()

                                    if (!snapshot.exists()) {
                                        Log.d(
                                            "SubmissionsViewModel",
                                            "No submissions found at path"
                                        )
                                    }

                                    for (child in snapshot.children) {
                                        val submissions = child.getValue(Submissions::class.java)
                                        if (submissions != null) {
                                            Log.d(
                                                "SubmissionsViewModel",
                                                "Loaded submissions: ${submissions.submissiontitle} by ${submissions.doctor}"
                                            )
                                            _submissions.add(submissions)
                                            if (submissions.doctor == currentUsername) {
                                                _createdSubmissions.add(submissions)
                                            }
                                        } else {
                                            Log.d(
                                                "SubmittedViewModel",
                                                "Failed to parse submission from snapshot: ${child.key}"
                                            )
                                        }
                                    }
                                    Log.d(
                                        "SubmissionViewModel",
                                        "Total submissions loaded: ${_submissions.size}"
                                    )
                                    Log.d(
                                        "SubmissionViewModel",
                                        "Created submissions count: ${createdSubmissions.size}"
                                    )

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.e(
                                        "SubmissionViewModel",
                                        "Firebase listener cancelled: ${error.message}"
                                    )
                                    Toast.makeText(
                                        context,
                                        "Failed to load submission: ${error.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        } else {
                            Toast.makeText(
                                context,
                                "Failed to get your username",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Failed to get your hospital name", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }


    fun createSubmission(
        doctor: String,
        divisionName: String,
        submissiontitle: String,
        submissiondescription: String,
        fileURL: String,
        createdTime: String,

        ) {
        // Generate unique ID for the submission
        val submissionId = System.currentTimeMillis().toString()

        // Get current user and school name
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        // Fetch school name from session manager
        SessionManager.fetchCurrentUserHospitalName { hospitalName ->
            if (hospitalName != null) {
                // Create the assignment object
                val submission = Submissions(
                    doctor = doctor,
                    divisionName = divisionName,
                    submissiontitle = submissiontitle,
                    submissiondescription = submissiondescription,
                    fileURL = fileURL,
                    createdTime = createdTime,
                    submissionId = submissionId,
                )

                // Save submission in Firebase under <hospitalname>/Submissions/<submissionId>
                val assignmentRef = FirebaseDatabase.getInstance()
                    .getReference("$hospitalName/Submissions/$submissionId")
                assignmentRef.setValue(submission).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            this.context,
                            "Submission created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(ROUT_DOCTOR_DASHBOARD)
                    } else {
                        Toast.makeText(
                            this.context,
                            "Error uploading submission",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(this.context, "Failed to fetch hospital name", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun updateSubmission(
        submissionId: String,
        divisionName: String,
        title: String,
        description: String,
        fileURL: String,
        onSuccess: () -> Unit,        // ← callback instead
        onError: (Throwable) -> Unit  // ← optional error callback
    ) {
        SessionManager.fetchCurrentUserHospitalName { hospitalName ->
            if (hospitalName != null) {
                val assignmentRef = FirebaseDatabase.getInstance()
                    .getReference("$hospitalName/Submissions/$submissionId")

                val updates = mapOf<String, Any>(
                    "divisionName" to divisionName,
                    "subimissiontitle" to title,
                    "submissiondescription" to description,
                    "fileURL" to fileURL,
                )

                assignmentRef.updateChildren(updates)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onError(e) }
            } else {
                Toast.makeText(context, "Failed to fetch hospital name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteSubmission(submissionId: String) {
        SessionManager.fetchCurrentUserHospitalName { hospitalName ->
            if (hospitalName != null) {
                val ref = FirebaseDatabase.getInstance()
                    .getReference("$hospitalName/Submissions/$submissionId")

                ref.removeValue()
                    .addOnSuccessListener {
                        Log.d("SubmissionViewModel", "Submission deleted")
                    }
                    .addOnFailureListener { e ->
                        Log.e("SubmissionViewModel", "Failed to delete: ${e.message}")
                    }
            }
        }
    }

}