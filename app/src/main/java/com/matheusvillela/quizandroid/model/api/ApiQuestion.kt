package com.matheusvillela.quizandroid.model.api

data class ApiQuestion(
    val id: String,
    val statement: String,
    val options: List<String>
)