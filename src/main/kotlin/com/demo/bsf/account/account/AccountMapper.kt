package com.demo.bsf.account.account

import com.demo.bsf.account.account.dto.Account
import com.demo.bsf.account.db.gen.tables.records.AccountRecord
import org.springframework.stereotype.Component

@Component
class AccountMapper {

    fun toAccount(source: AccountRecord): Account {
        return Account(
            id = source.id,
            ownerId = source.ownerId,
            balanceAmount = source.balanceAmount,
            createdAt = source.createdAt,
            modifiedAt = source.modifiedAt
        )
    }
}