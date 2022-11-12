package com.demo.bsf.account.transaction

import com.demo.bsf.account.db.gen.tables.records.TransactionRecord
import com.demo.bsf.account.gen.TransactionRecordGen
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class TransactionServiceTest {

    val transactionRepo: TransactionRepository = mock(TransactionRepository::class.java);
    val transactionMapper: TransactionMapper = spy(TransactionMapper())
    val unit: TransactionService = TransactionService(transactionRepo, transactionMapper)

    @Test
    fun should_delegate_call_to_repo() {
        // given
        val transactionRecord: TransactionRecord = TransactionRecordGen.transactionRecord()
        `when`(transactionRepo.listTransactions(transactionRecord.accountId)).thenReturn(listOf(transactionRecord))

        // when
        val transactions: List<Transaction> = unit.getAccountTransactions(transactionRecord.accountId)

        // then
        assertThat(transactions.size).isEqualTo(1)
        assertThat(transactions.get(0).id).isEqualTo(transactionRecord.id)
        verify(transactionRepo).listTransactions(transactionRecord.accountId)
        verify(transactionMapper).toTransaction(transactionRecord)
    }

}
