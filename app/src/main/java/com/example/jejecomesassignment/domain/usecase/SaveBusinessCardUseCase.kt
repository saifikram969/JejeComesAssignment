package com.example.jejecomesassignment.domain.usecase

import com.example.jejecomesassignment.domain.model.BusinessCard
import com.example.jejecomesassignment.domain.repository.BusinessCardRepository

class SaveBusinessCardUseCase(
    private val repository: BusinessCardRepository
){
    operator fun invoke(card: BusinessCard, onResult: (Boolean) -> Unit) {
        repository.saveCard(card, onResult)
    }
}