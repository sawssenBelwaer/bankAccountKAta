package com.kata.bank.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class AccountOperation {

    private Long operationNumber;
    private BigDecimal amount;
    private LocalDateTime date;

}
