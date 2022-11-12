package com.demo.bsf.account.account

import com.demo.bsf.account.account.dto.Account
import com.demo.bsf.account.db.gen.tables.records.AccountRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class AccountMapperTest {
    private val unit: AccountMapper = AccountMapper()

    @Test
    fun should_map_account_record_to_account() {
        // given
        val source: AccountRecord = AccountRecord().apply {
            id = UUID.randomUUID()
            ownerId = UUID.randomUUID()
            balanceAmount = 100_000
            createdAt = LocalDateTime.now()
            modifiedAt = LocalDateTime.now()
        }

        // when
        val account: Account = unit.toAccount(source)

        // then
        with(account) {
            assertThat(id).isEqualTo(source.id)
            assertThat(ownerId).isEqualTo(source.ownerId)
            assertThat(balanceAmount).isEqualTo(source.balanceAmount)
            assertThat(createdAt).isEqualTo(source.createdAt)
            assertThat(modifiedAt).isEqualTo(source.modifiedAt)
        }
    }
}