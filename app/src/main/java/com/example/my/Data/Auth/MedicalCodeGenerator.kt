package com.example.my.Data.Auth

import kotlin.random.Random
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


object MedicalCodeGenerator {
    private const val CODE_PREFIX = "HOS"
    private const val DIGIT_COUNT = 3
    private const val LETTER_COUNT = 2
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun generateMedicalCode(onCodeGenerated: (String) -> Unit) {
        var MedicalCode: String
        do {
            // Generate a new school code
            MedicalCode = generateRandomMedicalCode()
        } while (isCodeExists(MedicalCode)) // Keep generating until the code does not exist

        // Return the unique code through the callback
        onCodeGenerated(MedicalCode)
    }

    private fun generateRandomMedicalCode(): String {
        val digits = (1..DIGIT_COUNT)
            .map { Random.nextInt(0, 10) }
            .joinToString("")

        val letters = (1..LETTER_COUNT)
            .map { ('A'..'Z').random() }
            .joinToString("")

        return CODE_PREFIX + digits + letters
    }

    private fun isCodeExists(schoolCode: String): Boolean {
        // Check if the generated school code exists in Firebase
        var exists = false
        database.root
            .orderByChild("info/medicalCode")
            .equalTo(schoolCode).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    exists = true
                }
            }
        return exists
    }


}