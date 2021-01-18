package com.kata.bank.domain.service;

import com.kata.bank.domain.model.Account;

import java.math.BigDecimal;

public interface IAccountService {

    void addAccountOperation(String accountNumber, BigDecimal operationAmount);

    Account getAccountByNumber(String accountNumber);
}
