package com.example.jejecomesassignment.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.jejecomesassignment.domain.model.BusinessCard
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BusinessCardViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _saveResult = MutableStateFlow<Boolean?>(null)
    val saveResult: StateFlow<Boolean?> get() = _saveResult

    private var currentUserEmail: String? = null

    fun setUserEmail(email: String) {
        currentUserEmail = email
        val user = hashMapOf("email" to email)
        firestore.collection("users").document(email).set(user)
    }

    fun saveCard(card: BusinessCard) {
        val data = hashMapOf(
            "name" to card.name,
            "phone" to card.phone,
            "email" to card.email,
            "company" to card.company,
            "address" to card.address,
            "savedBy" to currentUserEmail.orEmpty()
        )

        Log.d("SaveCard", "Saving: $data")

        firestore.collection("businessCards")
            .add(data)
            .addOnSuccessListener {
                Log.d("SaveCard", "Success")
                _saveResult.value = true
            }
            .addOnFailureListener { e ->
                Log.e("SaveCard", "Error: ${e.localizedMessage}")
                _saveResult.value = false
            }
    }

    fun updateField(cardId: String, field: String, value: String) {
        firestore.collection("businessCards").document(cardId)
            .update(field, value)
    }
}
