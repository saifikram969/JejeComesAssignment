package com.example.jejecomesassignment.domain.model

data class BusinessCard(
    val id: String = "",               // 🔴 Add this line
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val company: String = "",
    val address: String = ""
)
