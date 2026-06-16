package com.example.kotlin_crud
data class Post(
    val id: Int? = null, // Nullable for creation since the API auto-generates IDs
    val title: String,
    val body: String
)
