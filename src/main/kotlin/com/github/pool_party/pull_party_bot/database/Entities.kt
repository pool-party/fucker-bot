package com.github.pool_party.pull_party_bot.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and

class Party(id: EntityID<Int>) : IntEntity(id) {

    var name by Parties.name
    var chat by Chat referencedOn Parties.chatId
    var users by Parties.users

    companion object : IntEntityClass<Party>(Parties) {

        fun find(chatId: Long, partyName: String): Party? =
            find { Parties.chatId.eq(chatId) and Parties.name.eq(partyName) }.firstOrNull()

        fun find(partyName: String): Party? = find { Parties.name eq partyName }.firstOrNull()
    }
}

class Chat(id: EntityID<Long>) : LongEntity(id) {

    var isRude by Chats.isRude
    val parties by Party referrersOn Parties.chatId

    companion object : LongEntityClass<Chat>(Chats)
}
