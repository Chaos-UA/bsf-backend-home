package com.demo.bsf.account.gen

import com.demo.bsf.account.db.gen.tables.records.TransactionRecord
import com.demo.bsf.account.transaction.TransactionOperationType
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

object TransactionRecordGen {

    fun transactionRecord(): TransactionRecord {
        return TransactionRecord().apply {
            id = UUID.randomUUID()
            accountId = UUID.randomUUID()
            sourceAccountId = UUID.randomUUID()
            operationType = TransactionOperationType.ACCOUNT_MONEY_TRANSFER.name
            description = "Transfer money to another account"
            amount = 1000
            createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)
        }
    }

    fun transactionRecord(accountId: UUID, sourceAccountId: UUID): TransactionRecord {
        return this.transactionRecord().apply {
            this.accountId = accountId
            this.sourceAccountId = sourceAccountId
        }
    }
}