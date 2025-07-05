package com.example.jejecomesassignment.data.firebase


import com.example.jejecomesassignment.domain.model.BusinessCard
import com.example.jejecomesassignment.domain.repository.BusinessCardRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepositoryImpl : BusinessCardRepository {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("business_cards")

   override fun saveCard(card: BusinessCard, onResult: (Boolean) -> Unit) {
        val key = card.id.ifEmpty { database.push().key ?: "" }
        val updatedCard = card.copy(id = key)

        database.child(key).setValue(updatedCard)
            .addOnCompleteListener { onResult(it.isSuccessful) }
    }

   override fun updateCardField(cardId: String, field: String, value: String, onResult: (Boolean) -> Unit) {
        database.child(cardId).child(field).setValue(value)
            .addOnCompleteListener { onResult(it.isSuccessful) }
    }
}
