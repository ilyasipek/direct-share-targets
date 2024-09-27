package com.ilyasipek.targetusersshortcuts

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.Person
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.ilyasipek.targetusersshortcuts.ui.theme.TargetUsersShortcutsTheme

const val firstTimeChatOpen = false

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (firstTimeChatOpen) {
            // 1. Call setCategories() to associate the shortcut with the appropriate mimeType attributes. For example, for an SMS app, if the contact is not RCS- or MMS-enabled, you wouldn't associate the corresponding shortcut with non-text MIME types such as image/* and video/*.
            // 2. For a given conversation, once a dynamic shortcut is pushed and usage is reported, don't change the shortcut ID. This ensures retention of usage data for ranking.addOrSortShortcuts()
            addOrSortShortcuts()
        }
        enableEdgeToEdge()
        setContent {
            TargetUsersShortcutsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(text = "Send Message Using...")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = {
                                    pushDynamicShortcut(buildUser1Shortcut(SEND_MESSAGE_CAPABILITY))
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "User1")
                            }
                            Button(
                                onClick = {
                                    pushDynamicShortcut(buildUser2Shortcut(SEND_MESSAGE_CAPABILITY))
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "User2")
                            }
                            Button(
                                onClick = {
                                    pushDynamicShortcut(buildUser3Shortcut(SEND_MESSAGE_CAPABILITY))
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "User3")
                            }
                        }

                        Text(text = "Receive Message For...", modifier = Modifier.padding(top = 16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = {
                                    pushDynamicShortcut(buildUser1Shortcut(RECEIVE_MESSAGE_CAPABILITY))
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "User1")
                            }
                            Button(
                                onClick = {
                                    pushDynamicShortcut(buildUser2Shortcut(RECEIVE_MESSAGE_CAPABILITY))
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "User2")
                            }
                            Button(
                                onClick = {
                                    pushDynamicShortcut(buildUser3Shortcut(RECEIVE_MESSAGE_CAPABILITY))
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "User3")
                            }
                        }


                        Text(text = "Select to remove a user's shortcut...", modifier = Modifier.padding(top = 16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = {
                                    removeTargetUser(USER_ID_1)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "User1")
                            }
                            Button(
                                onClick = {
                                    removeTargetUser(USER_ID_2)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "User2")
                            }
                            Button(
                                onClick = {
                                    removeTargetUser(USER_ID_3)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "User3")
                            }
                        }
                    }
                }
            }
        }
    }

    /***
     * This function can be called either when we open the chat/app for the first to add the shortcuts
     * or to reorder the existing shortcuts
     *
     * Note: Don't publish shortcuts that are stale; a conversation with
     * no user activity in the last 30 days is considered stale.
     * This step can be provided by a WorkManager that checks everyday if there are any stale conv. and removes them.
     * */
    private fun addOrSortShortcuts() {
        val shortcutInfo = buildUser1Shortcut()

        val shortcutInfo2 = buildUser2Shortcut()

        val shortcutInfo3 = buildUser3Shortcut()

        ShortcutManagerCompat.setDynamicShortcuts(this, listOf(shortcutInfo, shortcutInfo2, shortcutInfo3))
    }

    private fun pushDynamicShortcut(shortcutInfoCompat: ShortcutInfoCompat) {
        val isPushed = ShortcutManagerCompat.pushDynamicShortcut(this, shortcutInfoCompat)
        val toast = if (isPushed) {
            "Pushed"
        } else {
            "Not pushed"
        }
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
    }

    /**
     * When a user deletes a conversation, remove the target user as well.
     *
     * */
    private fun removeTargetUser(shortcutId: String) {
        ShortcutManagerCompat.removeLongLivedShortcuts(this, listOf(shortcutId))
        Toast.makeText(this, "Shortcuts is removed.", Toast.LENGTH_SHORT).show()
    }

    private fun buildUser1Shortcut(
        capability: String? = null,
    ) = ShortcutInfoCompat.Builder(this, USER_ID_1)
        .setShortLabel("iipek")
        .setIcon(IconCompat.createWithResource(this, R.drawable.ic_launcher_foreground))
        .setLongLabel("Ilyas ipek")
        .setCategories(categories)
        .setLongLived(true)
        .setPerson(
            Person.Builder()
                .setName("Ilyas ipek")
                .build()
        )
        .setIntent(getChatActivityIntent()) // Also you can use setIntents to create a stack of activities for example (MainActivity -> ChatActivity) this will enable the user to navigate back to an activity we want
        .apply {
            if (capability != null) {
                addCapabilityBinding(capability)
            }
        } // add  "message.recipient.@type", listOf("Audience") for groups or add actions.intent.RECEIVE_MESSAGE for reciving messages
        .build()

    private fun buildUser2Shortcut(capability: String? = null) = ShortcutInfoCompat.Builder(this, USER_ID_2)
        .setShortLabel("username2")
        .setIcon(IconCompat.createWithResource(this, R.drawable.baseline_17mp_24))
        .setLongLabel("Name Lastname2")
        .setCategories(categories)
        .setLongLived(true)
        .setPerson(
            Person.Builder()
                .setName("Name Lastname2")
                .build()
        )
        .setIntent(getChatActivityIntent()) // Also you can use setIntents to create a stack of activities for example (MainActivity -> ChatActivity) this will enable the user to navigate back to an activity we want
        .apply {
            if (capability != null) {
                addCapabilityBinding(capability)
            }
        }
        .build()

    private fun buildUser3Shortcut(
        capability: String? = null,
    ) = ShortcutInfoCompat.Builder(this, USER_ID_3)
        .setShortLabel("username3")
        .setIcon(IconCompat.createWithResource(this, R.drawable.baseline_17mp_24))
        .setLongLabel("Name Lastname3")
        .setCategories(categories)
        .setLongLived(true)
        .setPerson(
            Person.Builder()
                .setName("Name Lastname3")
                .build()
        )
        .setIntent(getChatActivityIntent()) // Also you can use setIntents to create a stack of activities for example (MainActivity -> ChatActivity) this will enable the user to navigate back to an activity we want
        .apply {
            if (capability != null) {
                addCapabilityBinding(capability)
            }
        }
        .build()

    private fun getChatActivityIntent() = Intent(this, ChatActivity::class.java).apply {
        action = Intent.ACTION_VIEW
    }

    companion object {
        const val SEND_MESSAGE_CAPABILITY = "actions.intent.SEND_MESSAGE"
        const val RECEIVE_MESSAGE_CAPABILITY = "actions.intent.RECEIVE_MESSAGE"

        const val USER_ID_1 = "user1"
        const val USER_ID_2 = "user2"
        const val USER_ID_3 = "user3"

        // this categories should be the same as the ones in the shortcuts.xml
        val categories = setOf(
            "com.example.android.sharingshortcuts.category.TEXT_SHARE_TARGET",
            "com.example.android.sharingshortcuts.category.IMAGE_SHARE_TARGET",
            "com.example.android.sharingshortcuts.category.VIDEO_SHARE_TARGET",
        )
    }
}