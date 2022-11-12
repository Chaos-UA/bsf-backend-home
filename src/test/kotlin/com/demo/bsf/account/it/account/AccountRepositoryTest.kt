package com.demo.bsf.account.it.account

import com.demo.bsf.account.account.AccountRepository
import com.demo.bsf.account.db.gen.tables.records.AccountRecord
import com.demo.bsf.account.gen.AccountRecordGen
import com.demo.bsf.account.it.BaseIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.temporal.ChronoUnit
import java.util.*

class AccountRepositoryTest @Autowired constructor(
    private val accountRepo: AccountRepository) : BaseIntegrationTest()  {

    @Test
    fun should_not_find_by_id_when_not_exist() {
        assertThat(accountRepo.findById(UUID.randomUUID())).isNull()
    }

    @Test
    fun should_not_find_by_ids_for_update_when_not_exist() {
        assertThat(accountRepo.findByIdsForUpdate(listOf(UUID.randomUUID()))).isEmpty()
    }

    @Test
    fun should_find_account() {
        // given
        val account: AccountRecord = createAccount(AccountRecordGen.accountRecord())

        // then
        assertAccountEquals(accountRepo.findById(account.id)!!, account)
    }

    @Test
    fun should_find_updated_account() {
        // given
        val account: AccountRecord = createAccount(AccountRecordGen.accountRecord())
        account.balanceAmount += 1000

        // when
        accountRepo.update(account)

        // then
        assertAccountEquals(accountRepo.findById(account.id)!!, account)
    }

    fun createAccount(account: AccountRecord): AccountRecord {
        accountRepo.create(account);
        return accountRepo.findById(account.id)!!
    }

    fun assertAccountEquals(actual: AccountRecord, expected: AccountRecord) {
        with(actual) {
            assertThat(id).isEqualTo(expected.id)
            assertThat(ownerId).isEqualTo(expected.ownerId)
            assertThat(balanceAmount).isEqualTo(expected.balanceAmount)
            assertThat(createdAt.truncatedTo(ChronoUnit.MILLIS))
                .isEqualTo(expected.createdAt.truncatedTo(ChronoUnit.MILLIS))
            assertThat(modifiedAt.truncatedTo(ChronoUnit.MILLIS))
                .isEqualTo(expected.modifiedAt.truncatedTo(ChronoUnit.MILLIS))
        }
    }

}
