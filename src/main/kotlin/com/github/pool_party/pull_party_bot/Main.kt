package com.github.pool_party.pull_party_bot

import com.elbekD.bot.Bot
import com.github.pool_party.pull_party_bot.commands.initializePingCommands

const val APP_URL = "https://pullpartybot.herokuapp.com"
const val USER_NAME = "PullPartyBot"
const val DEFAULT_PORT = 80

fun main() {
    val token = System.getenv("TELEGRAM_TOKEN") ?: throw RuntimeException("Unable to get system variable for token")
    val bot = Bot.createWebhook(USER_NAME,token) {
        url = "${APP_URL}/${token}"
        allowedUpdates = listOf(AllowedUpdate.Message)

        server {
            host = "0.0.0.0"
            port = System.getenv("PORT")?.toInt() ?: DEFAULT_PORT
        }
    }

    bot.onCommand("/start") { msg, _ ->
        bot.sendMessage(msg.chat.id, "Hello World!")
    }

    bot.start()
}
