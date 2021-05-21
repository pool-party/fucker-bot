package com.github.pool_party.pull_party_bot.commands.handlers.callback

import com.elbekD.bot.Bot
import com.elbekD.bot.types.CallbackQuery
import com.github.pool_party.pull_party_bot.commands.Callback
import com.github.pool_party.pull_party_bot.commands.CallbackAction
import com.github.pool_party.pull_party_bot.commands.messages.ON_PERMISSION_DENY_CALLBACK
import com.github.pool_party.pull_party_bot.commands.messages.onAliasDeleteSuccess
import com.github.pool_party.pull_party_bot.commands.validateAdministrator
import com.github.pool_party.pull_party_bot.database.dao.PartyDao

class DeleteSuggestionCallback(private val partyDao: PartyDao) : Callback {

    override val callbackAction = CallbackAction.DELETE

    override suspend fun Bot.process(callbackQuery: CallbackQuery, partyId: Int) {
        val message = callbackQuery.message

        if (message == null) {
            answerCallbackQuery(callbackQuery.id)
            return
        }

        if (!validateAdministrator(callbackQuery.from, message.chat, false)) {
            answerCallbackQuery(
                callbackQuery.id,
                ON_PERMISSION_DENY_CALLBACK
            )
            return
        }

        val partyName = partyDao.delete(partyId)

        if (partyName != null) {
            answerCallbackQuery(callbackQuery.id, onAliasDeleteSuccess(partyName))
        }

        deleteMessage(message.chat.id, message.message_id)
        answerCallbackQuery(callbackQuery.id)
    }
}