package com.kata.bank.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.kata.bank.BankApplication;
import com.kata.bank.domain.model.Account;
import com.kata.bank.domain.model.AccountOperation;
import com.kata.bank.domain.service.AccountService;
import com.kata.bank.domain.service.OperationService;
import com.kata.bank.dto.AccountDto;
import com.kata.bank.dto.DepositDto;
import com.kata.bank.dto.OperationDto;
import com.kata.bank.dto.WithdrawDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankApplication.class)
@AutoConfigureMockMvc
@Slf4j
public class AccountRestControllerTest {

    private static final Gson OBJECT_MAPPER =
            new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                            LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();

    @Autowired
    private AccountRestController accountRestController;
    @MockBean
    private AccountService accountService;
    @MockBean
    private OperationService operationService;
    @Autowired
    private MockMvc mockMvc;


    @Test
    public void should_succeed_to_make_a_deposit() throws Exception {

        //prepare
        String accountNumber = "1234";
        BigDecimal amount = BigDecimal.valueOf(24);
        Mockito.doNothing().when(accountService).addAccountOperation(accountNumber, amount);

        //execute
        mockMvc.perform(MockMvcRequestBuilders.put("/account/1234/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.toJson(
                        new DepositDto().setAmount(BigDecimal.valueOf(24))
                )))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.ACCEPTED.value()));

        //verify
        Mockito.verify(accountService).addAccountOperation(accountNumber, amount);
    }

    @Test
    public void should_succeed_to_make_a_withdraw() throws Exception {

        //prepare
        String accountNumber = "1234";
        BigDecimal amount = BigDecimal.valueOf(30);
        Mockito.doNothing().when(accountService).addAccountOperation(accountNumber, amount);

        //execute
        mockMvc.perform(MockMvcRequestBuilders.put("/account/1234/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.toJson(
                        new WithdrawDto().setAmount(BigDecimal.valueOf(30))
                )))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.ACCEPTED.value()));

        //verify
        Mockito.verify(accountService).addAccountOperation(accountNumber, amount.negate());
    }

    @Test
    public void should_return_account_information_for_a_given_accountNr() throws Exception {

        //prepare
        String accountNumber = "1234";
        Account account = Account.builder()
                .number(accountNumber)
                .overdraft(BigDecimal.valueOf(23))
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .balance(BigDecimal.valueOf(2333.45))
                .build();

        when(accountService.getAccountByNumber(accountNumber)).thenReturn(account);

        //execute
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/account/1234")).andReturn();

        //assert
        String jsonAccount = mvcResult.getResponse().getContentAsString();
        AccountDto resultAccount = OBJECT_MAPPER.fromJson(jsonAccount, AccountDto.class);

        Assertions.assertEquals(AccountDto.builder()
                        .balance(account.getBalance())
                        .creationDate(account.getCreationDate())
                        .overdraft(account.getOverdraft())
                        .build()
                , resultAccount, "Expected and result accounts must be equal");
    }

    @Test
    public void test_should_return_operations_history() throws Exception {

        //prepare
        String accountNumber = "1234";
        Account account = Account.builder()
                .number(accountNumber)
                .overdraft(BigDecimal.valueOf(23))
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .balance(BigDecimal.valueOf(2333.45))
                .build();
        Long operationNumber = 1234L;
        AccountOperation accountoperation = AccountOperation.builder()
                .operationNumber(operationNumber)
                .date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .amount(BigDecimal.valueOf(33.0))
                .build();

        when(accountService.getAccountByNumber(accountNumber)).thenReturn(account);
        when(operationService.getAccountOperations(accountNumber)).thenReturn(List.of(accountoperation));

        //execute
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/account/1234/history")).andReturn();

        //assert

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Type listType = new TypeToken<ArrayList<OperationDto>>() {
        }.getType();
        Collection<OperationDto> resultOperations = OBJECT_MAPPER.fromJson(jsonResponse, listType);

        OperationDto expectedOperationDto = OperationDto.builder()
                .amount(accountoperation.getAmount())
                .date(accountoperation.getDate())
                .build();

        assertThat(resultOperations, hasSize(1));
        assertThat(resultOperations, contains(expectedOperationDto));

    }

}
