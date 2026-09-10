package com.example.phylax.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.phylax.data.model.EmergencySession
import com.example.phylax.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    onRecordClick: (EmergencySession) -> Unit,
    viewModel: HistoryViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    var sessionToEdit by remember { mutableStateOf<EmergencySession?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PrimaryText)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Emergency History",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = PrimaryText,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (uiState.isLoading && uiState.sessions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = LocationBlue)
            }
        } else if (uiState.sessions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No history found", color = SecondaryText)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.sessions) { session ->
                    HistoryItem(
                        session = session, 
                        onClick = { onRecordClick(session) },
                        onEditNote = { sessionToEdit = session },
                        onDelete = { viewModel.deleteRecord(session.id) }
                    )
                }
            }
        }
    }

    sessionToEdit?.let { session ->
        EditNoteDialog(
            initialNote = session.note,
            onDismiss = { sessionToEdit = null },
            onConfirm = { newNote ->
                viewModel.updateNote(session.id, newNote)
                sessionToEdit = null
            }
        )
    }
}

@Composable
fun HistoryItem(
    session: EmergencySession, 
    onClick: () -> Unit,
    onEditNote: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("MMM d — h:mm a", Locale.getDefault())
    val dateString = dateFormat.format(Date(session.startedAt))

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = MaterialTheme.shapes.medium,
                color = EmergencyRed.copy(alpha = 0.1f)
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.padding(12.dp), tint = EmergencyRed)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (session.note.isNotEmpty()) session.note else "Emergency Alert", 
                    style = MaterialTheme.typography.titleMedium.copy(color = PrimaryText, fontWeight = FontWeight.Bold)
                )
                Text(text = dateString, style = MaterialTheme.typography.bodySmall.copy(color = SecondaryText))
                if (session.status == "ENDED") {
                    val duration = (session.endedAt ?: session.startedAt) - session.startedAt
                    val minutes = duration / (1000 * 60)
                    Text(text = "Resolved after $minutes minutes", style = MaterialTheme.typography.bodyMedium.copy(color = SecondaryText))
                }
            }
            
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit Title/Note") },
                        onClick = {
                            showMenu = false
                            onEditNote()
                        },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete", color = EmergencyRed) },
                        onClick = {
                            showMenu = false
                            onDelete()
                        },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = EmergencyRed) }
                    )
                }
            }
        }
    }
}

@Composable
fun EditNoteDialog(
    initialNote: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var note by remember { mutableStateOf(initialNote) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Alert Note") },
        text = {
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Title / Note") },
                placeholder = { Text("e.g., Accident at Main St") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(note) }) {
                Text("SAVE", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL")
            }
        }
    )
}
