package jkly.rankbot

import com.google.gson.Gson
import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.EntityStore
import jetbrains.exodus.entitystore.StoreTransaction
import jkly.rankbot.elo.Player

class XodusPlayerRepository(val entityStore: EntityStore) : PlayerRepository {
    val gson = Gson()

    override fun get(id: String): SlackPlayer {
        val txn = entityStore.beginReadonlyTransaction()
        return txn.getWithSlackPlayerId(id)?.toSlackPlayer()?:SlackPlayer(id, Player(1500.0, 0))
    }

    private fun Entity.toSlackPlayer(): SlackPlayer {
        return SlackPlayer(this.getProperty(Field.SLACK_ID.fieldName).toString(),
                gson.fromJson(this.getBlobString(Field.BLOB_NAME.fieldName), Player::class.java))
    }

    override fun save(slackPlayer: SlackPlayer) {
        val txn: StoreTransaction = entityStore.beginTransaction()
        try {
            do {
                val entity = txn.getWithSlackPlayerId(slackPlayer.slackId)?:let {
                    val newEntity = txn.newEntity(XodusEntityType.PLAYER.name)
                    newEntity.setProperty(Field.SLACK_ID.fieldName, slackPlayer.slackId)
                    newEntity
                }
                entity.setBlobString(Field.BLOB_NAME.fieldName, gson.toJson(slackPlayer.player))
            } while (!txn.flush())
        } finally {
            txn.abort()
        }
    }

    private fun StoreTransaction.getWithSlackPlayerId(id:String) : Entity? {
        return this.find(XodusEntityType.PLAYER.name, Field.SLACK_ID.fieldName, id).firstOrNull()
    }

    private enum class Field(val fieldName:String) {
        SLACK_ID("slackId"),
        BLOB_NAME("blob")
    }

}