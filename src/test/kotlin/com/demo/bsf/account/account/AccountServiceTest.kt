package com.demo.bsf.account.account

import com.demo.bsf.account.account.dto.AccountMoneyTransferRequest
import com.demo.bsf.account.db.gen.tables.records.AccountRecord
import com.demo.bsf.account.exception.BadRequestException
import com.demo.bsf.account.exception.NotFoundException
import com.demo.bsf.account.gen.AccountRecordGen
import com.demo.bsf.account.gen.TransactionRecordGen
import com.demo.bsf.account.transaction.Transaction
import com.demo.bsf.account.transaction.TransactionMapper
import com.demo.bsf.account.transaction.TransactionRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class AccountServiceTest {

    val accountRepo: AccountRepository = mock(AccountRepository::class.java)
    val transactionRepo: TransactionRepository = mock(TransactionRepository::class.java)
    val accountMapper: AccountMapper = spy(AccountMapper())
    val transactionMapper: TransactionMapper = spy(TransactionMapper())
    val unit: AccountService = AccountService(accountRepo, transactionRepo, accountMapper, transactionMapper)

    @Test
    fun should_fail_when_target_account_is_equal_to_source_account() {
        val accountId = UUID.randomUUID()
        val request = transferRequest().apply { targetAccountId = accountId }
        assertThatThrownBy { unit.transferMoney(accountId, request) }
            .isInstanceOf(BadRequestException::class.java)
            .hasMessageContaining("Source account can't be equal to target account. Account ID: $accountId")
    }

    @Test
    fun should_fail_with_invalid_amount() {
        // given
        val accountId = UUID.randomUUID()
        val request = transferRequest().apply { amount = 0 }

        // then
        assertThatThrownBy { unit.transferMoney(accountId, request) }
            .isInstanceOf(BadRequestException::class.java)
            .hasMessageContaining("Amount must be higher than 0, but it is: 0")
    }

    @Test
    fun should_fail_when_source_account_not_exist() {
        // given
        val sourceAccount: AccountRecord = AccountRecordGen.accountRecord()
        val targetAccount: AccountRecord = AccountRecordGen.accountRecord()
        `when`(accountRepo.findByIdsForUpdate(listOf(sourceAccount.id, targetAccount.id)))
            .thenReturn(mapOf(targetAccount.id to targetAccount))

        val request = transferRequest().apply { targetAccountId = targetAccount.id }

        // then
        assertThatThrownBy { unit.transferMoney(sourceAccount.id, request) }
            .isInstanceOf(NotFoundException::class.java)
            .hasMessageContaining("Account with ID ${sourceAccount.id} not found")
    }

    @Test
    fun should_fail_when_target_account_not_exist() {
        // given
        val sourceAccount: AccountRecord = AccountRecordGen.accountRecord()
        val targetAccount: AccountRecord = AccountRecordGen.accountRecord()
        `when`(accountRepo.findByIdsForUpdate(listOf(sourceAccount.id, targetAccount.id)))
            .thenReturn(mapOf(sourceAccount.id to sourceAccount))

        val request = transferRequest().apply { targetAccountId = targetAccount.id }

        // then
        assertThatThrownBy { unit.transferMoney(sourceAccount.id, request) }
            .isInstanceOf(NotFoundException::class.java)
            .hasMessageContaining("Account with ID ${targetAccount.id} not found")
    }

    @Test
    fun should_fail_when_not_enough_balance() {
        // given
        val sourceAccount: AccountRecord = AccountRecordGen.accountRecord().apply { balanceAmount = 100 }
        val targetAccount: AccountRecord = AccountRecordGen.accountRecord()
        `when`(accountRepo.findByIdsForUpdate(listOf(sourceAccount.id, targetAccount.id)))
            .thenReturn(
                mapOf(
                    sourceAccount.id to sourceAccount,
                    targetAccount.id to targetAccount
                )
            )

        val request = transferRequest().apply {
            targetAccountId = targetAccount.id
            amount = 101
        }

        // then
        assertThatThrownBy { unit.transferMoney(sourceAccount.id, request) }
            .isInstanceOf(BadRequestException::class.java)
            .hasMessageContaining(
                "Not enough balance on account ${sourceAccount.id} "
                        + "to perform transfer for the amount: 101"
            )
    }

    @Test
    fun should_fail_when_transaction_already_exist() {
        // given
        val transactionId = UUID.randomUUID()
        val sourceAccount: AccountRecord = AccountRecordGen.accountRecord()
        val targetAccount: AccountRecord = AccountRecordGen.accountRecord()
        `when`(accountRepo.findByIdsForUpdate(listOf(sourceAccount.id, targetAccount.id)))
            .thenReturn(
                mapOf(
                    sourceAccount.id to sourceAccount,
                    targetAccount.id to targetAccount
                )
            )

        val request = transferRequest().apply {
            targetAccountId = targetAccount.id
            amount = 101
            this.transactionId = transactionId
        }

        `when`(transactionRepo.findById(transactionId)).thenReturn(TransactionRecordGen.transactionRecord())

        // then
        assertThatThrownBy { unit.transferMoney(sourceAccount.id, request) }
            .isInstanceOf(BadRequestException::class.java)
            .hasMessageContaining("Transaction with ID $transactionId already exist")
    }

    @Test
    fun should_transfer_money() {
        // given
        val transactionId = UUID.randomUUID()
        val sourceAccount: AccountRecord = AccountRecordGen.accountRecord().apply {
            balanceAmount = 100
        }
        val targetAccount: AccountRecord = AccountRecordGen.accountRecord().apply {
            balanceAmount = 100
        }
        `when`(accountRepo.findByIdsForUpdate(listOf(sourceAccount.id, targetAccount.id)))
            .thenReturn(
                mapOf(
                    sourceAccount.id to sourceAccount,
                    targetAccount.id to targetAccount
                )
            )

        val request = transferRequest().apply {
            targetAccountId = targetAccount.id
            amount = 50
            this.transactionId = transactionId
        }

        // when
        val transaction: Transaction = unit.transferMoney(sourceAccount.id, request)

        // then
        verify(transactionRepo).findById(transactionId)
        verify(accountRepo).findByIdsForUpdate(listOf(sourceAccount.id, targetAccount.id))
        verify(accountRepo).update(sourceAccount)
        verify(accountRepo).update(targetAccount)

        assertThat(sourceAccount.balanceAmount).isEqualTo(50)
        assertThat(targetAccount.balanceAmount).isEqualTo(150)
        with(transaction) {
            assertThat(id).isEqualTo(transactionId)
            assertThat(amount).isEqualTo(50)
            assertThat(sourceAccountId).isEqualTo(sourceAccount.id)
            assertThat(accountId).isEqualTo(targetAccount.id)
            assertThat(description).isEqualTo(request.description)
            assertThat(transaction.createdAt).isNotNull()
        }
    }

    fun transferRequest(): AccountMoneyTransferRequest {
        return AccountMoneyTransferRequest(
            targetAccountId = UUID.randomUUID(),
            description = "Money transfer notes",
            amount = 100_000,
            transactionId = UUID.randomUUID()
        )
    }

}