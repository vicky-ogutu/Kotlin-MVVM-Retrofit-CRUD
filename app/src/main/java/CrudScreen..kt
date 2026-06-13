import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CrudScreen(viewModel: PostViewModel = viewModel()) {
    val state by viewModel.uiState
    var titleInput by remember { mutableStateOf("") }
    var bodyInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Entry Inputs for Create
        OutlinedTextField(
            value = titleInput,
            onValueChange = { titleInput = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = bodyInput,
            onValueChange = { bodyInput = it },
            label = { Text("Body Content") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Button(
            onClick = {
                if (titleInput.isNotBlank() && bodyInput.isNotBlank()) {
                    viewModel.createPost(titleInput, bodyInput)
                    titleInput = ""
                    bodyInput = ""
                }
            },
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth()
        ) {
            Text("Create Post")
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Render States dynamically
        when (val result = state) {
            is UiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
            }
            is UiState.Success -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(result.posts) { post ->
                        PostItemCard(
                            post = post,
                            onUpdate = { viewModel.updatePost(post.id ?: 0, "Updated title", "Updated body") },
                            onDelete = { viewModel.deletePost(post.id ?: 0) }
                        )
                    }
                }
            }
            is UiState.Error -> Text("Network Error: ${result.message}", color = MaterialTheme.colorScheme.error)
            else -> {}
        }
    }
}

@Composable
fun PostItemCard(post: Post, onUpdate: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = post.title, style = MaterialTheme.typography.titleMedium)
            Text(text = post.body, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onUpdate) { Text("Update Mock") }
                TextButton(onClick = onDelete, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                    Text("Delete")
                }
            }
        }
    }
}
