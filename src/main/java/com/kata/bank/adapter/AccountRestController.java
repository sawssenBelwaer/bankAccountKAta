package com.kata.bank.adapter;

import com.kata.bank.domain.model.Account;
import com.kata.bank.domain.service.IAccountService;
import com.kata.bank.domain.service.IOperationService;
import com.kata.bank.dto.AccountDto;
import com.kata.bank.dto.DepositDto;
import com.kata.bank.dto.OperationDto;
import com.kata.bank.dto.WithdrawDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("account")
public class AccountRestController {
    private final IAccountService accountService;
    private final IOperationService operationService;

    public AccountRestController(IAccountService accountService, IOperationService operationService) {
        this.accountService = accountService;
        this.operationService = operationService;
    }

    @PutMapping(value = "/{accountNr}/deposit")
    public ResponseEntity makeDeposit(@PathVariable String accountNr, @RequestBody DepositDto depositDto) {
        accountService.addAccountOperation(accountNr, depositDto.getAmount());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping(value = "/{accountNr}/withdraw")
    public ResponseEntity withdraw(@PathVariable String accountNr, @RequestBody WithdrawDto withdrawDto) {
        accountService.addAccountOperation(accountNr, withdrawDto.getAmount().negate());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping(value = "/{accountNr}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> getAccount(@PathVariable String accountNr) {
        Account account = accountService.getAccountByNumber(accountNr);

        return ResponseEntity.ok(AccountDto.builder()
                .balance(account.getBalance())
                .creationDate(account.getCreationDate())
                .overdraft(account.getOverdraft())
                .build());
    }

    @GetMapping(value = "/{accountNr}/history")
    public Collection<OperationDto> getHistory(@PathVariable String accountNr) {
        return operationService.getAccountOperations(accountNr)
                .stream()
                .map(o -> OperationDto.builder()
                        .amount(o.getAmount())
                        .date(o.getDate())
                        .build())
                .collect(Collectors.toList());
    }
}
