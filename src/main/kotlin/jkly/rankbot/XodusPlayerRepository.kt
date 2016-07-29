package jkly.rankbot

import jetbrains.exodus.entitystore.EntityStore
import jetbrains.exodus.entitystore.StoreTransaction
import jkly.rankbot.elo.Player

class XodusPlayerRepository(val entityStore: EntityStore) : PlayerRepository {
    override fun get(id: String): Player {
        throw UnsupportedOperationException()
    }

    override fun save(player: Player) {
        var txn: StoreTransaction? = entityStore.beginTransaction()
        try {
            do {
                if (txn != entityStore.currentTransaction) {
                    txn = null
                }
            } while (!(txn?.flush()?:false))
        } finally {
            txn?.abort()
        }
    }


}