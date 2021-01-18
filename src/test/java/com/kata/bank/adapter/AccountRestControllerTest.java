package com.kata.bank.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.kata.bank.BankApplication;
import com.kata.bank.domain.service.AccountService;
import com.kata.bank.dto.DepositDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

}
