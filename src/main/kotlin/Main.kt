import com.elbekD.bot.Bot
import com.elbekD.bot.types.KeyboardButton
import com.elbekD.bot.types.ReplyKeyboardMarkup

fun main(args: Array<String>) {
    val token = System.getenv("TELEGRAM_TOKEN") ?: throw RuntimeException("Unable to get system variable for token")
    val userName = "PullPartyBot"
    val bot = Bot.createPolling(userName, token)

    bot.onCommand("/start") { msg, _ ->
        bot.sendMessage(msg.chat.id, "Hello World!")
    }

    bot.start()
}
