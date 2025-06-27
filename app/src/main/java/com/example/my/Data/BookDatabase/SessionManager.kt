package com.example.my.Data.BookDatabase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object SessionManager {
    fun fetchCurrentUserHospitalName(callback: (String?) -> Unit) {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return callback(null)
        val rootRef = FirebaseDatabase.getInstance().reference

        rootRef.get().addOnSuccessListener { snapshot ->
            for (hospital in snapshot.children) {
                val HospitalName = hospital.key ?: continue

                for (role in listOf("Patient", "Doctor")) {
                    val roleNode = hospital.child(role)
                    for (user in roleNode.children) {
                        val userInfo = user.child("info")
                        val uidInDb = userInfo.child("id").getValue(String::class.java)
                        if (uidInDb == currentUid) {
                            callback(HospitalName)
                            return@addOnSuccessListener
                        }
                    }
                }
            }
            callback(null) // Not found
        }.addOnFailureListener {
            callback(null)
        }
    }

    fun fetchCurrentUsername(callback: (String?) -> Unit) {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return callback(null)
        val rootRef = FirebaseDatabase.getInstance().reference

        rootRef.get().addOnSuccessListener { snapshot ->
            for (hospital in snapshot.children) {
                for (role in listOf("Patient", "Doctor")) {
                    val roleNode = hospital.child(role)
                    for (user in roleNode.children) {
                        val userInfo = user.child("info")
                        val uidInDb = userInfo.child("id").getValue(String::class.java)
                        if (uidInDb == currentUid) {
                            val username = user.key
                            callback(username)
                            return@addOnSuccessListener
                        }
                    }
                }
            }
            callback(null) // Not found
        }.addOnFailureListener {
            callback(null)
        }
    }
}

