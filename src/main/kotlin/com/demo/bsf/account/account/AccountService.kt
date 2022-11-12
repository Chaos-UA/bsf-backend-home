package com.demo.bsf.account.account

import com.demo.bsf.account.account.dto.Account
import com.demo.bsf.account.account.dto.AccountMoneyTransferRequest
import com.demo.bsf.account.db.gen.tables.records.AccountRecord
import com.demo.bsf.account.db.gen.tables.records.TransactionRecord
import com.demo.bsf.account.exception.BadRequestException
import com.demo.bsf.account.exception.ConflictException
import com.demo.bsf.account.exception.NotFoundException
import com.demo.bsf.account.transaction.Transaction
import com.demo.bsf.account.transaction.TransactionMapper
import com.demo.bsf.account.transaction.TransactionOperationType
import com.demo.bsf.account.transaction.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors

@Service
class AccountService @Autowired constructor(
    private val accountRepo: AccountRepository,
    private val transactionRepo: TransactionRepository,
    private val accountMapper: AccountMapper,
    private val transactionMapper: TransactionMapper) {

    @Transactional(readOnly = true)
    fun getAccount(accountId: UUID): Account {
        return accountMapper.toAccount(accountRepo.findById(accountId) ?: throw accountNotFound(accountId))
    }

    @Transactional(readOnly = true)
    fun getAccounts(): List<Account> {
        return accountRepo.getAccounts().stream()
            .map { accountMapper.toAccount(it) }
            .collect(Collectors.toList())
    }

    @Transactional
    fun transferMoney(accountId: UUID, request: AccountMoneyTransferRequest): Transaction {
        if (accountId.equals(request.targetAccountId)) {
            throw BadRequestException("Source account can't be equal to target account. Account ID: $accountId")
        }
        if (request.amount <= 0) {
            throw BadRequestException("Amount must be higher than 0, but it is: ${request.amount}")
        }
        val accountsMap: Map<UUID, AccountRecord> = accountRepo.findByIdsForUpdate(
            listOf(accountId, request.targetAccountId))
        val sourceAccount: AccountRecord = accountsMap.get(accountId) ?: throw accountNotFound(accountId)
        val targetAccount: AccountRecord = accountsMap.get(request.targetAccountId)
            ?: throw accountNotFound(request.targetAccountId)

        if (sourceAccount.balanceAmount < request.amount) {
            throw BadRequestException("Not enough balance on account ${accountId}"
                    + " to perform transfer for the amount: ${request.amount}")
        }

        if (request.transactionId?.let { transactionRepo.findById(it) } != null) {
            throw ConflictException("Transaction with ID ${request.transactionId} already exist")
        }

        val transactionId: UUID = request.transactionId ?: UUID.randomUUID()
        return doMoneyTransfer(sourceAccount, targetAccount, transactionId, request.amount, request.description);
    }

    private fun doMoneyTransfer(sourceAccount: AccountRecord,
                                targetAccount: AccountRecord,
                                transactionId: UUID,
                                transferAmount: Long,
                                transferDescription: String?): Transaction {
        val dateTime = LocalDateTime.now();
        sourceAccount.balanceAmount += -transferAmount
        targetAccount.balanceAmount += transferAmount

        val transaction: TransactionRecord = TransactionRecord().apply {
            id = transactionId
            accountId = targetAccount.id
            sourceAccountId = sourceAccount.id
            operationType = TransactionOperationType.ACCOUNT_MONEY_TRANSFER.name
            description = transferDescription
            amount = transferAmount
            createdAt = dateTime
        }

        transactionRepo.create(transaction)
        accountRepo.update(sourceAccount)
        accountRepo.update(targetAccount)
        return transactionMapper.toTransaction(transaction);
    }

    private fun accountNotFound(accountId: UUID): NotFoundException {
        return NotFoundException("Account", accountId);
    }
}