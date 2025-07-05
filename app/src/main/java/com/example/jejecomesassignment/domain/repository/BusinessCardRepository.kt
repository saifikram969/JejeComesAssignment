package com.example.jejecomesassignment.domain.repository

import com.example.jejecomesassignment.domain.model.BusinessCard

interface BusinessCardRepository {
    fun saveCard(card: BusinessCard, onResult: (Boolean) -> Unit)
    fun updateCardField(cardId: String, field: String, value: String, onResult: (Boolean) -> Unit)

}