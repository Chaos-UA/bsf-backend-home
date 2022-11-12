package com.demo.bsf.account.gen

import com.demo.bsf.account.db.gen.tables.records.AccountRecord
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

object AccountRecordGen {

    fun accountRecord(): AccountRecord {
        return AccountRecord().apply {
            id = UUID.randomUUID()
            ownerId = UUID.randomUUID()
            balanceAmount = 100_000
            createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)
            modifiedAt = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)
        }
    }

}