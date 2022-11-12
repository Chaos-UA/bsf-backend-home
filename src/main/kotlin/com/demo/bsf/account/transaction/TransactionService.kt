package com.demo.bsf.account.transaction

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

@Service
class TransactionService @Autowired constructor(
    private val transactionRepo: TransactionRepository,
    private val transactionMapper: TransactionMapper) {

    fun getAccountTransactions(accountId: UUID): List<Transaction> {
        return transactionRepo.listTransactions(accountId).stream()
            .map { transactionMapper.toTransaction(it) }
            .collect(Collectors.toList())
    }
}