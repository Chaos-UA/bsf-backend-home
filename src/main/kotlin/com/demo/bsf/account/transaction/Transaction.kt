package com.demo.bsf.account.transaction

import java.time.LocalDateTime
import java.util.*

data class Transaction (
    var id: UUID,
    var accountId: UUID,
    /**
     * Source account ID present for ACCOUNT_MONEY_TRANSFER operation type.
     */
    var sourceAccountId: UUID?,
    var operationType: TransactionOperationType,
    var description: String?,
    var amount: Long,
    var createdAt: LocalDateTime
)