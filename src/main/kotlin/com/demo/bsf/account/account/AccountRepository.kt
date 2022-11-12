package com.demo.bsf.account.account

import com.demo.bsf.account.db.gen.tables.Account.ACCOUNT
import com.demo.bsf.account.db.gen.tables.records.AccountRecord
import com.demo.bsf.account.exception.InternalException
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors

@Repository
class AccountRepository @Autowired constructor(private val dslContext: DSLContext) {

    fun findById(accountId: UUID): AccountRecord? {
        return dslContext.selectFrom(ACCOUNT)
            .where(ACCOUNT.ID.eq(accountId))
            .fetchOptional().orElse(null)
    }

    fun getAccounts(): List<AccountRecord> {
        return dslContext.selectFrom(ACCOUNT)
            .fetch()
    }

    /**
     * Select account records with lock for update transaction.
     * May not return records that are not exist for requested IDs.
     */
    fun findByIdsForUpdate(accountIds: Collection<UUID>): Map<UUID, AccountRecord> {
        return dslContext.selectFrom(ACCOUNT)
            .where(ACCOUNT.ID.`in`(accountIds))
            .forUpdate()
            .fetch()
            .stream().collect(Collectors.toMap(
                { account: AccountRecord -> account.id }, { account: AccountRecord -> account })
            )
    }

    fun update(account: AccountRecord) {
        val modifiedAtBeforeUpdate = account.modifiedAt
        account.modifiedAt = LocalDateTime.now()
        val result: Int = dslContext.update(ACCOUNT)
            .set(account)
            .where(ACCOUNT.ID.eq(account.id).and(ACCOUNT.MODIFIED_AT.eq(modifiedAtBeforeUpdate)))
            .execute()
        if (result != 1) {
            throw InternalException("Failed to update user: $account")
        }
    }

    fun create(account: AccountRecord) {
        dslContext.executeInsert(account)
    }
}