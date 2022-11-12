package com.demo.bsf.account.it.account

import com.demo.bsf.account.account.AccountRepository
import com.demo.bsf.account.db.gen.tables.records.AccountRecord
import com.demo.bsf.account.gen.AccountRecordGen
import com.demo.bsf.account.it.BaseIntegrationTest
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AccountApiTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val accountRepo: AccountRepository) : BaseIntegrationTest()  {

    @Test
    fun should_get_account_by_id() {
        // given
        val account: AccountRecord = createAccount(AccountRecordGen.accountRecord())

        // when
        val accountResult = mockMvc.perform(get("/api/v1/accounts/${account.id}"))
            .andExpect(status().isOk)

        // then
        with(objectMapper.readTree(accountResult.andReturn().response.contentAsByteArray)) {
            assertThat(get("id").textValue()).isEqualTo(account.id.toString())
            assertThat(get("ownerId").textValue()).isEqualTo(account.ownerId.toString())
            assertThat(get("balanceAmount").intValue()).isEqualTo(account.balanceAmount)
            assertThat(get("createdAt").textValue()).isEqualTo(account.createdAt.toString())
            assertThat(get("modifiedAt").textValue()).isEqualTo(account.modifiedAt.toString())
        }
    }

    @Test
    fun should_get_account_by_list_and_the_same_by_id() {
        // given
        createAccount(AccountRecordGen.accountRecord())

        // when
        val accountsJsonArray: JsonNode = objectMapper.readTree(mockMvc.perform(get("/api/v1/accounts"))
            .andExpect(status().isOk).andReturn().response.contentAsByteArray)

        val accountJson = objectMapper.readTree(mockMvc.perform(
            get("/api/v1/accounts/${accountsJsonArray.get(0).get("id").textValue()}"))
            .andExpect(status().isOk).andReturn().response.contentAsByteArray)

        // then
        assertThat(accountJson).isEqualTo(accountsJsonArray.get(0))
    }

    fun createAccount(account: AccountRecord): AccountRecord {
        accountRepo.create(account);
        return accountRepo.findById(account.id)!!
    }

}