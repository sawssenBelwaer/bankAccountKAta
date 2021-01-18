package com.kata.bank.domain.service;

import com.kata.bank.infrastructure.entity.OperationEntity;
import com.kata.bank.infrastructure.repository.AccountRepository;
import com.kata.bank.infrastructure.repository.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
class OperationService implements IOperationService {

    private final OperationRepository operationRepository;
    private final AccountRepository accountRepository;

    public OperationService(OperationRepository operationRepository, AccountRepository accountRepository) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public void addAccountOperation(String accountNumber, BigDecimal operationAmount) {

        //store operation
        operationRepository.save(new OperationEntity()
                .setAccount(accountRepository.getOne(accountNumber))
                .setOperationDate(LocalDateTime.now())
                .setAmount(operationAmount));
    }
}