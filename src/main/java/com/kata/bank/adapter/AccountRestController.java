package com.kata.bank.adapter;

import com.kata.bank.domain.service.IAccountService;
import com.kata.bank.dto.DepositDto;
import com.kata.bank.dto.WithdrawDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account")
public class AccountRestController {
    private final IAccountService accountService;

    public AccountRestController(IAccountService accountService) {
        this.accountService = accountService;
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
}
