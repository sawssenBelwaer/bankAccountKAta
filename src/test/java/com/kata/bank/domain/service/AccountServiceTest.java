package com.kata.bank.domain.service;

import com.kata.bank.BankApplication;
import com.kata.bank.domain.model.Account;
import com.kata.bank.domain.model.AccountOperation;
import com.kata.bank.infrastructure.entity.AccountEntity;
import com.kata.bank.infrastructure.entity.OperationEntity;
import com.kata.bank.infrastructure.repository.AccountRepository;
import com.kata.bank.infrastructure.repository.OperationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankApplication.class)
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private OperationService operationService;
    @MockBean
    private OperationRepository operationRepository;
    @Mock
    private Account account;


    @Test
    public void should_succeed_to_add_accountOperation() {

        //prepare
        String accountNumber = "1234";
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(BigDecimal.valueOf(230));
        accountEntity.setCreationDate(LocalDateTime.now());
        accountEntity.setNumber(accountNumber);
        accountEntity.setOverdraft(BigDecimal.valueOf(34));

        Long operationNumber = 1234L;
        BigDecimal operationAmount = BigDecimal.TEN;
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setAmount(operationAmount);
        operationEntity.setOperationDate(LocalDateTime.now());
        operationEntity.setOperationNumber(operationNumber);

        AccountOperation accountoperation = AccountOperation.builder()
                .operationNumber(operationNumber)
                .date(LocalDateTime.now())
                .amount(BigDecimal.valueOf(33))
                .build();

        when(accountRepository.findById(accountNumber)).thenReturn(Optional.ofNullable(accountEntity));


        //execute
        accountService.addAccountOperation(accountNumber, operationAmount);
        account.addAmount(operationAmount);

        //assert
        verify(accountRepository).findById(accountNumber);
        verify(operationService).addAccountOperation(accountNumber, operationAmount);
        verify(account).addAmount(operationAmount);
        verify(accountRepository).save(accountEntity);

    }


}
