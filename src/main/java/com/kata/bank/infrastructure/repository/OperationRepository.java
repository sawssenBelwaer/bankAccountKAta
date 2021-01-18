package com.kata.bank.infrastructure.repository;


import com.kata.bank.infrastructure.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {


    List<OperationEntity> findByAccountNumber(String accountNumber);
}