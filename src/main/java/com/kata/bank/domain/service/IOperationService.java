package com.kata.bank.domain.service;

import com.kata.bank.domain.model.AccountOperation;

import java.math.BigDecimal;
import java.util.Collection;

public interface IOperationService {

    void addAccountOperation(String accountNumber, BigDecimal operationAmount);

    Collection<AccountOperation> getAccountOperations(String accountNumber);
}