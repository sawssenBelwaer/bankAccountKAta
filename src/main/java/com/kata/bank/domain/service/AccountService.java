package com.kata.bank.domain.service;

import com.kata.bank.domain.model.Account;
import com.kata.bank.infrastructure.entity.AccountEntity;
import com.kata.bank.infrastructure.repository.AccountRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public
class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final OperationService operationService;

    public AccountService(AccountRepository accountRepository, OperationService operationService) {
        this.accountRepository = accountRepository;
        this.operationService = operationService;
    }

    @Override
    @Transactional
    public void addAccountOperation(String accountNumber, BigDecimal operationAmount) {

        AccountEntity accountEntity = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new ObjectNotFoundException(accountNumber, AccountEntity.class.getSimpleName()));


        Account account = Account.builder()
                .balance(accountEntity.getBalance())
                .creationDate(accountEntity.getCreationDate())
                .number(accountEntity.getNumber())
                .overdraft(accountEntity.getOverdraft())
                .build();

        //do operation
        account.addAmount(operationAmount);

        //update balance
        accountEntity.setBalance(account.getBalance());
        accountRepository.save(accountEntity);

        //call operation service
        operationService.addAccountOperation(accountNumber, operationAmount);
    }

    @Override
    @Transactional
    public Account getAccountByNumber(String accountNumber) {

        AccountEntity entity = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new ObjectNotFoundException(accountNumber, AccountEntity.class.getSimpleName()));

        return Account.builder()
                .balance(entity.getBalance())
                .creationDate(entity.getCreationDate())
                .number(entity.getNumber())
                .overdraft(entity.getOverdraft())
                .build();

    }
}