package com.demo.bsf.account.transaction

import com.demo.bsf.account.db.gen.tables.records.TransactionRecord
import com.demo.bsf.account.gen.TransactionRecordGen
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TransactionMapperTest {
    private val unit = TransactionMapper()

    @Test
    fun should_map_transaction_record_to_transaction() {
        // given
        val source: TransactionRecord = TransactionRecordGen.transactionRecord()

        // when
        val transaction: Transaction = unit.toTransaction(source)

        // then
        with(transaction) {
            assertThat(id).isEqualTo(source.id)
            assertThat(accountId).isEqualTo(source.accountId)
            assertThat(sourceAccountId).isEqualTo(source.sourceAccountId)
            assertThat(operationType.name).isEqualTo(source.operationType)
            assertThat(description).isEqualTo(source.description)
            assertThat(amount).isEqualTo(source.amount)
            assertThat(createdAt).isEqualTo(source.createdAt)
        }
    }
}
