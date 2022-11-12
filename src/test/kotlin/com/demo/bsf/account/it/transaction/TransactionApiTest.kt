package com.demo.bsf.account.it.transaction

import com.demo.bsf.account.account.AccountRepository
import com.demo.bsf.account.db.gen.tables.records.AccountRecord
import com.demo.bsf.account.db.gen.tables.records.TransactionRecord
import com.demo.bsf.account.gen.AccountRecordGen
import com.demo.bsf.account.gen.TransactionRecordGen
import com.demo.bsf.account.it.BaseIntegrationTest
import com.demo.bsf.account.transaction.TransactionRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

class TransactionApiTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val accountRepo: AccountRepository,
    val transactionRepo: TransactionRepository) : BaseIntegrationTest()  {

    @Test
    fun should_list_transactions() {
        // given
        val account: AccountRecord = AccountRecordGen.accountRecord()
        accountRepo.create(account)
        val sourceAccount: AccountRecord = AccountRecordGen.accountRecord()
        accountRepo.create(sourceAccount)
        val transaction: TransactionRecord = TransactionRecordGen.transactionRecord(account.id, sourceAccount.id)
        transactionRepo.create(transaction)

        // when
        val results = mockMvc.perform(get("/api/v1/accounts/${account.id}/transactions"))
            .andExpect(status().isOk)

        // then
        val jsonResponse = objectMapper.readTree(results.andReturn().response.contentAsByteArray)!!
        assertThat(jsonResponse.size()).isEqualTo(1)
        with(jsonResponse.get(0)) {
            assertThat(get("id").textValue()).isEqualTo(transaction.id.toString())
            assertThat(get("accountId").textValue()).isEqualTo(transaction.accountId.toString())
            assertThat(get("sourceAccountId").textValue()).isEqualTo(transaction.sourceAccountId.toString())
            assertThat(get("operationType").textValue()).isEqualTo(transaction.operationType.toString())
            assertThat(get("description").textValue()).isEqualTo(transaction.description)
            assertThat(get("amount").intValue()).isEqualTo(transaction.amount)
            assertThat(LocalDateTime.parse(get("createdAt").textValue())).isEqualTo(transaction.createdAt)
        }
    }
}