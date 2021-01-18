package com.kata.bank.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class OperationEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long operationNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationDate;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_NR")
    private AccountEntity account;

}
