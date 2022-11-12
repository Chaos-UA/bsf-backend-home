package com.demo.bsf.account.account.dto

import java.time.LocalDateTime
import java.util.*

data class Account (
    var id: UUID,
    var ownerId: UUID,
    var balanceAmount: Long,
    var createdAt: LocalDateTime,
    var modifiedAt: LocalDateTime
)