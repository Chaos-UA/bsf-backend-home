package com.demo.bsf.account.account

import com.demo.bsf.account.account.dto.Account
import com.demo.bsf.account.account.dto.AccountMoneyTransferRequest
import com.demo.bsf.account.transaction.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class AccountApiController @Autowired constructor(private val accountService: AccountService) {

    @GetMapping("/api/v1/accounts/{accountId}")
    fun getAccount(@PathVariable accountId: UUID): Account {
        return accountService.getAccount(accountId)
    }

    @GetMapping("/api/v1/accounts")
    fun getAccounts(): List<Account> {
        return accountService.getAccounts()
    }

    @PostMapping("/api/v1/accounts/{accountId}/transfer-money")
    fun transferMoney(@PathVariable accountId: UUID,
                      @RequestBody transferMoneyRequest: AccountMoneyTransferRequest): Transaction {
        return accountService.transferMoney(accountId, transferMoneyRequest)
    }
}