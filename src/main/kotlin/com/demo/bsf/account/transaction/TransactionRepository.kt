package com.demo.bsf.account.transaction

import com.demo.bsf.account.db.gen.tables.Transaction.TRANSACTION
import com.demo.bsf.account.db.gen.tables.records.TransactionRecord
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class TransactionRepository @Autowired constructor(private val dslContext: DSLContext) {

    fun create(transaction: TransactionRecord) {
        dslContext.executeInsert(transaction)
    }

    fun findById(transactionId: UUID): TransactionRecord? {
        return dslContext.selectFrom(TRANSACTION)
            .where(TRANSACTION.ID.eq(transactionId))
            .fetchOptional().orElse(null)
    }

    fun listTransactions(accountId: UUID): List<TransactionRecord> {
        return dslContext.selectFrom(TRANSACTION)
            .where(TRANSACTION.SOURCE_ACCOUNT_ID.eq(accountId)
                .or(TRANSACTION.ACCOUNT_ID.eq(accountId)))
            .orderBy(TRANSACTION.CREATED_AT.desc())
            .fetch()
    }
}