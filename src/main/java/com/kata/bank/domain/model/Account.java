package com.kata.bank.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class Account {
    private String number;
    private LocalDateTime creationDate;
    private BigDecimal balance;
    private BigDecimal overdraft;


    public void addAmount(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
}
