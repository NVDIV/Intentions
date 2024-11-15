package com.example.project3

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.provider.CalendarContract
import android.text.format.DateUtils
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(this)
        }
    }
}

@Composable
fun MainScreen(context: Context) {
    var textField1 by remember { mutableStateOf(TextFieldValue()) }
    var textField2 by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = textField1,
            onValueChange = { textField1 = it },
            label = { Text("Pole 1") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = textField2,
            onValueChange = { textField2 = it },
            label = { Text("Pole 2") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { sendSMS(context, textField1.text, textField2.text) }, Modifier.fillMaxWidth()) {
                Text("Send SMS")
            }
            Button(onClick = { findGeo(context, textField1.text) }, Modifier.fillMaxWidth()) {
                Text("Find Geo")
            }
            Button(onClick = { createDocument(context) }, Modifier.fillMaxWidth()) {
                Text("Create Document")
            }
            Button(onClick = { sendEmail(context, textField1.text, textField2.text) }, Modifier.fillMaxWidth()) {
                Text("Send Email")
            }
            Button(onClick = { makeCall(context, textField2.text) }, Modifier.fillMaxWidth()) {
                Text("Make Call")
            }
            Button(onClick = { openMusicPlayer(context) }, Modifier.fillMaxWidth()) {
                Text("Open Music Player")
            }
            Button(onClick = { openCalendarEvent(context, textField1.text, textField2.text) }, Modifier.fillMaxWidth()) {
                Text("Add to Calendar")
            }
            Button(
                onClick = {
                    val contactList = arrayListOf(textField1.text, textField2.text)
                    val intent = Intent(context, ActivityB::class.java).apply {
                        putStringArrayListExtra("CONTACT_LIST", contactList)
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open Activity B with Contacts")
            }
            Button(onClick = { (context as? ComponentActivity)?.finishAffinity() }, Modifier.fillMaxWidth()) {
                Text("Close App")
            }
        }
    }
}

fun sendSMS(context: Context, phoneNumber: String, message: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("smsto:$phoneNumber")
        putExtra("sms_body", message)
    }
    context.startActivity(intent)
}

fun findGeo(context: Context, location: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("geo:0,0?q=$location")
    }
    context.startActivity(intent)
}

fun createDocument(context: Context) {
    // Intent to open a document creation app, if available
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/pdf"  // Example type; adjust as needed
        putExtra(Intent.EXTRA_TITLE, "NewDocument")
    }
    context.startActivity(intent)
}

fun sendEmail(context: Context, emailAddress: String, subject: String) {

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Only email apps handle this.
        putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    context.startActivity(intent)
}

fun makeCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    context.startActivity(intent)
}

fun openMusicPlayer(context: Context) {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_APP_MUSIC)
    }
    context.startActivity(intent)
}

fun openCalendarEvent(context: Context, dateTime: String, title: String) {
    // Expected format: "yyyy-MM-dd HH:mm"
    val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val parsedDate = try {
        dateTimeFormat.parse(dateTime)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    if (parsedDate == null) {
        // Show an error message if the date/time format is incorrect
        Toast.makeText(context, "Invalid date format. Use 'yyyy-MM-dd HH:mm'.", Toast.LENGTH_SHORT).show()
        return
    }

    // Set event start and end times
    val startMillis = parsedDate.time
    val endMillis = startMillis + 60 * 60 * 1000 // 1-hour duration

    // Create an intent to insert a calendar event
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, title)
        putExtra(CalendarContract.Events.EVENT_LOCATION, "Lublin")
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
    }

    context.startActivity(intent)
}



