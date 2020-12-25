package com.github.pool_party.pull_party_bot.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.jodatime.CurrentDateTime
import org.jetbrains.exposed.sql.jodatime.datetime

object Parties : IntIdTable() {
    val name = varchar("name", 50)
    val chatId = reference("chat_id", Chats)
    val users = text("users")
    val lastUse = datetime("last_use").defaultExpression(CurrentDateTime())

    init {
        index(false, name, chatId)
        uniqueIndex(chatId, name)
    }
}

object Chats : LongIdTable() {
    override val id = long("chat_id").entityId()
    val isRude = bool("is_rude").default(false)
}
