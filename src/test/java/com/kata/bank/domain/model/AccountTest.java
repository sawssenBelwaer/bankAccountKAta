package com.kata.bank.domain.model;

import com.kata.bank.BankApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankApplication.class)
public class AccountTest {

    Account account;

    @Test
    public void should_succeed_to_add_amount_to_account() {
        //prepare
        String accountNumber = "1234";
        account = new Account(accountNumber, LocalDateTime.now(), BigDecimal.valueOf(3220), BigDecimal.valueOf(30));

        BigDecimal amount = BigDecimal.TEN;
        //execute
        account.addAmount(amount);
        //assert
        assertEquals(BigDecimal.valueOf(3230), account.getBalance());

    }
}
