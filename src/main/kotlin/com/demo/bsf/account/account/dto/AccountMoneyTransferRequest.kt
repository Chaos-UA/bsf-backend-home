package com.demo.bsf.account.account.dto

import java.util.*

class AccountMoneyTransferRequest(
    var targetAccountId: UUID,
    var amount: Long,
    var description: String?,
    var transactionId: UUID?
)