package com.kata.bank.domain.service;

import java.math.BigDecimal;

public interface IOperationService {

    void addAccountOperation(String accountNumber, BigDecimal operationAmount);
}