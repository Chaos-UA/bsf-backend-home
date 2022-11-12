package com.demo.bsf.account.it.transaction

import com.demo.bsf.account.account.AccountRepository
import com.demo.bsf.account.db.gen.tables.records.AccountRecord
import com.demo.bsf.account.db.gen.tables.records.TransactionRecord
import com.demo.bsf.account.gen.AccountRecordGen
import com.demo.bsf.account.gen.TransactionRecordGen
import com.demo.bsf.account.it.BaseIntegrationTest
import com.demo.bsf.account.transaction.TransactionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.temporal.ChronoUnit
import java.util.*

class TransactionRepositoryTest @Autowired constructor(
    val accountRepo: AccountRepository,
    val transactionRepo: TransactionRepository) : BaseIntegrationTest()  {

    @Test
    fun should_not_find_by_id_when_not_exist() {
        assertThat(transactionRepo.findById(UUID.randomUUID())).isNull()
    }

    @Test
    fun should_create_find_and_list() {
        // given
        val account: AccountRecord = AccountRecordGen.accountRecord()
        accountRepo.create(account)
        val sourceAccount: AccountRecord = AccountRecordGen.accountRecord()
        accountRepo.create(sourceAccount)
        val transaction: TransactionRecord = TransactionRecordGen.transactionRecord(account.id, sourceAccount.id)

        // when
        transactionRepo.create(transaction)

        // then
        assertTransactionEquals(transactionRepo.findById(transaction.id)!!, transaction)

        with(transactionRepo.listTransactions(account.id)) {
            assertThat(this.size).isEqualTo(1)
            assertTransactionEquals(this.get(0), transaction)
        }
        with(transactionRepo.listTransactions(sourceAccount.id)) {
            assertThat(this.size).isEqualTo(1)
            assertTransactionEquals(this.get(0), transaction)
        }
    }

    fun assertTransactionEquals(actual: TransactionRecord, expected: TransactionRecord) {
        with(actual) {
            assertThat(id).isEqualTo(expected.id)
            assertThat(accountId).isEqualTo(expected.accountId)
            assertThat(sourceAccountId).isEqualTo(expected.sourceAccountId)
            assertThat(operationType).isEqualTo(expected.operationType)
            assertThat(description).isEqualTo(expected.description)
            assertThat(amount).isEqualTo(expected.amount)
            assertThat(createdAt.truncatedTo(ChronoUnit.MILLIS))
                .isEqualTo(expected.createdAt.truncatedTo(ChronoUnit.MILLIS))
        }
    }
}