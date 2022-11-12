package com.demo.bsf.account.transaction

import com.demo.bsf.account.db.gen.tables.records.TransactionRecord
import org.springframework.stereotype.Component

@Component
class TransactionMapper {
    fun toTransaction(source: TransactionRecord): Transaction {
        return Transaction(
            id = source.id,
            accountId = source.accountId,
            sourceAccountId = source.sourceAccountId,
            operationType = TransactionOperationType.valueOf(source.operationType),
            description = source.description,
            amount = source.amount,
            createdAt = source.createdAt)
    }
}