package com.kata.bank.domain.service;

import java.math.BigDecimal;

public interface IAccountService {

    void addAccountOperation(String accountNumber, BigDecimal operationAmount);
}
