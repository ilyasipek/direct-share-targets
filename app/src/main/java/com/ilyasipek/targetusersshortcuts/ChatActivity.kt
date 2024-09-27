package com.ilyasipek.targetusersshortcuts

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ilyasipek.targetusersshortcuts.ui.theme.TargetUsersShortcutsTheme

class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get the shortcut from the intent
        val type = this.intent.type // image/* text/plain video/*
        val action = this.intent.action // should be • "android.intent.action.SEND"
        val shortcutId = this.intent.extras?.getString(Intent.EXTRA_SHORTCUT_ID)
        val getAllDate = "TODO()" // todo
        if(action == Intent.ACTION_SEND && shortcutId.isNullOrBlank().not()){
            Toast.makeText(this, "New stuff has been shared.", Toast.LENGTH_SHORT).show()
        }
        println("Intent is received ${savedInstanceState?.getString(Intent.EXTRA_SHORTCUT_ID)}")
        enableEdgeToEdge()
        setContent {
            TargetUsersShortcutsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text("Hi in chat", modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        println()
    }
}


@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    TargetUsersShortcutsTheme {
        Greeting2("Android")
    }
}