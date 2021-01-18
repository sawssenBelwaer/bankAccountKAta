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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankApplication.class)
public class OperationServiceTest {
    @Autowired
    private OperationService operationService;
    @MockBean
    private OperationRepository operationRepository;
    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void should_get_AccountOperation() {
        //prepare
        String accountNumber = "1234";
        AccountEntity entity = new AccountEntity();
        entity.setBalance(BigDecimal.valueOf(230));
        entity.setCreationDate(LocalDateTime.now());
        entity.setNumber(accountNumber);
        entity.setOverdraft(BigDecimal.valueOf(34));

        when(accountRepository.findById(accountNumber)).thenReturn(Optional.ofNullable(entity));
        Long operationNumber = 1234L;
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setAmount(BigDecimal.valueOf(230));
        operationEntity.setAccount(entity);

        when(operationRepository.findById(operationNumber)).thenReturn(Optional.ofNullable(operationEntity));

        //execute
        Collection<AccountOperation> result = operationService.getAccountOperations(accountNumber);

        //assert
        Account accountResult = Account.builder()
                .balance(entity.getBalance())
                .creationDate(entity.getCreationDate())
                .number(entity.getNumber())
                .overdraft(entity.getOverdraft())
                .build();
        ;
        assertThat(result.contains(accountResult));
    }

    @Test
    public void should_add_AccountOperation() {
        //prepare
        String accountNumber = "1234";
        AccountEntity entity = new AccountEntity();
        entity.setBalance(BigDecimal.valueOf(230));
        entity.setCreationDate(LocalDateTime.now());
        entity.setNumber(accountNumber);
        entity.setOverdraft(BigDecimal.valueOf(34));

        when(accountRepository.findById(accountNumber)).thenReturn(Optional.ofNullable(entity));

        Long operationNumber = 1234L;
        BigDecimal operationAmount = BigDecimal.valueOf(230);

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setAmount(operationAmount);
        operationEntity.setAccount(entity);

        when(operationRepository.save(operationEntity)).thenReturn(operationEntity);


        //execute
        operationService.addAccountOperation(accountNumber, operationAmount);

        verify(operationRepository, times(1)).save(Mockito.any(OperationEntity.class));
    }
}