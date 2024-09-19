package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@Validated
@RestController
@RequestMapping("balance")
public class BalanceController {

    private final BalanceService balanceService;

    @Autowired
    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }


    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public BigDecimal getBalance() {
        return balanceService.retrieveBalance();
    }
}
