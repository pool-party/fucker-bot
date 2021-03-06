package com.github.pool_party.pull_party_bot.commands.handlers.testcontainers

import com.github.pool_party.pull_party_bot.commands.messages.ON_ARGUMENT_LIST_SUCCESS
import com.github.pool_party.pull_party_bot.commands.messages.ON_LIST_SUCCESS
import kotlin.test.Test

internal class ListingTest : AbstractTestContainerTest() {

    private val partyName = "party"
    private val aliasName = "alias"
    private val members = "@first @second @third"
    private val listMembers = members.filter { it != '@' }
    private val listOutput = "$ON_ARGUMENT_LIST_SUCCESS\n- $listMembers\n  └── `$aliasName`"
    private val admins = "- admin\n  └── `admins` _(reserved)_"

    @Test
    fun `list command test`() {
        -"/create $partyName $members"
        -"/alias $aliasName $partyName"
        -"/list $aliasName"
        +listOutput
    }

    @Test
    fun `list command with deleted party test`() {
        -"/create $partyName $members"
        -"/alias $aliasName $partyName"
        -"/delete $partyName"
        -"/list $aliasName"
        +listOutput
    }

    @Test
    fun `list command with deleted full party test`() {
        -"/create $partyName $members"
        -"/alias $aliasName $partyName"
        -"/delete $aliasName"
        -"/delete $partyName"
        -"/list"
        +"$ON_LIST_SUCCESS\n$admins"
    }

    @Test
    fun `different output formats test`() {
        -"/create $partyName first"
        -"/create first another"
        -"/list first"
        +"""
            $ON_ARGUMENT_LIST_SUCCESS
            - first
              └── `$partyName`
            - another
              └── `first`
        """.trimIndent()
    }

    @Test
    fun `multiple parties list test`() {
        val secondPartyName = "second"
        val secondPartyMembers = "members"

        -"/create $partyName $members"
        -"/alias $aliasName $partyName"
        -"/create $secondPartyName $secondPartyMembers"
        -"/list"
        +(
            "$ON_LIST_SUCCESS\n$admins\n" +
                """
                    - $secondPartyMembers
                      └── `$secondPartyName`
                    - $listMembers
                      ├── `${aliasName.lowercase()}`
                      └── `${partyName.lowercase()}`
                """.trimIndent()
            )
    }

    @Test
    fun `a horses herd list test`() {
        val partyNames = mutableListOf<String>()

        -"/create $partyName $members"

        for (i in 0..100) {
            val currentPartyName = "${"cheval".repeat(7)}-$i"
            partyNames.add(currentPartyName)

            -"/alias $currentPartyName $partyName"
        }

        -"/list"

        val sortedPatyNames = partyNames.reversed().map { "  ├── `$it`" }

        +"$ON_LIST_SUCCESS\n$admins\n- $listMembers\n${sortedPatyNames.take(74).joinToString("\n")}"
        +("${sortedPatyNames.drop(74).joinToString("\n")}\n" + "  └── `$partyName`")
    }
}
