package com.example.jejecomesassignment.di

import com.example.jejecomesassignment.data.firebase.FirebaseRepositoryImpl
import com.example.jejecomesassignment.domain.repository.BusinessCardRepository
import com.example.jejecomesassignment.domain.usecase.SaveBusinessCardUseCase
import com.example.jejecomesassignment.domain.usecase.UpdateBusinessCardFieldUseCase
import com.example.jejecomesassignment.presentation.viewModel.BusinessCardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel // ✅ Required
import org.koin.dsl.module

val appModule = module {
    single<BusinessCardRepository> { FirebaseRepositoryImpl() }
    single { SaveBusinessCardUseCase(get()) }
    single { UpdateBusinessCardFieldUseCase(get()) }
    viewModel { BusinessCardViewModel() } // ✅ Now type inferred correctly
}
