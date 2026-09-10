package com.example.phylax.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phylax.data.model.ChatMessage
import com.example.phylax.data.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository = ChatRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    var uiState by mutableStateOf(ChatUiState())
        private set

    private var currentChatId: String? = null

    fun initChat(otherUserId: String) {
        val currentUserId = auth.currentUser?.uid ?: return
        val chatId = repository.getChatId(currentUserId, otherUserId)
        currentChatId = chatId
        
        viewModelScope.launch {
            repository.getMessages(chatId).collectLatest { messages ->
                uiState = uiState.copy(messages = messages)
            }
        }
    }

    fun sendMessage(text: String, otherUserId: String) {
        val chatId = currentChatId ?: return
        val currentUserId = auth.currentUser?.uid ?: return
        
        val message = ChatMessage(
            senderId = currentUserId,
            receiverId = otherUserId,
            text = text
        )

        viewModelScope.launch {
            repository.sendMessage(chatId, message)
        }
    }
}

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false
)
