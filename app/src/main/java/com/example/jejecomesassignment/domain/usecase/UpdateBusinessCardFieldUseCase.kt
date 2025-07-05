package com.example.jejecomesassignment.domain.usecase

import com.example.jejecomesassignment.domain.repository.BusinessCardRepository


class UpdateBusinessCardFieldUseCase(
    private val repository: BusinessCardRepository
) {
    operator fun invoke(cardId: String, field: String, value: String, onResult: (Boolean) -> Unit) {
        repository.updateCardField(cardId, field, value, onResult)
    }
}