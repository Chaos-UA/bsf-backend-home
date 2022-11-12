package com.demo.bsf.account.transaction

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class TransactionApiController @Autowired constructor(private val transactionService: TransactionService) {

    @GetMapping("/api/v1/accounts/{accountId}/transactions")
    fun getAccountTransactions(@PathVariable accountId: UUID): List<Transaction> {
        return transactionService.getAccountTransactions(accountId)
    }
}