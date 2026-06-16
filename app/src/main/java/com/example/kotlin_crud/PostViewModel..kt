package com.example.kotlin_crud

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

sealed interface UiState {
    object Idle : UiState
    object Loading : UiState
    data class Success(val posts: List<Post>) : UiState
    data class Error(val message: String) : UiState
}

class PostViewModel : ViewModel() {
    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> = _uiState

    init {
        readPosts() // Fetch initial list data
    }

    // READ
    fun readPosts() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = RetrofitClient.apiService.getPosts()
                _uiState.value = UiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown Error")
            }
        }
    }

    // CREATE
    fun createPost(title: String, body: String) {
        viewModelScope.launch {
            try {
                val newPost = Post(title = title, body = body)
                RetrofitClient.apiService.createPost(newPost)
                readPosts() // Refresh backend list representation
            } catch (e: Exception) { /* Handle error state */ }
        }
    }

    // UPDATE
    fun updatePost(id: Int, title: String, body: String) {
        viewModelScope.launch {
            try {
                val updated = Post(id = id, title = title, body = body)
                RetrofitClient.apiService.updatePost(id, updated)
                readPosts() // Refresh backend list representation
            } catch (e: Exception) { /* Handle error state */ }
        }
    }

    // DELETE
    fun deletePost(id: Int) {
        viewModelScope.launch {
            try {
                RetrofitClient.apiService.deletePost(id)
                readPosts() // Refresh backend list representation
            } catch (e: Exception) { /* Handle error state */ }
        }
    }
}
