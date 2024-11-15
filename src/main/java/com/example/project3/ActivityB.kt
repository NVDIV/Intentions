package com.example.project3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class ActivityB : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Receive the list of contacts
        val contactList = intent.getStringArrayListExtra("CONTACT_LIST") ?: arrayListOf()

        setContent {
            ActivityBScreen(
                onBackToMain = { finish() },
                onGoToMain = {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                },
                contacts = contactList
            )
        }
    }
}

@Composable
fun ActivityBScreen(onBackToMain: () -> Unit, onGoToMain: () -> Unit, contacts: List<String>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Contacts:")
        contacts.forEach { contact ->
            Text(text = contact)
        }

        Button(onClick = onBackToMain, modifier = Modifier.fillMaxWidth()) {
            Text("Close Activity B")
        }
        Button(onClick = onGoToMain, modifier = Modifier.fillMaxWidth()) {
            Text("Go to Activity A")
        }
    }
}
